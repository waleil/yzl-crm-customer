package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.activity.model.responseModel.ActivityProductResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.customer.dao.MemberDiseaseMapper;
import cn.net.yzl.crm.customer.dao.MemberGradeRecordDao;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.ProductConsultationMapper;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.feign.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.feign.client.product.ProductFien;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.service.impl.phone.MemberPhoneServiceImpl;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.MemberDiseaseIdUpdateVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberCrowdGroupDao memberCrowdGroupDao;
    @Autowired
    CustomerGroupService customerGroupService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    MemberDiseaseMapper memberDiseaseMapper;

    @Autowired
    MemberPhoneServiceImpl memberPhoneService;
    @Autowired
    MemberLabelDao memberLabelDao;
    @Autowired
    ActivityFien activityFien;
    @Autowired
    ProductFien productFien;
    @Autowired
    ProductConsultationMapper productConsultationMapper;


    private String memberCountkey="memeberCount";

    /**
     * wangzhe
     * 2021-01-25
     * @param member
     * @return
     */
    @Override
    @Transactional
    public int insert(Member member) {
        //生成顾客会员卡号缓存的key
        String cacheKey = CacheKeyUtil.maxMemberCardCacheKey();
        //生成顾客会员卡号
        long maxMemberCard = redisUtil.incr(cacheKey, 1);
        //设置顾客会员卡号
        member.setMember_card(String.valueOf(maxMemberCard));
        //保存数据
        int result = memberMapper.insertSelective(member);
        return result;
    }

    @Override
    public ComResponse<Page<Member>> findPageByCondition(MemberSerchConditionDTO dto) {

        // 获取数量
//
//       int num =  memberMapper.findPageByConditionCount(dto);
//       if(num<1){
//           return ComResponse.nodata();
//       }
//        Page<Member> page = new Page<>();
//        PageParam pageParam = new PageParam();
//        pageParam.setTotalCount(num);
//        pageParam.setPageSize(dto.getPageSize());
//        pageParam.setPageNo(dto.getCurrentPage());
//        pageParam.setNextPage((int)Math.ceil(num/dto.getPageSize()));
//
//        page.setPageParam(pageParam);
//        dto.setFromLine((dto.getCurrentPage()-1)*dto.getPageSize());
//        List<Member> list = memberMapper.findPageByCondition(dto);
//        page.setItems(list);


        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<Member> list = memberMapper.findPageByCondition(dto);
        if(list==null || list.size()<0){
            return ComResponse.nodata();
        }
        Page<Member> page = AssemblerResultUtil.resultAssembler(list);

        return ComResponse.success(page);
    }
    @Override
    public List<MemberProductEffect> getMemberProductEffectList(String member_card) {
        return memberMapper.getMemberProductEffectList(member_card);
    }
    @Override
    public int updateByMemberCardSelective(Member dto) {
        return memberMapper.updateByMemberCardSelective(dto);
    }

    @Override
    public Member selectMemberByCard(String memberCard) {
        return memberMapper.selectMemberByCard(memberCard);
    }

    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     *
     * @param member_card
     * @return
     */
    @Override
    public List<MemberPhone> getMemberPhoneList(String member_card) {
        return memberMapper.getMemberPhoneList(member_card);
    }

    /**
     * 根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）
     *
     * @param phone
     * @return
     */
    @Override
    public Member getMemberByPhone(String phone) {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(phone);
        phoneList.add("0" + phone);
        phoneList.add("00" + phone);
        return memberMapper.getMemberByPhone(phoneList); //因为手机号或许有前缀0，所以要加0的判断
    }

    /**
     * 设置顾客为会员
     *
     * @param member_card
     */
    @Override
    public void setMemberToVip(String member_card) {
        memberMapper.setMemberToVip(member_card);
    }


    /**
     * 获取咨询商品
     *
     * @param member_card
     * @return
     */
    @Override
    public List<ProductConsultation> getProductConsultationList(String member_card) {
        return memberMapper.getProductConsultationList(member_card);
    }

    @Override
    public ComResponse<Integer> insertMemberDisease(MemberDiseaseDto memberDiseaseDto) {
        List<MemberDisease> memberDiseaseByCardAndDiseaseId = memberMapper.getMemberDiseaseByCardAndDiseaseId(memberDiseaseDto.getMemberCard(), memberDiseaseDto.getDiseaseId());
        if(memberDiseaseByCardAndDiseaseId!=null && memberDiseaseByCardAndDiseaseId.size()>0){
            return ComResponse.fail(ResponseCodeEnums.MEMBER_DISEASE_EXIST_ERROR.getCode(),ResponseCodeEnums.MEMBER_DISEASE_EXIST_ERROR.getMessage());
        }
        Integer integer = memberMapper.insertMemberDisease(memberDiseaseDto);
        return ComResponse.success(integer);
    }

    @Override
    public ComResponse<List<MemberDiseaseCustomerDto>> getMemberDisease(String memberCard) {
        List<MemberDiseaseCustomerDto> list = memberMapper.getMemberDiseaseDtoByMemberCard(memberCard);
        if(list==null || list.size()<1){
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }

    @Override
    public void saveReveiverAddress(ReveiverAddress reveiverAddress) {
        memberMapper.saveReveiverAddress(reveiverAddress);
    }

    @Override
    public void updateReveiverAddress(ReveiverAddress reveiverAddress) {
        memberMapper.updateReveiverAddress(reveiverAddress);
    }

    @Override
    public List<ReveiverAddress> getReveiverAddress(String member_card) {
        return memberMapper.getReveiverAddress(member_card);
    }

    @Override
    public MemberOrderStat getMemberOrderStat(String member_card) {
        return memberMapper.getMemberOrderStat(member_card);
    }

    /**
     * 保存顾客购买能力
     *
     * @param memberOrderStat
     */
    @Override
    public void addMemberOrderStat(MemberOrderStat memberOrderStat) {
        memberMapper.addMemberOrderStat(memberOrderStat);
    }

    /**
     * 修改顾客购买能力
     *
     * @param memberOrderStat
     */
    @Override
    public void updateMemberOrderStat(MemberOrderStat memberOrderStat) {
        memberMapper.updateMemberOrderStat(memberOrderStat);
    }

//    /**
//     * 获取顾客行为偏好
//     *
//     * @param member_card
//     * @return
//     */
//    @Override
//    public MemberAction getMemberAction(String member_card) {
//        return memberMapper.getMemberAction(member_card);
//    }
//
//    @Override
//    public void saveMemberAction(MemberAction memberAction) {
//        memberMapper.saveMemberAction(memberAction);
//    }
//
//    @Override
//    public void updateMemberAction(MemberAction memberAction) {
//        memberMapper.updateMemberAction(memberAction);
//    }

    @Override
    public List<MemberOrderStatViewModel> getMemberList(List<String> member_cards) {
        return memberMapper.getMemberList(member_cards);
    }
    /**
     * 获取顾客行为偏好基础数据
     *
     * @return
     */
    @Override
    public List<MemberBaseAttr> getmemberActions() {
        return memberMapper.getmemberActions();
    }

    /**
     * 根据crowdId 删除顾客圈选
     *
     * @param crowdId
     */
    @Override
    public void delMemberCrowdGroup(String crowdId) {
        memberCrowdGroupDao.delMemberCrowdGroup(crowdId);
    }

    /**
     * 根据一批顾客卡号获取顾客病症信息
     * @param member_cards
     * @return
     */
    @Override
    public List<MemberDisease> getMemberDiseaseByMemberCards(List<String> member_cards) {
        return memberMapper.getMemberDiseaseByMemberCards(member_cards);
    }

    @Override
    public int addCrowdGroup(CrowdGroup crowdGroup) {
        return memberMapper.addCrowdGroup(crowdGroup);
    }

    /**
     * 修改圈选
     * @param crowdGroup
     */
    @Override
    public void updateCrowdGroup(CrowdGroup crowdGroup) {
        memberMapper.updateCrowdGroup(crowdGroup);
    }

    /**
     * 删除圈选
     * @param id
     */
    @Override
    public void delCrowdGroupById(int id) {
        memberMapper.delCrowdGroupById(id);
    }

    /**
     * 根据一批顾客卡号获取顾客账户信息
     * @param member_cards
     * @return
     */
    @Override
    public List getMemberAmount(List<String> member_cards) {
        return memberMapper.getMemberAmount(member_cards);
    }

    @Override
    public List<member_wide> selectFullMemberByPage(int currentPage, int pageSize) {
        //  PageHelper.startPage(currentPage, pageSize);
        List<member_wide> list = memberMapper.selectFullMemberByPage(currentPage*pageSize, pageSize);
        // Page<member_wide> page = AssemblerResultUtil.resultAssembler(list);

        return list;
    }

    @Override
    public member_wide getMemberFromMongo(String member_card) {
        return memberCrowdGroupDao.getMemberFromMongo(member_card);
    }

    @Override
    public void saveMemberToMongo(member_wide member) {
        memberCrowdGroupDao.saveMemberToMongo(member);
    }

    @Override
    public void updateMemberToMongo(member_wide member) throws Exception {
        memberCrowdGroupDao.updateMemberToMongo(member);
    }


    @Override
    @Transactional
    public ComResponse<String> addProductConsultation(List<ProductConsultationInsertVO> productConsultationInsertVOList) {
        for (ProductConsultationInsertVO productConsultationInsertVO : productConsultationInsertVOList) {

            cn.net.yzl.crm.customer.model.db.ProductConsultation productConsultation = new cn.net.yzl.crm.customer.model.db.ProductConsultation();
            BeanUtil.copyProperties(productConsultationInsertVO,productConsultation);

            int num =  productConsultationMapper.insertSelective(productConsultation);
            if(num<0) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE);
            }
        }
        return ComResponse.success();
    }

    @Override
    public List<MemberAddressAndLevelDTO> getMembereAddressAndLevelByMemberCards(List<String> memberCardList) {
        return memberMapper.getMembereAddressAndLevelByMemberCards(memberCardList);
    }

    @Autowired
    private MemberGradeRecordDao memberGradeRecordDao;
    @Override
    public ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordList(String memberCard) {

        List<MemberGradeRecordDto> list =  memberGradeRecordDao.getMemberGradeRecordList(memberCard);

        if(list==null || list.size()<1){
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }


    /**
     * 更新顾客病症id
     * @param vo
     * @return
     */
    @Override
    public Integer updateMemberDiseaseByDiseaseId(MemberDiseaseIdUpdateVO vo) {
       return memberDiseaseMapper.updateMemberDiseaseByDiseaseId(vo);
    }


    /**
     * 处理实时进线时，保存顾客信息
     * @param coilInVo
     * @return
     */
    @Override
    @Transactional
    public ComResponse<MemberGroupCodeDTO> coilInDealMemberData(MemberCoilInVO coilInVo){
        //判断当前号码是否已经使用
        ComResponse<Member> response = memberPhoneService.getMemberByphoneNumber(coilInVo.getCallerPhone());
        Integer status = response.getStatus();
        Member member = response.getData();
        String memberCard = "";
        String groupId = "";
        MemberLabel label = new MemberLabel();
        //会员不存在
        if (member == null) {
            //将顾客来电号码区分(座机/手机)插入member_phone表
            //新建客户信息
            ComResponse<String> addMemberResult = memberPhoneService.getMemberCardByphoneNumber(coilInVo.getCallerPhone());
            status = addMemberResult.getStatus();
            if (status != 200) {
            }
            if (addMemberResult.getData() == null) {
                return ComResponse.fail(addMemberResult.getCode(), addMemberResult.getMessage());
            }



            //新添加的会员卡号
            memberCard = addMemberResult.getData();
            //通过会员卡号查询会员信息
            member = selectMemberByCard(memberCard);
            //更新会员级别
            member.setMGradeId(1);
            member.setMGradeName("无卡");
            member.setM_grade_code(null);
            //更新会员信息
            int update = updateByMemberCardSelective(member);
            if (update < 0) {
                //TODO 更新会员信息失败
            }

            //将顾客信息插入顾客标签库：顾客编号、进线时间、咨询的商品、首次进线广告、媒体等；
            label.setMemberCard(member.getMember_card());
            label.setMediaId(coilInVo.getMediaId());
            label.setMediaName(coilInVo.getMediaName());
            label.setAdverCode(coilInVo.getAdvId());
            label.setAdverName(coilInVo.getAdvName());
            memberLabelDao.save(label);
            //添加会员咨询商品记录（更新product_consultation 去重）
           /* ComResponse<List<ActivityProductResponse>> activityProducts
                    = activityFien.getProductListByActivityBusNo((long) coilInVo.getAdvId());
            List<ActivityProductResponse> productList = activityProducts.getData();
            StringBuilder sb = new StringBuilder();
            if (CollectionUtil.isNotEmpty(productList)) {
                for (ActivityProductResponse product : productList) {
                    sb.append(product.getProductCode()).append(",");
                }
            }
            String productCodeStr = sb.length() > 0 ? sb.substring(0,sb.length()-1) : "";
            if (StringUtils.isNotEmpty(productCodeStr)) {
                ComResponse<List<ProductMainDTO>> productResult = productFien.queryByProductCodes(productCodeStr);
                List<ProductMainDTO> products = productResult.getData();
                //用于删除商品
                List<String> pcCodeList = new ArrayList<>();
                //咨询的商品集合
                List<ProductConsultationInsertVO> pcList = new ArrayList<>();
                Date date = new Date();
                for (ProductMainDTO dto : products) {
                    ProductConsultationInsertVO pc = new ProductConsultationInsertVO();
                    pc.setConsultationTime(date);
                    pc.setMemberCard(memberCard);
                    pc.setMemberCard(dto.getProductCode());
                    pc.setProductName(dto.getName());
                    pcCodeList.add(dto.getProductCode());
                    pcList.add(pc);
                }
                //删除该客户的匹配到的商品
                productConsultationMapper.deleteByMemberCardAndProductCodes(memberCard, pcCodeList);
                //批量保存
                ComResponse<String> saveResult = this.addProductConsultation(pcList);
            }*/
        }
        //会员已经存在的情景
        else{
            //判断顾客是否已经被圈选
            groupId = customerGroupService.queryGroupIdByMemberCard(member.getMember_card());
            //用于后面对该顾客进行圈选
            if (StringUtils.isEmpty(groupId)) {
                label.setMemberCard(member.getMember_card());
                label.setMemberCard(member.getMember_name());
            }
        }
        //没有圈选的客户进行圈选
        if (StringUtils.isEmpty(groupId)) {
            /**
             * 匹配是否有对应的圈选规则；（只匹配第一个符合的群组，同时将顾客编号和群组编号插入关系表中group_ref_member）
             */
            //查询出所有的规则，根据股则id圈选，选中的则跳出循环
            List<member_crowd_group> ruleList =memberCrowdGroupDao.query4Task();

            if (CollectionUtil.isNotEmpty(ruleList)) {
                for (member_crowd_group group : ruleList) {
                    if (customerGroupService.isCrowdGroupIncludeMemberCard(group, member.getMember_card())) {
                        groupId = group.get_id();
                        break;
                    }
                }
                //根据规则插入客户信息
                if (StringUtils.isNotEmpty(groupId)) {
                    List<MemberLabel> labels = new ArrayList<>();
                    labels.add(label);
                    customerGroupService.memberCrowdGroupRunByLabels(groupId,labels);
                }
            }
        }
        //返回顾客编号、群组编号；
        MemberGroupCodeDTO dto = new MemberGroupCodeDTO();
        dto.setMemberCard(member.getMember_card());
        dto.setMemberGroupCode(groupId);

        return ComResponse.success(dto);
    }


}
