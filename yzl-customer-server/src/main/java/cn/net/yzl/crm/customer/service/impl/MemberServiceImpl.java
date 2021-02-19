package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountResponse;
import cn.net.yzl.activity.model.responseModel.MemberLevelPagesResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.customer.dao.*;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.feign.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.feign.client.product.ProductFien;
import cn.net.yzl.crm.customer.feign.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.model.db.MemberGradeRecordPo;
import cn.net.yzl.crm.customer.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.model.mogo.MemberOrder;
import cn.net.yzl.crm.customer.model.mogo.MemberProduct;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.service.MemberAddressService;
import cn.net.yzl.crm.customer.service.MemberProductEffectService;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.service.impl.phone.MemberPhoneServiceImpl;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.*;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.customer.vo.order.OrderProductVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderInfoVO;
import cn.net.yzl.crm.customer.vo.work.WorkOrderBeanVO;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    ProductConsultationMapper productConsultationMapper;

    @Autowired
    MemberProductEffectService memberProductEffectService;

    @Autowired
    MemberOrderStatMapper memberOrderStatMapper;

    @Autowired
    private MemberGradeRecordDao memberGradeRecordDao;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    MemberAmountRedbagIntegralMapper memberAmountRedbagIntegralMapper;

    @Autowired
    ProductFien productFien;
    @Autowired
    OrderFien orderFien;
    @Autowired
    ActivityFien activityFien;
    @Autowired
    WorkOrderClient workOrderClient;

    @Autowired
    MemberPhoneMapper phoneMapper;

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

        //保存memberPhoneList
        List<MemberPhone> memberPhoneList = member.getMemberPhoneList();
        if (CollectionUtil.isNotEmpty(memberPhoneList)) {
            for (MemberPhone memberPhone : memberPhoneList) {
                if (StringUtils.isEmpty(memberPhone.getPhone_number())) {
                    continue;
                }
                //设置会员卡号
                memberPhone.setMember_card(member.getMember_card());
                result = phoneMapper.insert(memberPhone);
            }
        }

        //保存receiveAddressList
        List<ReveiverAddress> receiveAddressList = member.getReceive_address_list();
        ComResponse<String> addressResult = null;
        if (CollectionUtil.isNotEmpty(receiveAddressList)) {
            for (ReveiverAddress address : receiveAddressList) {
                //设置会员卡号
                address.setMember_card(member.getMember_card());
                if (StringUtils.isEmpty(address.getMember_name())){
                    address.setMember_name(member.getMember_name());
                }
                ReveiverAddressInsertVO insertVO = new ReveiverAddressInsertVO();
                insertVO.setMemberCard(address.getMember_card());
                insertVO.setMemberName(address.getMember_name());
                insertVO.setMemberMobile(address.getMember_mobile());
                insertVO.setMemberProvinceNo(address.getMember_province_no());
                insertVO.setMemberProvinceName(address.getMember_province_name());
                insertVO.setMemberCityNo(address.getMember_city_no());
                insertVO.setMemberCityName(address.getMember_city_name());
                insertVO.setMemberCountyNo(address.getMember_country_no());
                insertVO.setMemberCountyName(address.getMember_country_name());
                insertVO.setMemberStreetNo(address.getMember_street_no());
                insertVO.setMemberStreetName(address.getMember_street_name());
                insertVO.setMemberAddress(address.getMember_address());
                insertVO.setCreateCode(address.getCreate_code());
                insertVO.setUpdateCode(address.getUpdate_code());
                addressResult = memberAddressService.addReveiverAddress(insertVO);
                Integer status = addressResult.getStatus();
                if (status != 1) {
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "收货地址保存失败!");
                }
            }
        }
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
     * @param orderVo
     * @return
     */
    @Override
    public Integer updateMemberDiseaseByDiseaseId(MemberDiseaseIdUpdateVO orderVo) {
       return memberDiseaseMapper.updateMemberDiseaseByDiseaseId(orderVo);
    }

//    @Override
//    public ComResponse<Boolean> orderSignUpdateMemberData(OrderSignInfo4MqVO orderInfo4MqVo) {
//        //获取顾客的商品服用效果表 member_product_effect，更新里面的
//        MemberProductEffectSelectVO effectVo = new MemberProductEffectSelectVO();
//
//
//        ComResponse<List<MemberProductEffectDTO>> productEffectResult = memberProductEffectService.getProductEffects(effectVo);
//        List<MemberProductEffectDTO> productEffectList = productEffectResult.getData();
//        //查询历史商品服用效果
//        Map<String, MemberProductEffectDTO> dtoMap= new HashMap<>();
//        if (CollectionUtil.isNotEmpty(productEffectList)) {
//            for (MemberProductEffectDTO dto : productEffectList) {
//                dtoMap.put(dto.getProductCode(), dto);
//            }
//        }

//        //订单购买的商品
//        List<OrderProductVO> productList = orderInfo4MqVo.getProductList();
//        //之前没有购买过的商品
//        List<MemberProductEffectInsertVO> productVoList = new ArrayList<>();
//        for (OrderProductVO productVO : productList) {
//            MemberProductEffectDTO dto = dtoMap.get(productVO.getProductCode());
//            //没有则新增
//            if (dto == null) {
//                MemberProductEffectInsertVO vo = new MemberProductEffectInsertVO();
//                vo.setMemberCard();
//            }
//        }



/*

        查询客户所有的商品服用效果记录，更新
        product_name
        product_last_num(++) 商品剩余量
        due_date  商品服用完日期
        product_count 购买商品数量
        upate_time
        updator

        //更新member_order_stat
        total_counsum_amount 累计消费金额
        累计订单总金额 total_order_amount    【这个是下单的时候更新还是签收的时候更新】
        累计订单应收总金额 order_rec_amount 【这个需要更新吗】
        //最后一次下单时间 last_order_time
        //最后一次购买商品 last_buy_product_code
        order_high_am 订单最低金额
        order_low_am 订单最高金额
        order_avg_am 订单平均金额
        //product_type_cnt 购买产品种类个数
        总平均购买天数 day_avg_count (现在时间 - 首次下单时间)/订单数
        年度平均购买天数   year_avg_count
        最后一次签收时间 last_sign_time


        从DMC获取顾客级别，判断顾客是否升级；修改member里面的会员级别   ，修改 member_grade_record 会员信息


        更新member_label*/









//        return null;
//    }


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

    /**
     * 订单签收的时候更新
     * wangzhe
     * 2021-02-08
     * @param orderInfo4MqVo
     * @return
     */
    @Transactional
    @Override
    public ComResponse<Boolean> orderSignUpdateMemberData(OrderSignInfo4MqVO orderInfo4MqVo) {
        String memberCard = orderInfo4MqVo.getMemberCardNo();
        List<OrderProductVO> buyProductList = orderInfo4MqVo.getProductList();//订单购买的商品
        StringBuilder buyProductCodes = new StringBuilder();

        for (OrderProductVO productVO : buyProductList) {
            buyProductCodes.append(productVO.getProductCode()).append(",");
        }
        String codes = "";
        if (buyProductCodes.length() > 0) {
            codes = buyProductCodes.substring(0, buyProductCodes.length() - 1);
        }

        //通过商品编号，获取商品信息
        ComResponse<List<ProductMainDTO>> response = productFien.queryByProductCodes(codes.split(","));
        List<ProductMainDTO> data = response.getData();
        Map<String, ProductMainDTO> productMap = new HashMap<>();
        for (ProductMainDTO mainDTO : data) {
            productMap.put(mainDTO.getProductCode(), mainDTO);
        }

        //查询客户对应的商品服用效果
        MemberProductEffectSelectVO effectVo = new MemberProductEffectSelectVO();
        effectVo.setMemberCard(memberCard);
        Map<String, MemberProductEffectDTO> dtoMap = new HashMap<>();
        ComResponse<List<MemberProductEffectDTO>> productEffectResult = memberProductEffectService.getProductEffects(effectVo);
        List<MemberProductEffectDTO> productEffectList = productEffectResult.getData();
        if (CollectionUtil.isNotEmpty(productEffectList)) {
            for (MemberProductEffectDTO dto : productEffectList) {
                dtoMap.put(dto.getProductCode(), dto);
            }
        }

        //之前没有购买过的商品
        List<MemberProductEffectInsertVO> addProductVoList = new ArrayList<>();
        List<MemberProductEffectInsertVO> updateProductVoList = new ArrayList<>();

        for (OrderProductVO productVO : buyProductList) {
            MemberProductEffectDTO dto = dtoMap.get(productVO.getProductCode());
            //没有则新增
            MemberProductEffectInsertVO vo = new MemberProductEffectInsertVO();
            if (dto != null) {
                BeanUtil.copyProperties(dto, vo);
                updateProductVoList.add(vo);
            } else {
                vo.setMemberCard(memberCard);
                vo.setProductCode(productVO.getProductCode());
                addProductVoList.add(vo);
            }
            //获取商品的信息(主要是规格)
            ProductMainDTO ProductMainDTO = productMap.get(productVO.getProductCode());
            if (ProductMainDTO == null) {
                continue;
            }
            String totalUseNum = ProductMainDTO.getTotalUseNum();
            vo.setProductLastNum(Integer.valueOf(totalUseNum) + vo.getProductLastNum());//商品剩余量
            vo.setProductName(ProductMainDTO.getName());//商品名称
            vo.setOrderNo(orderInfo4MqVo.getOrderNo());//商品关联的最后一次签收订单编号
            vo.setProductLastNum(productVO.getProductCount());
            vo.setProductCount(vo.getProductCount() + productVO.getProductCount());//购买商品数量

            if (vo.getOneToTimes() == null) {
                vo.setOneToTimes(ProductMainDTO.getOneToTimes());
            }
            if (vo.getOneUseNum() == null) {
                vo.setOneUseNum(ProductMainDTO.getOneUseNum());
            }
        }
        //保存
        if (addProductVoList.size() > 0) {
            memberProductEffectService.batchSaveProductEffect(addProductVoList);
        }
        //更新
        if (updateProductVoList.size() > 0) {
            memberProductEffectService.batchSaveProductEffect(updateProductVoList);
        }

        //更新member_order_stat
        List<cn.net.yzl.crm.customer.model.db.MemberOrderStat> memberOrderStats = memberOrderStatMapper.queryByMemberCodes(Arrays.asList(memberCard));
        cn.net.yzl.crm.customer.model.db.MemberOrderStat memberOrderStat;
        //存在则更新最后下单时间
        if (CollectionUtil.isNotEmpty(memberOrderStats)) {
            memberOrderStat = memberOrderStats.get(0);
            //累计消费金额
            Integer totalCounsumAmount = memberOrderStat.getTotalCounsumAmount() == null ? 0 : memberOrderStat.getTotalCounsumAmount();
            totalCounsumAmount += orderInfo4MqVo.getSpend();
            memberOrderStat.setTotalCounsumAmount(totalCounsumAmount);

            //累计充值金额
            Integer totalInvestAmount = memberOrderStat.getTotalInvestAmount() == null ? 0 : memberOrderStat.getTotalInvestAmount();
            totalInvestAmount += orderInfo4MqVo.getCash1();
            memberOrderStat.setTotalInvestAmount(totalInvestAmount);

            //累计订单总金额
            Integer totalOrderAmount = memberOrderStat.getTotalOrderAmount() == null ? 0 : memberOrderStat.getTotalOrderAmount();
            totalOrderAmount += orderInfo4MqVo.getTotalAll();
            memberOrderStat.setTotalOrderAmount(totalOrderAmount);

            //累计订单应收总金额
            Integer orderRecAmount = memberOrderStat.getOrderRecAmount() == null ? 0 : memberOrderStat.getOrderRecAmount();
            orderRecAmount += orderInfo4MqVo.getCash();
            memberOrderStat.setOrderRecAmount(orderRecAmount);


            memberOrderStat.setLastBuyProductCode(codes);//最后一次购买商品
            //首次购买商品
            if (StringUtils.isEmpty(memberOrderStat.getFirstBuyProductCode())) {
                memberOrderStat.setFirstBuyProductCode(memberOrderStat.getLastBuyProductCode());
            }
            if (memberOrderStat.getFirstOrderAm() == null) {
                memberOrderStat.setFirstOrderAm(orderInfo4MqVo.getTotalAll());//首单金额
            }
            if (memberOrderStat.getOrderHighAm() == null || memberOrderStat.getOrderHighAm() < orderInfo4MqVo.getTotalAll()) {
                memberOrderStat.setOrderHighAm(orderInfo4MqVo.getTotalAll());//订单最高金额
            }

            if (memberOrderStat.getOrderLowAm() == null || memberOrderStat.getOrderLowAm() > orderInfo4MqVo.getTotalAll()) {
                memberOrderStat.setOrderLowAm(orderInfo4MqVo.getTotalAll());//订单最低金额
            }

            if (memberOrderStat.getBuyCount() == null) {
                memberOrderStat.setBuyCount(0);
            }
            memberOrderStat.setBuyCount(memberOrderStat.getBuyCount() + 1);//累计购买次数
            memberOrderStat.setOrderAvgAm(memberOrderStat.getTotalOrderAmount() / memberOrderStat.getBuyCount());//订单平均金额
            memberOrderStat.setProductTypeCnt(addProductVoList.size() + productEffectList.size());//购买产品种类个数

            //总平均购买天数
            //memberOrderStat.getLastOrderTime() - memberOrderStat.getFirstOrderTime() / memberOrderStat.getBuyCount()
            //memberOrderStat.setDayAvgCount();
            //memberOrderStat.setYearAvgCount();//年度平均购买天数 TODO 暂时不处理
            memberOrderStatMapper.updateByPrimaryKeySelective(memberOrderStat);


            //从DMC获取顾客级别，判断顾客是否升级；修改member里面的会员级别   ，修改 member_grade_record 会员信息
            //TODO 从DMC获取顾客级别定义
            //todo 从订单中心获取本年度的累计消费金额与本次消费金额、本次预存金额
            List<MemberGradeRecordDto> recordList = memberGradeRecordDao.getMemberGradeRecordList(memberCard);
            MemberGradeRecordPo vo = new MemberGradeRecordPo();
            if (CollectionUtil.isNotEmpty(recordList)) {
                MemberGradeRecordDto dto = recordList.get(0);
                BeanUtil.copyProperties(dto, vo);
                //从DMC获取顾客级别 ->判断是否可以升级
                boolean upLevel = false;
                //更新会员级别
                if (upLevel) {
                    vo.setBeforeGradeId(vo.getMGradeId());
                    vo.setBeforeGradeName(vo.getMGradeName());
                    //vo.setMGradeId();
                    //vo.setMGradeName();
                    memberGradeRecordDao.updateByPrimaryKeySelective(vo);
                }
            }
            //设置reids缓存
            Date currentDateStart = cn.net.yzl.crm.customer.utils.date.DateUtil.getCurrentDateStart();
            String version = DateUtil.format(currentDateStart, "yyyyMMdd");


            redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(version),memberCard);
        }
        return ComResponse.success(true);
    }

    /**
     * 处理工单时更新
     * @param workOrderInfoVO
     * @return
     */
    @Override
    public ComResponse<Boolean> dealWorkOrderUpdateMemberData(MemberWorkOrderInfoVO workOrderInfoVO) {
        //设置reids缓存
        Date currentDateStart = cn.net.yzl.crm.customer.utils.date.DateUtil.getCurrentDateStart();
        String version = DateUtil.format(currentDateStart, "yyyyMMdd");
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(version),workOrderInfoVO.getMemberCard());
        return ComResponse.success(true);
    }

    /**
     * 下单时，
     *  更新最后下单时间
     * @param orderCreateInfoVO
     * @return
     */
    @Override
    public ComResponse<Boolean> dealOrderCreateUpdateMemberData(OrderCreateInfoVO orderCreateInfoVO) {
        String memberCard = orderCreateInfoVO.getMemberCard();
        memberMapper.updateLastOrderTime(memberCard,orderCreateInfoVO.getCreateTime());
        //member_order_stat
        List<cn.net.yzl.crm.customer.model.db.MemberOrderStat> memberOrderStats = memberOrderStatMapper.queryByMemberCodes(Arrays.asList(memberCard));
        cn.net.yzl.crm.customer.model.db.MemberOrderStat memberOrderStat;
        //存在则更新最后下单时间
        if (CollectionUtil.isNotEmpty(memberOrderStats)) {
            memberOrderStat = memberOrderStats.get(0);
            memberOrderStat.setLastOrderTime(orderCreateInfoVO.getCreateTime());//最后一次下单时间
            memberOrderStatMapper.updateByPrimaryKeySelective(memberOrderStat);
        }
        //不存在则添加记录
        else {
            memberOrderStat = new cn.net.yzl.crm.customer.model.db.MemberOrderStat();
            memberOrderStat.setMemberCard(memberCard);//会员卡号
            memberOrderStat.setFirstOrderTime(orderCreateInfoVO.getCreateTime());//首单下单时间
            memberOrderStat.setLastOrderTime(orderCreateInfoVO.getCreateTime());//最后一次下单时间
            memberOrderStat.setFirstOrderStaffNo(orderCreateInfoVO.getStaffNo());//首单下单员工
            memberOrderStat.setFirstOrderNo(orderCreateInfoVO.getOrderNo());//首单订单编号
            memberOrderStatMapper.insertSelective(memberOrderStat);
        }
        //设置reids缓存
        Date currentDateStart = cn.net.yzl.crm.customer.utils.date.DateUtil.getCurrentDateStart();
        String version = DateUtil.format(currentDateStart, "yyyyMMdd");
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(version),memberCard);
        return ComResponse.success(true);
    }


    /**
     * 保存工单的时候
     * @param memberHangUpVO
     * @return
     */
//    @Override
//    public ComResponse<Boolean> hangUpUpdateMemberData(MemberHangUpVO memberHangUpVO) {
//        //设置reids缓存
//        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),memberHangUpVO.getMemberCard());
//
//        return ComResponse.success(true);
//    }

    /**
     * 同步member_label
     * wangzhe
     * 2021-02-18
     * @return
     */
    @Override
    public boolean updateMemberLabel() {
        //获取redis缓存
        Date yesterdayStart = cn.net.yzl.crm.customer.utils.date.DateUtil.getYesterdayStart();
        String version = DateUtil.format(yesterdayStart, "yyyyMMdd");
        String key = CacheKeyUtil.syncMemberLabelCacheKey(version);

        Set<Object> memberSet = redisUtil.sGet(key);
        if (CollectionUtil.isEmpty(memberSet)) {
            log.info("CacheKeyUtil.syncMemberLabelCacheKey() - 数据同步：当前没有需要同步的数据");
            return true;
        }

        Map<String, Integer> activityMap = new HashMap<>();

        List<String> memberCodes = new ArrayList<>();
        for (Object m : memberSet) {
            String memberCard = m.toString();
            memberCodes.add(memberCard);
        }
        //查询MySql数据库中客户的相关信息
        List<MemberLabel> list = memberMapper.queryMemberLabelByCodes(memberCodes);
        if (CollectionUtils.isEmpty(list)) {
            log.error("没有查询到顾客信息");
            return false;
        }

        List<cn.net.yzl.crm.customer.model.mogo.MemberDisease> memberDiseaseList = memberDiseaseMapper.queryByMemberCodes(memberCodes);
        Map<String, List<cn.net.yzl.crm.customer.model.mogo.MemberDisease>> memberDiseaseListMap = memberDiseaseList.stream()
                .collect(Collectors.groupingBy(cn.net.yzl.crm.customer.model.mogo.MemberDisease::getMemberCard));

        //查询综合行为( from member_action_relation where member_card in)
        List<ActionDict> actionDictList = memberMapper.queryActionByMemberCodes(memberCodes);
        Map<String, List<ActionDict>> actionDictListMap = actionDictList.stream()
                .collect(Collectors.groupingBy(ActionDict::getMemberCard));

        //通过会员卡号查询顾客服用效果( from member_product_effect where member_card in)
        List<MemberProduct> memberProducts=memberMapper.queryProductByMemberCodes(memberCodes);
        Map<String, List<MemberProduct>> memberProductsMap = memberProducts.stream()
                .collect(Collectors.groupingBy(MemberProduct::getMemberCard));


        ComResponse<List<MemberOrderObject>> querymemberorder = null;
        Map<String, List<MemberOrder>> memberRefOrderMap = new HashMap<>();
        try {
            querymemberorder = orderFien.querymemberorder(memberCodes);
            //获取会员订单信息
            List<MemberOrderObject> data = querymemberorder.getData();
            List<MemberOrder> memberRefOrders = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(data)) {
                MemberOrderObject memberOrder1 = data.get(0);
                List<MemberOrderDTO> orders = memberOrder1.getOrders();
                for (MemberOrderDTO order : orders) {
                    MemberOrder memberOrder = new MemberOrder();
                    memberOrder.setMemberCard(memberOrder1.getMemberCardNo());
                    if (order.getActivityNo() != null) {
                        memberOrder.setActivityCode(String.valueOf(order.getActivityNo()));
                    }
                    memberOrder.setOrderCode(order.getOrderNo());
                    memberOrder.setLogisticsStatus(order.getLogisticsStatus());
                    memberOrder.setCompanyCode(order.getExpressCompanyCode());
                    memberOrder.setStatus(order.getOrderStatus());
                    if (order.getPayType() != null) {
                        memberOrder.setPayType(Integer.valueOf(order.getPayType()));
                    }
                    memberOrder.setPayMode(order.getPayMode());
                    memberOrder.setSource(String.valueOf(order.getMediaNo()));
                    if (order.getPayStatus() !=null){
                        memberOrder.setPayStatus(Integer.valueOf(order.getPayStatus()));
                    }
                    memberRefOrders.add(memberOrder);
                }
                memberRefOrderMap = memberRefOrders.stream()
                        .collect(Collectors.groupingBy(MemberOrder::getMemberCard));
            }
        } catch (Exception e) {
            log.error("获取会员订单信息失败！");
        }

        //封装标签数据
        for (MemberLabel memberLabel : list) {
            //是否有QQ
            if (org.springframework.util.StringUtils.hasText(memberLabel.getQq())) {
                memberLabel.setHasQQ(true);
            } else {
                memberLabel.setHasQQ(false);
            }
            //是否有邮箱
            if (org.springframework.util.StringUtils.hasText(memberLabel.getEmail())) {
                memberLabel.setHasEmail(true);
            } else {
                memberLabel.setHasEmail(false);
            }
            //是否有微信
            if (org.springframework.util.StringUtils.hasText(memberLabel.getWechat())) {
                memberLabel.setHasWechat(true);
            } else {
                memberLabel.setHasWechat(false);
            }
            //生日月份
            if (org.springframework.util.StringUtils.hasText(memberLabel.getBirthday())) {
                memberLabel.setMemberMonth(getMonth(memberLabel.getBirthday()));
            }
            //是否有余额
            if(memberLabel.getTotalMoney()>0){
                memberLabel.setHasMoney(true);
            }else{
                memberLabel.setHasMoney(false);
            }
            //处理创建时间，修改时间，首次下单时间，最后一个下单时间
            if(Objects.nonNull(memberLabel.getCreateTime())){
                memberLabel.setCreateTime(MongoDateHelper.getMongoDate(memberLabel.getCreateTime()));
            }
            if(Objects.nonNull(memberLabel.getLastSignTime())){
                memberLabel.setLastSignTime(MongoDateHelper.getMongoDate(memberLabel.getLastSignTime()));
            }
            if(Objects.nonNull(memberLabel.getUpdateTime())){
                memberLabel.setUpdateTime(MongoDateHelper.getMongoDate(memberLabel.getUpdateTime()));
            }
            if(Objects.nonNull(memberLabel.getFirstOrderTime())){
                memberLabel.setFirstOrderTime(MongoDateHelper.getMongoDate(memberLabel.getFirstOrderTime()));

            }

            //首次购买商品
            if(org.springframework.util.StringUtils.hasText(memberLabel.getFirstBuyProductCod())){
                Set<String> set = org.springframework.util.StringUtils.commaDelimitedListToSet(memberLabel.getFirstBuyProductCod());
                memberLabel.setFirstBuyProductCodes(new ArrayList<>(set));
            }

            //最后一次购买商品
            if(org.springframework.util.StringUtils.hasText(memberLabel.getLastBuyProductCode())){
                Set<String> set = org.springframework.util.StringUtils.commaDelimitedListToSet(memberLabel.getLastBuyProductCode());
                memberLabel.setLastBuyProductCodes(new ArrayList<>(set));
            }

            String memberCard = memberLabel.getMemberCard();
            //设置会员卡号
            memberLabel.set_id(memberCard);

            //获取对应会员卡号的顾客的服用效果下信息
            List<MemberProduct> products = memberProductsMap.get(memberCard);
            //设置顾客服用效果
            if(!CollectionUtils.isEmpty(products)){
                memberLabel.setMemberProductList(products);
            }

            //获取顾客的红包 积分 优惠券记录
            MemberAmountRedbagIntegral memberAmountRedbagIntegral = memberAmountRedbagIntegralMapper.selectByMemberCard(memberCard);
            if (memberAmountRedbagIntegral == null) {
                memberAmountRedbagIntegral.setMemberCard(memberCard);
            }


            //是否有积分、红包、优惠券要从DMC获取
            ComResponse<MemberAccountResponse> dmcResponse = activityFien.getAccountByMemberCard(memberCard);
            MemberAccountResponse dmcData = dmcResponse.getData();
            //积分
            if (dmcData.getMemberIntegral() != null && dmcData.getMemberIntegral() > 0) {
                memberLabel.setHasIntegral(true);
                memberAmountRedbagIntegral.setLastIntegral(dmcData.getMemberIntegral());
            }else{
                memberLabel.setHasIntegral(false);
                memberAmountRedbagIntegral.setLastIntegral(0);
            }

            //红包
            if (dmcData.getMemberRedBag() != null && dmcData.getMemberRedBag() > 0) {
                memberLabel.setHasTedBag(true);
                memberAmountRedbagIntegral.setLastRedBag(dmcData.getMemberRedBag().intValue());
            }else{
                memberLabel.setHasTedBag(false);
                memberAmountRedbagIntegral.setLastRedBag(0);
            }

            //优惠券
            if (dmcData.getMemberCouponSize() != null && dmcData.getMemberCouponSize() > 0) {
                memberLabel.setHasIntegral(true);
            }else{
                memberLabel.setHasIntegral(false);
            }

            //新增
            if (memberAmountRedbagIntegral.getId() == null) {
                memberAmountRedbagIntegralMapper.insertSelective(memberAmountRedbagIntegral);
            }else{
                //更新
                memberAmountRedbagIntegralMapper.updateByPrimaryKeySelective(memberAmountRedbagIntegral);
            }


            //TODO 怎么获取 一次性预存款 一次性消费满多少 一年累计消费满
            //获取会员信息
            //Member member1 = selectMemberByCard(memberCard);
            //获取会员等级，判断是否升级
            PageParam pageParam = new PageParam();
            pageParam.setPageNo(1);
            pageParam.setPageSize(20);
            ComResponse<Page<MemberLevelPagesResponse>> dmcLevelResponse = activityFien.getMemberLevelPages(pageParam);
            Page<MemberLevelPagesResponse> data = dmcLevelResponse.getData();
            if (dmcLevelResponse != null && data != null && CollectionUtil.isNotEmpty(data.getItems())) {
                List<MemberLevelPagesResponse> dmcLevelData = data.getItems();
                //等级倒叙排序
                Collections.sort(dmcLevelData, new Comparator<MemberLevelPagesResponse>()
                {
                    public int compare(MemberLevelPagesResponse o1, MemberLevelPagesResponse o2)
                    {
                        return o2.getMemberLevelGrade() - o1.getMemberLevelGrade();
                    }
                });

                ComResponse<List<MemberTotal>> memberTotalResponse = orderFien.queryMemberTotal(memberCodes);
                List<MemberTotal> memberTotalData = memberTotalResponse.getData();
                if (CollectionUtil.isNotEmpty(memberTotalData)) {
                    MemberTotal memberTotal = memberTotalData.get(0);//因为接口支持多个会员卡号，这里只用了一个卡号
                    BigDecimal totalSpend = memberTotal.getTotalSpend() == null ? BigDecimal.ZERO : memberTotal.getTotalSpend();//累计消费
                    BigDecimal maxSpend = memberTotal.getMaxSpend() == null ? BigDecimal.ZERO : memberTotal.getMaxSpend();//最高消费
                    BigDecimal maxCash1 = memberTotal.getMaxCash1() == null ? BigDecimal.ZERO : memberTotal.getMaxCash1();//最高预存

                    MemberLevelPagesResponse level = null;
                    //遍历DMC会员级别信息，判断顾客当前属于那个级别
                    for (MemberLevelPagesResponse levelData : dmcLevelData) {
                        if (totalSpend.compareTo(new BigDecimal(String.valueOf(levelData.getYearTotalSpendMoney()))) >= 0) {//一年累计消费满
                            level = levelData;
                            break;
                        } else if (maxSpend.compareTo(new BigDecimal(String.valueOf(levelData.getDisposableSpendMoney()))) >= 0) {//一次性消费满多少
                            level = levelData;
                            break;
                        } else if (maxCash1.compareTo(new BigDecimal(String.valueOf(levelData.getDisposableAdvanceMoney()))) >= 0) {//一次性预存款
                            level = levelData;
                            break;
                        }
                        if (level != null) {
                            //当前顾客的会员级别信息
                            MemberGradeRecordPo memberLevel = new MemberGradeRecordPo();
                            //查询顾客当前的会员级别信息
                            List<MemberGradeRecordDto> memberGradeRecordList = memberGradeRecordDao.getMemberGradeRecordList(memberCard);
                            if (CollectionUtil.isNotEmpty(memberGradeRecordList)) {
                                MemberGradeRecordDto oldLevel = memberGradeRecordList.get(0);
                                memberLevel.setBeforeGradeId(oldLevel.getMGradeId());
                                memberLevel.setBeforeGradeName(oldLevel.getMGradeName());
                                memberLevel.setId(oldLevel.getId());
                            } else {
                                //新建会员等级信息
                                memberLevel = new MemberGradeRecordPo();
                                memberLevel.setCreateTime(new Date());
                            }
                            memberLevel.setMGradeId(level.getMemberLevelGrade());
                            memberLevel.setMGradeName(level.getMemberLevelName());
                            if (memberLevel.getId() == null) {
                                memberGradeRecordDao.insertSelective(memberLevel);
                            } else {
                                memberGradeRecordDao.updateByPrimaryKeySelective(memberLevel);
                            }
                            //查询顾客表信息
                            Member member = memberMapper.selectMemberByCard(memberCard);
                            member.setMGradeId(memberLevel.getMGradeId());
                            member.setMGradeName(memberLevel.getMGradeName());
                            memberMapper.updateByMemberCardSelective(member);
                        }
                    }
                }
            }

            //获取当前顾客的综合行为
            List<ActionDict> actionDicts =actionDictListMap.get(memberCard);
            //设置当前顾客的综合行为
            if(!CollectionUtils.isEmpty(actionDicts)){
                Map<Integer,List<ActionDict>> temp=actionDicts.stream().filter(s->s.getType()!=null).collect(Collectors.groupingBy(ActionDict::getType));
                //方便接电话时间
                if(!CollectionUtils.isEmpty(temp.get(1))){
                    memberLabel.setPhoneDictList(temp.get(1));
                }
                //2性格偏好
                if(!CollectionUtils.isEmpty(temp.get(2))){
                    memberLabel.setMemberCharacterList(temp.get(2));
                }
                //3响应时间
                if(!CollectionUtils.isEmpty(temp.get(3))){
                    memberLabel.setMemberResponseTimeList(temp.get(3));
                }
                //综合行为
                if(!CollectionUtils.isEmpty(temp.get(5))){
                    memberLabel.setComprehensiveBehaviorList(temp.get(5));
                }
                //下单行为
                if(!CollectionUtils.isEmpty(temp.get(6))){
                    memberLabel.setOrderBehaviorList(temp.get(6));
                }
                //活动偏好
                if(!CollectionUtils.isEmpty(temp.get(7))){
                    memberLabel.setActivityBehaviorList(temp.get(7));
                }
            }
            List<cn.net.yzl.crm.customer.model.mogo.MemberDisease> diseaseList = memberDiseaseListMap.get(memberCard);
            if(CollectionUtils.isEmpty(diseaseList)){
                memberLabel.setMemberDiseaseList(diseaseList);
            }
            //处理订单
            List<MemberOrder> mros = memberRefOrderMap.get(memberCard);
            List<Integer> BusNos = new ArrayList<>();
            if(!CollectionUtils.isEmpty(mros)){
                for(MemberOrder memberOrder:mros){
                    if (StringUtils.isEmpty(memberOrder.getActivityCode())) {
                        continue;
                    }
                    Integer activityType = activityMap.get(memberOrder.getActivityCode());
                    if (activityType == null) {
                        //要从DMC获取活动类型
                        ComResponse<List<ActivityDetailResponse>> listByBusNos = activityFien.getListByBusNos(Arrays.asList(Integer.valueOf(memberOrder.getActivityCode())));
                        List<ActivityDetailResponse> data2 = listByBusNos.getData();
                        if (data2 == null) {
                            continue;
                        }
                        ActivityDetailResponse activity = data2.get(0);
                        Integer type = activity.getActivityType();
                        activityMap.put(memberOrder.getActivityCode(), type);
                    }else{
                        memberOrder.setActivityType(String.valueOf(activityType));
                    }
                    if(memberOrder.getActivityCode()!=null){
                        memberOrder.setActivityFlag(true);
                    }else{
                        memberOrder.setActivityFlag(false);
                    }
                }
                memberLabel.setMemberOrders(mros);
                memberLabel.setHaveOrder(1);
            }else{
                memberLabel.setHaveOrder(0);
            }
            //顾客最后一次进线，最后一次通话
            ComResponse<MemberLastCallInDTO> lastCallManage = workOrderClient.getLastCallManageByMemberCard(memberCard);
            if (200 != lastCallManage.getCode()){
                log.error("获取顾客最后进线、通过时间异常");
            }else{
                MemberLastCallInDTO lastCallData = lastCallManage.getData();
                //最后一次进线时间
                String lastCallInTime = lastCallData.getLastCallInTime();
                //最后一次拨打时间
                String lastDialTime = lastCallData.getLastDialTime();

                //最后一次拨打时间
                if(StringUtils.isNotEmpty(lastDialTime)){
                    memberLabel.setLastCallTime(MongoDateHelper.getMongoDate(DateUtil.parse(lastDialTime)));
                }
                //设置最后一次进线时间
                if(StringUtils.isNotEmpty(lastCallInTime)){
                    memberLabel.setLastCallInTime(MongoDateHelper.getMongoDate(DateUtil.parse(lastCallInTime)));
                }
            }
            //保存到mongo数据库
            memberLabelDao.save(memberLabel);
        }
        list.clear();
        //清除redis缓存
        redisUtil.del(key);

        log.info("数据同步完成");
        return true;
    }

    @Transactional
    @Override
    public int saveMemberReferral(MemberAndAddWorkOrderVO memberReferralVO) {
        Member memberVO = memberReferralVO.getMemberVO();
        WorkOrderBeanVO workOrderBeanVO = memberReferralVO.getWorkOrderBeanVO();
        //保存用户信息
        int result = this.insert(memberVO);
        if (result > 0) {
            Date now = new Date();
            //保存工单
            String memberCard = memberVO.getMember_card();
            workOrderBeanVO.setMemberCard(memberCard);
            workOrderBeanVO.setAcceptStatus(2);//工单接收状态：已接收
            //workOrderBeanVO.setActivity(3);
            workOrderBeanVO.setAllocateTime(now);//分配时间
            workOrderBeanVO.setApplyUpStatus(0);//上交状态：未上交
            workOrderBeanVO.setCallFlag(0);//员工当日拨打状态：未拨打
            workOrderBeanVO.setCallTimes(0);//坐席已拨打次数：0次
            List<MemberPhone> memberPhoneList = memberVO.getMemberPhoneList();
            if (CollectionUtil.isNotEmpty(memberPhoneList)) {
                workOrderBeanVO.setCalledPhone(memberPhoneList.get(0).getPhone_number());//被叫号码
            }
            //workOrderBeanVO.setCallerPhone("400-");//主叫号码
            if (workOrderBeanVO.getCreateTime() == null) {
                workOrderBeanVO.setCreateTime(now);//创建时间
            }


            workOrderBeanVO.setHistoryFlag(0);//非历史数据
            workOrderBeanVO.setIsVisiable(1);//可见
            workOrderBeanVO.setIsWorkOrder(0);//不是建档工单
            if (StringUtils.isEmpty(workOrderBeanVO.getMemberName())) {
                workOrderBeanVO.setMemberName(memberVO.getMember_name());//会员名称
            }
            workOrderBeanVO.setMGradeCode("1");
            workOrderBeanVO.setSouce(3);//工单来源：自有的
            workOrderBeanVO.setStatus(1);//工单处理状态：未处理
            workOrderBeanVO.setTradeStatus(2);//工单成交状态：未成交
            workOrderBeanVO.setTransTimes(0);//调整次数：0
            if (workOrderBeanVO.getUpdateTime() == null) {
                workOrderBeanVO.setUpdateTime(now);//修改时间
            }
            workOrderBeanVO.setVisitType(2);//工单类别：常规回访
            workOrderBeanVO.setWorkOrderMoney(new BigDecimal("0.00"));
            workOrderBeanVO.setWorkOrderType(1);//工单类型：热线工单
            workOrderClient.addWorkOrder(workOrderBeanVO);
        }
        return result;
    }

    private static Integer getMonth(String date) {
        String[] str = date.split("-");
        if(str.length>2){
            return Integer.parseInt(str[1]);
        }
        return 0;
        }


}
