package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.customer.dao.MemberDiseaseMapper;
import cn.net.yzl.crm.customer.dao.MemberGradeRecordDao;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.ProductConsultationMapper;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.MemberDiseaseIdUpdateVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderInfoVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.yzl.crm.customer.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberCrowdGroupDao memberCrowdGroupDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    MemberDiseaseMapper memberDiseaseMapper;


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

    @Autowired
    private ProductConsultationMapper productConsultationMapper;

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

    @Override
    public ComResponse<MemberGroupCodeDTO> coilInDealMemberData(MemberCoilInVO coilInVo) {
        return null;
    }

    @Override
    public ComResponse<Boolean> orderSignUpdateMemberData(OrderSignInfo4MqVO orderInfo4MqVo) {
        return null;
    }

    @Override
    public ComResponse<Boolean> dealWorkOrderUpdateMemberData(MemberWorkOrderInfoVO workOrderInfoVO) {
        return null;
    }

    @Override
    public ComResponse<Boolean> dealOrderCreateUpdateMemberData(OrderCreateInfoVO orderCreateInfoVO) {
        return null;
    }

    @Override
    public boolean updateMemberLabel() {
        return false;
    }


}
