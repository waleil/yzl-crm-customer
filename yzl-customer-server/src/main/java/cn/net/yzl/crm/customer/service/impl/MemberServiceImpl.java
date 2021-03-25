package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountResponse;
import cn.net.yzl.activity.model.responseModel.MemberLevelPagesResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.customer.dao.*;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.feign.api.ActivityClientAPI;
import cn.net.yzl.crm.customer.feign.api.OrderClientAPI;
import cn.net.yzl.crm.customer.feign.api.ProductClientAPI;
import cn.net.yzl.crm.customer.feign.api.WorkOrderClientAPI;
import cn.net.yzl.crm.customer.feign.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.feign.client.product.ProductFien;
import cn.net.yzl.crm.customer.feign.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.customer.feign.model.MemberGradeValidDate;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.model.db.MemberGradeRecordPo;
import cn.net.yzl.crm.customer.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.model.mogo.MemberOrder;
import cn.net.yzl.crm.customer.model.mogo.MemberProduct;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.service.*;
import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.service.impl.phone.MemberPhoneServiceImpl;
import cn.net.yzl.crm.customer.service.memberDict.MemberActionRelationService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.CentYuanConvertUtil;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import cn.net.yzl.crm.customer.utils.date.DealDateUtil;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.*;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.crm.customer.vo.member.MemberGrandSelectVo;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.customer.vo.order.OrderProductVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderDiseaseVo;
import cn.net.yzl.crm.customer.vo.work.MemeberWorkOrderSubmitVo;
import cn.net.yzl.crm.customer.vo.work.WorkOrderBeanVO;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    MemberOrderStatService memberOrderStatService;

//    @Autowired
//    ProductFien productFien;
//    @Autowired
//    OrderFien orderFien;
//    @Autowired
//    ActivityFien activityFien;
    @Autowired
    WorkOrderClient workOrderClient;

    @Autowired
    MemberPhoneMapper phoneMapper;

    @Autowired
    MemberActionRelationService memberActionRelationService;

    @Autowired
    MemberAmountService memberAmountService;

    @Autowired
    private RabbitTemplate template;

    private String memberCountkey="memeberCount";

    /**
     * 新增顾客信息 同时 保存memberPhone 和顾客收货地址
     * wangzhe
     * 2021-01-25
     * @param member
     * @return
     */
    @Override
    @Transactional
    public int insert(Member member) {
        List<MemberPhone> memberPhoneList = member.getMemberPhoneList();
        //真正要保存的数据(里面的电话号不为空)
        List<MemberPhone> savePhoneList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(memberPhoneList)) {
            for (MemberPhone memberPhone : memberPhoneList) {
                if (StringUtils.isEmpty(memberPhone.getPhone_number())) {
                    continue;
                }
                savePhoneList.add(memberPhone);
                //判断当前号码是否已经使用
                ComResponse<Member> response = memberPhoneService.getMemberByphoneNumber(memberPhone.getPhone_number());
                if (response.getCode() != 200 || response.getData() != null) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "电话号码:" + memberPhone.getPhone_number() + "已经被使用!");
                }
            }
        }

        //生成顾客会员卡号缓存的key
        String cacheKey = CacheKeyUtil.maxMemberCardCacheKey();
        //生成顾客会员卡号
        long maxMemberCard = redisUtil.incr(cacheKey, 1);
        //设置顾客会员卡号
        member.setMember_card(String.valueOf(maxMemberCard));
        member.setMGradeId(1);
        member.setMGradeName("无卡");
        member.setM_grade_code(null);
        //设置媒体类型信息
        if (member.getSource() != null) {
            member.setMedia_type_code(member.getSource());
            member.setMedia_type_name(convertMediaTypeCode2Name(member.getSource()));
        }

        //保存数据
        int result = memberMapper.insertSelective(member);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "保存会员电话信息失败!");
        }
        Date now = new Date();
        String phoneNumber = "",haveZeroNumber="",noZeroNumber = "";
        //保存memberPhoneList
        for (MemberPhone memberPhone : savePhoneList) {
            if (StringUtils.isEmpty(memberPhone.getPhone_number())) {
                continue;
            }
            //设置会员卡号
            memberPhone.setMember_card(member.getMember_card());
            memberPhone.setCreate_time(now);
            memberPhone.setUpdate_time(now);
            phoneNumber = memberPhone.getPhone_number();
            //是否以0开头 --> 去掉0
            if (phoneNumber.startsWith("0")){
                noZeroNumber = phoneNumber.substring(1);
                haveZeroNumber = phoneNumber;
            }else{
                noZeroNumber = phoneNumber;
                haveZeroNumber = "0" + phoneNumber;
            }
            //校验手机号
            int phoneType = 0;
            if (memberPhoneService.isMobile(noZeroNumber)) {
                phoneType = 1;
            }
            //不是手机号时，要校验是否为电话号
            if (phoneType == 0 && memberPhoneService.isPhone(phoneNumber)){
                phoneType = 2;
            }
            if (phoneType == 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "电话号码:"+memberPhone.getPhone_number()+"格式不正确!");
            }
            //获取入参的点火类型
            Integer phone_type = memberPhone.getPhone_type();
            if (phone_type != null && phoneType != phone_type.intValue()) {
                if (phone_type.intValue() == 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "手机号码:"+memberPhone.getPhone_number()+"格式不正确!");
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "电话号码:"+memberPhone.getPhone_number()+"格式不正确!");
                }
            }

            memberPhone.setPhone_type(phoneType);
            if (memberPhone.getEnabled() == null) {
                memberPhone.setEnabled(1);//默认可用
            }

            result = phoneMapper.insert(memberPhone);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "保存会员电话号码"+memberPhone.getPhone_number()+"失败!");
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
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "收货地址保存失败!");
                }
                //将顾客信息保存到member_label(mongo)
                //boolean ret = syncMemberLabel(Arrays.asList(member.getMember_card()), 2, null);

                //设置缓存(2小时后同步)
                redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),member.getMember_card());
            }
        }
        return result;
    }

    @Override
    public ComResponse<Page<Member>> findPageByCondition(MemberSerchConditionDTO dto)  {
        //Page<Member> page = AssemblerResultUtil.resultAssembler(memberList);

        CompletableFuture<Integer> cfCount=CompletableFuture.supplyAsync(()->this.memberMapper.findCountByCondition(dto));
        CompletableFuture<List<Member>> cfList=CompletableFuture.supplyAsync(()->this.memberMapper.findPageByCondition(dto));
        CompletableFuture.allOf(cfCount,cfList);
        List<Member> memberList =Collections.emptyList();
        try {
             memberList = cfList.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getLocalizedMessage(),e);
        }
        Integer totalCount =0;
        try {
             totalCount = cfCount.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getLocalizedMessage(),e);
        }

        return ComResponse.success(dto.toPage(memberList,totalCount));
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
        Member member = memberMapper.selectMemberByCard(memberCard);
        if (member != null) {
            if (member.getAge() != null && member.getAge() == 0) {
                member.setAge(null);
            }
        }
        return member;
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
        List<MemberDiseaseCustomerDto> memberDiseaseCustomerDtoList = memberMapper.getMemberDiseaseDtoByMemberCard(memberCard);
        if(CollectionUtils.isEmpty(memberDiseaseCustomerDtoList)){
            return ComResponse.nodata();
        }
        return ComResponse.success(memberDiseaseCustomerDtoList);
    }

//    @Override
//    public void saveReveiverAddress(ReveiverAddress reveiverAddress) {
//        memberMapper.saveReveiverAddress(reveiverAddress);
//    }

//    @Override
//    public void updateReveiverAddress(ReveiverAddress reveiverAddress) {
//        memberMapper.updateReveiverAddress(reveiverAddress);
//    }

    @Override
    public List<ReveiverAddress> getReveiverAddress(String member_card) {
        return memberMapper.getReveiverAddress(member_card);
    }

    @Override
    public MemberOrderStat getMemberOrderStat(String member_card) {
        MemberOrderStat stat = memberMapper.getMemberOrderStat(member_card);
        if (stat != null) {
            stat.setTotalCounsumAmountD(CentYuanConvertUtil.cent2Yuan(stat.getTotal_counsum_amount()));//累计消费金额(元)
            stat.setTotalInvestAmountD(CentYuanConvertUtil.cent2Yuan(stat.getTotal_invest_amount()));//累计充值金额(元)
            stat.setFirstOrderAmD(CentYuanConvertUtil.cent2Yuan(stat.getFirst_order_am()));//首单金额(元)
            stat.setOrderHighAmD(CentYuanConvertUtil.cent2Yuan(stat.getOrder_high_am()));//订单最高金额(元)
            stat.setOrderLowAmD(CentYuanConvertUtil.cent2Yuan(stat.getOrder_low_am()));//订单最低金额(元)")
            stat.setOrderAvgAmD(CentYuanConvertUtil.cent2Yuan(stat.getOrder_avg_am()));//订单平均金额(元)
            stat.setReturnGoodsRateD(CentYuanConvertUtil.cent2Yuan(stat.getReturn_goods_rate()));//退货率
        }

        return stat;
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
/*    @Override
    public List<MemberBaseAttr> getmemberActions() {
        return memberMapper.getmemberActions();
    }*/

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
    public List<MemberAmount> getMemberAmount(List<String> member_cards) {
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
        //为空的时候不报错
        if (CollectionUtil.isEmpty(productConsultationInsertVOList)) {
            return ComResponse.success();
        }
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

    /**
     * wangzhe
     * 2021-02-26
     * @param productConsultationInsertVOList
     * @return
     */
    @Override
    @Transactional
    public ComResponse<String> batchSaveProductConsultation(List<ProductConsultationInsertVO> productConsultationInsertVOList) {
        //为空的时候不报错
        if (CollectionUtil.isEmpty(productConsultationInsertVOList)) {
            return ComResponse.success();
        }
        for (ProductConsultationInsertVO productConsultationInsertVO : productConsultationInsertVOList) {
            cn.net.yzl.crm.customer.model.db.ProductConsultation productConsultation = new cn.net.yzl.crm.customer.model.db.ProductConsultation();
            BeanUtil.copyProperties(productConsultationInsertVO,productConsultation);
        }

        //批量插入
        int num =  productConsultationMapper.batchInsert(productConsultationInsertVOList);
        if(num< 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE);
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

    @Override
    public ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordListByTimeRange(MemberGrandSelectVo vo) {

        List<MemberGradeRecordDto> list =  memberGradeRecordDao.getMemberGradeRecordListByTimeRange(vo.getMemberCard(),vo.getStartDate(),vo.getEndDate());

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
        String callerPhone = coilInVo.getCallerPhone();
        if (StringUtils.isEmpty(callerPhone)) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "电话号码不能为空!");
        }
        //判断当前号码是否已经使用
        ComResponse<Member> response = memberPhoneService.getMemberByphoneNumber(coilInVo.getCallerPhone());
        if (response.getCode() != 200) {
            String msg = response.getMessage();
            if (StringUtils.isEmpty(msg)) {
                msg = "保存会员信息失败!";
            }
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), msg);
        }
        Member member = response.getData();
        String memberCard = "";//会员卡号
        String groupId = "";

        //会员不存在 ->新建客户信息
        if (member == null) {
            //将顾客来电号码区分(座机/手机)插入member_phone表
            ComResponse<String> addMemberResult = memberPhoneService.getMemberCardByphoneNumber(coilInVo.getCallerPhone());
            if (addMemberResult.getCode() != 200) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(addMemberResult.getCode(), addMemberResult.getMessage());
            }
            //新添加的会员卡号
            memberCard = addMemberResult.getData();
            //通过会员卡号查询会员信息
            member = selectMemberByCard(memberCard);
            //更新会员级别
            member.setMGradeId(1);
            member.setMGradeName("无卡");
            member.setM_grade_code(null);
            member.setMedia_id(coilInVo.getMediaId());
            member.setMedia_name(coilInVo.getMediaName());
            member.setAdver_code(coilInVo.getAdvId());
            member.setAdver_name(coilInVo.getAdvName());
            member.setSource(coilInVo.getMediaType());//获客来源
            member.setMedia_type_code(coilInVo.getMediaType());
            member.setMedia_type_name(convertMediaTypeCode2Name(coilInVo.getMediaType()));

            //更新会员信息
            int update = updateByMemberCardSelective(member);
            if (update < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "保存会员信息失败!");
            }
        }
        //会员已经存在的情景 ->判断顾客是否已经被圈选
        else{
            memberCard = member.getMember_card();//会员卡号
            groupId = customerGroupService.queryGroupIdByMemberCard(memberCard);
        }
        //没有圈选的客户进行圈选
        if (StringUtils.isEmpty(groupId)) {
            //查询顾客有没有memeber_label
            List<String> memberCards = Arrays.asList(memberCard);
            Map<String,String> mongoMemberLabels = memberLabelDao.queryByCodes(memberCards);
            //没有member_label的顾客要新建
            if (CollectionUtil.isEmpty(mongoMemberLabels)) {
                //根据顾客基本资料，新建顾客标签(type=2.只同步顾客的基本资料(本服务数据))
                boolean result = syncMemberLabel(memberCards, 2, null);
                if (!result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "保存顾客标签信息失败!");
                }
            }

            /**
             * 匹配是否有对应的圈选规则；（只匹配第一个符合的群组，同时将顾客编号和群组编号插入关系表中group_ref_member）
             */
            //查询出生效的规则，根据规则id圈选，选中的则跳出循环
            List<member_crowd_group> ruleList =memberCrowdGroupDao.query4Task();

            if (CollectionUtil.isNotEmpty(ruleList)) {
                for (member_crowd_group group : ruleList) {
                    //判断当前客户是否属于给定圈选规则
                    if (customerGroupService.isCrowdGroupIncludeMemberCard(group, member.getMember_card())) {
                        groupId = group.get_id();
                        break;
                    }
                }
                //根据规则插入客户信息
                if (StringUtils.isNotEmpty(groupId)) {
                    MemberLabel label = new MemberLabel();
                    label.setMemberCard(memberCard);
                    customerGroupService.memberCrowdGroupRunByLabels(groupId,Arrays.asList(label));
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
     * 将媒体类型code转换成媒体类型名称
     * wangzhe
     * 2021-03-16
     * @param code 媒体类型code
     * @return
     */
    private String convertMediaTypeCode2Name(Integer code){
        //"获客来源渠道(媒介类型) -1：其他，0:电视媒体, 1:广播电台媒体，2：社区媒体，3：户外媒体，4：印刷媒体，5：互联网媒体，6：电商站内流量媒体"
        String name = null;
        if (code == null) {
            return name;
        }
        switch (code) {
            case -1 :
                name = "其他";
                break;
            case 0 :
                name = "电视媒体";
                break;
            case 1 :
                name = "广播电台媒体";
                break;
            case 2 :
                name = "社区媒体";
                break;
            case 3 :
                name = "户外媒体";
                break;
            case 4 :
                name = "印刷媒体";
                break;
            case 5 :
                name = "互联网媒体";
                break;
            case 6 :
                name = "电商站内流量媒体";
                break;
        }
        return name;
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
        String codes = "";
        //去重后的商品编号
        Set<String> codeSet = new HashSet<>();
        //之前没有购买过的商品
        List<MemberProductEffectInsertVO> addProductVoList = new ArrayList<>();
        List<MemberProductEffectUpdateVO> updateProductVoList = new ArrayList<>();
        List<MemberProductEffectDTO> productEffectList = null;
        if (CollectionUtil.isNotEmpty(buyProductList)) {
            for (OrderProductVO productVO : buyProductList) {
                buyProductCodes.append(productVO.getProductCode()).append(",");
                codeSet.add(productVO.getProductCode());
            }
            if (buyProductCodes.length() > 0) {
                codes = buyProductCodes.substring(0, buyProductCodes.length() - 1);
            }

            Map<String, ProductMainDTO> productMap = new HashMap<>();

            //通过商品编号，获取商品信息
            List<ProductMainDTO> productList = ProductClientAPI.queryByProductCodes(codes.split(","));
            if (codeSet.size() != productList.size()) {
                return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "包含不存在的商品!");
            }
            if (CollectionUtil.isNotEmpty(productList)) {
                for (ProductMainDTO mainDTO : productList) {
                    productMap.put(mainDTO.getProductCode(), mainDTO);
                }
            }

            //查询客户对应的商品服用效果
            MemberProductEffectSelectVO effectVo = new MemberProductEffectSelectVO();
            effectVo.setMemberCard(memberCard);
            Map<String, MemberProductEffectDTO> dtoMap = new HashMap<>();
            ComResponse<List<MemberProductEffectDTO>> productEffectResult = memberProductEffectService.getProductEffects(effectVo);
            productEffectList = productEffectResult.getData();
            if (productEffectResult != null && CollectionUtil.isNotEmpty(productEffectList)) {
                for (MemberProductEffectDTO dto : productEffectList) {
                    dtoMap.put(dto.getProductCode(), dto);
                }
            }

            for (OrderProductVO productVO : buyProductList) {
                MemberProductEffectDTO dto = dtoMap.get(productVO.getProductCode());
                MemberProductEffectInsertVO addVo = null;
                MemberProductEffectUpdateVO upVo = null;
                //没有则新增
                if (dto != null) {
                    upVo = new MemberProductEffectUpdateVO();
                    BeanUtil.copyProperties(dto, upVo);
                    updateProductVoList.add(upVo);
                } else {
                    addVo = new MemberProductEffectInsertVO();
                    addVo.setMemberCard(memberCard);
                    addVo.setProductCode(productVO.getProductCode());
                    addProductVoList.add(addVo);
                }
                //获取商品的信息(主要是规格)
                ProductMainDTO ProductMainDTO = productMap.get(productVO.getProductCode());
                if (ProductMainDTO == null) {
                    continue;
                }
                int productCount = productVO.getProductCount() == null ? 1 : productVO.getProductCount();//购买商品的数量
                String totalUseNum = ProductMainDTO.getTotalUseNum();//当商品规格 数量
                if (addVo != null) {
                    if (StringUtils.isNotEmpty(totalUseNum)) {
                        addVo.setProductLastNum(Integer.valueOf(totalUseNum) * productCount);//商品剩余量
                    }
                    addVo.setProductCount(productCount);//购买商品数量
                    addVo.setProductName(ProductMainDTO.getName());//商品名称
                    addVo.setOrderNo(orderInfo4MqVo.getOrderNo());//商品关联的最后一次签收订单编号

                    addVo.setUnit(ProductMainDTO.getUnit());//商品的计量单位
                    //首次添加的商品的默认的商品的服用状态为正常服用 by wangzhe 20210320
                    addVo.setTakingState(1);

                    //默认保存商品信息里面的用量信息
                    addVo.setOneToTimes(ProductMainDTO.getOneToTimes());
                    addVo.setOneUseNum(ProductMainDTO.getOneUseNum());
                }else if (upVo != null){
                    //已经存在的记录，如果服用状态为空或商品的剩余量为0，则需要更新商品的服用状态为正常服用 by wangzhe 20210320
                    if (dto.getTakingState() == null || dto.getProductLastNum() == 0) {
                        upVo.setTakingState(1);
                    }

                    if (StringUtils.isNotEmpty(totalUseNum)) {
                        upVo.setProductLastNum(Integer.valueOf(totalUseNum) * productCount + dto.getProductLastNum());//商品剩余量
                    }

                    upVo.setProductName(ProductMainDTO.getName());//商品名称
                    upVo.setOrderNo(orderInfo4MqVo.getOrderNo());//商品关联的最后一次签收订单编号
                    upVo.setProductCount(dto.getProductCount() + productVO.getProductCount());//购买商品数量

                    //当商品的计量单位为空的时候，重新设置
                    if (StringUtils.isEmpty(upVo.getUnit())) {
                        upVo.setUnit(ProductMainDTO.getUnit());//商品的计量单位
                    }

                    //默认保存商品信息里面的用量信息
                    /*if (dto.getOneToTimes() == null) {
                        upVo.setOneToTimes(ProductMainDTO.getOneToTimes());
                    }
                    if (dto.getOneUseNum() == null) {
                        upVo.setOneUseNum(ProductMainDTO.getOneUseNum());
                    }*/
                }
            }
            //保存
            if (addProductVoList.size() > 0) {
                ComResponse comResponse = memberProductEffectService.batchSaveProductEffect(addProductVoList);
                if (comResponse.getCode() != 200) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ComResponse.fail(comResponse.getCode(), comResponse.getMessage());
                }
            }
            //更新
            if (updateProductVoList.size() > 0) {
                ComResponse comResponse = memberProductEffectService.batchModifyProductEffect(orderInfo4MqVo.getStaffNo(), updateProductVoList);
                if (comResponse.getCode() != 200) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ComResponse.fail(comResponse.getCode(), comResponse.getMessage());
                }
            }
        }

        //更新member_order_stat
        cn.net.yzl.crm.customer.model.db.MemberOrderStat memberOrderStat = memberOrderStatService.queryByMemberCode(memberCard);
        if (memberOrderStat == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "memberOrderStat记录不存在!");
        }

        //累计消费金额
        Integer totalCounsumAmount = memberOrderStat.getTotalCounsumAmount() == null ? 0 : memberOrderStat.getTotalCounsumAmount();
        totalCounsumAmount += orderInfo4MqVo.getSpend();
        memberOrderStat.setTotalCounsumAmount(totalCounsumAmount);

//        //累计充值金额
//        Integer totalInvestAmount = memberOrderStat.getTotalInvestAmount() == null ? 0 : memberOrderStat.getTotalInvestAmount();
//        totalInvestAmount += orderInfo4MqVo.getCash1();
//        memberOrderStat.setTotalInvestAmount(totalInvestAmount);

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
        if (memberOrderStat.getFirstOrderAm() == null || memberOrderStat.getFirstOrderAm().intValue() == 0) {
            memberOrderStat.setFirstOrderAm(orderInfo4MqVo.getTotalAll());//真正首单金额
        }
        if (memberOrderStat.getOrderHighAm() == null || memberOrderStat.getOrderHighAm() < orderInfo4MqVo.getTotalAll()) {
            memberOrderStat.setOrderHighAm(orderInfo4MqVo.getTotalAll());//订单最高金额
        }

        if (memberOrderStat.getOrderLowAm() == null || memberOrderStat.getOrderLowAm().intValue() == 0 || memberOrderStat.getOrderLowAm() > orderInfo4MqVo.getTotalAll()) {
            memberOrderStat.setOrderLowAm(orderInfo4MqVo.getTotalAll());//订单最低金额
        }

        if (memberOrderStat.getBuyCount() == null) {
            memberOrderStat.setBuyCount(0);
        }
        memberOrderStat.setBuyCount(memberOrderStat.getBuyCount() + 1);//累计购买次数
        memberOrderStat.setOrderAvgAm(memberOrderStat.getTotalOrderAmount() / memberOrderStat.getBuyCount());//订单平均金额
        int orgNum = productEffectList == null ? 0 : productEffectList.size();
        memberOrderStat.setProductTypeCnt(addProductVoList.size() + orgNum);//购买产品种类个数
        //memberOrderStat.setLastOrderTime(orderInfo4MqVo.getSignTime());

        //总平均购买天数
        if (memberOrderStat.getLastOrderTime() != null && memberOrderStat.getFirstOrderTime() != null) {
            long betweenDay = DateUtil.between(memberOrderStat.getFirstOrderTime(), memberOrderStat.getLastOrderTime(), DateUnit.DAY);
            Integer buyDay = Math.round(betweenDay / (float) memberOrderStat.getBuyCount());
            memberOrderStat.setDayAvgCount(buyDay);
        }
        //最后一次订单的签收时间
        memberOrderStat.setLastSignTime(orderInfo4MqVo.getSignTime());

        //memberOrderStat.setYearAvgCount();//年度平均购买天数 由订单的定时器对每天有更新操作的订单进行T+1扫描，不在这里处理
        int result = memberOrderStatMapper.updateByPrimaryKeySelective(memberOrderStat);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"记录数据保存失败!");
        }

        //查询顾客表信息
        Member member = memberMapper.selectMemberByCard(memberCard);

        //更新客户表订单总金额
        member.setTotal_amount(totalCounsumAmount);//累计消费金额


        //顾客的首单金额为空或者为0的时候,设置顾客的首单金额
        if (member.getFirst_order_am() == null || member.getFirst_order_am().intValue() == 0) {
            member.setFirst_order_am(orderInfo4MqVo.getSpend());//首单正真金额
        }
        //顾客的会员标识不是会员的时要设置会员标识(这个标识在第一次订单签收的时候更新为1，之后不会再改变，即有第一次订单签收操作就更新为1)
        if (!member.isVip_flag()) {
            memberMapper.setMemberToVip(memberCard,orderInfo4MqVo.getSignTime());
        }
        result = memberMapper.updateByMemberGradeByMember(member);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"记录数据保存失败!");
        }

        //判断是否存在冻结消费，存在则确认消费
        MemberAmountDetail frozenDetail = memberAmountService.getFrozenDetailByOrder(orderInfo4MqVo.getOrderNo(), 2);
        //存在冻结消费，要进行确认
        if (frozenDetail != null) {
            ComResponse<String> response = memberAmountService.operationConfirm(2, orderInfo4MqVo.getOrderNo());
            if (response == null || response.getCode() != 200) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"确认扣款失败!");
            }
        }else{
            log.info("订单,{}签收时未找到消费冻结记录",orderInfo4MqVo.getOrderNo());
        }

        //判断是否是货到付款，货到付款要进行充值
        Integer payType = orderInfo4MqVo.getPayType();
        Integer cash1 = orderInfo4MqVo.getCash1();
        if (payType != null && payType == 1 && cash1 != null && cash1 > 0){
            MemberAmountDetailVO vo = new MemberAmountDetailVO();
            vo.setOrderNo(orderInfo4MqVo.getOrderNo());
            vo.setMemberCard(memberCard);
            vo.setDiscountMoney(orderInfo4MqVo.getCash1());//预存金额 分为单位
            vo.setObtainType(3);//3:充值
            vo.setRemark("订单签收时,预存金额");
            ComResponse<String> response = null;
            try {
                response = memberAmountService.operation(vo);
            } catch (Exception e) {
                log.error("订单:{}签收时,预存金额操作异常,",orderInfo4MqVo.getOrderNo(),e);
            }
            if (response == null || response.getCode() != 200) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ComResponse.fail(response.getCode(),response.getMessage());
            }
        }

        //设置缓存
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),memberCard);
        return ComResponse.success(true);
    }

    /**
     * 处理工单时更新
     * @param workOrderInfoVO
     * @return
     */
//    @Override
//    public ComResponse<Boolean> dealWorkOrderUpdateMemberData(MemberWorkOrderInfoVO workOrderInfoVO) {
//        //设置reids缓存
//        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),workOrderInfoVO.getMemberCard());
//        return ComResponse.success(true);
//    }

    /**
     * 下单时，
     *  更新最后下单时间
     * @param orderCreateInfoVO
     * @return
     */
    @Transactional
    @Override
    public ComResponse<Boolean> dealOrderCreateUpdateMemberData(OrderCreateInfoVO orderCreateInfoVO) {
        //通过顾客卡号获取顾客
        String memberCard = orderCreateInfoVO.getMemberCard();
        Member member = memberMapper.selectMemberByCard(memberCard);
        if (member == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"会员卡号:"+memberCard+"不存在！");
        }
        boolean isFirst = false;
        //member_order_stat
        cn.net.yzl.crm.customer.model.db.MemberOrderStat memberOrderStat = memberOrderStatService.queryByMemberCode(memberCard);
        if (memberOrderStat == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "memberOrderStat记录不存在!");
        }

        if ((StringUtils.isEmpty(memberOrderStat.getFirstOrderNo()) ||  "0".equals(memberOrderStat.getFirstOrderNo()))) {
            isFirst = true;
            memberOrderStat.setFirstOrderNo(orderCreateInfoVO.getOrderNo());//首单订单号
            memberOrderStat.setFirstOrderStaffNo(orderCreateInfoVO.getStaffNo());//首单下单员工
            memberOrderStat.setFirstOrderTime(orderCreateInfoVO.getCreateTime());//首单下单时间
        }
        memberOrderStat.setLastOrderTime(orderCreateInfoVO.getCreateTime());//最后一次下单时间
        int result = memberOrderStatMapper.updateByPrimaryKeySelective(memberOrderStat);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"更新顾客信息异常!");
        }

        //更新member中的首单信息(根据首单订单号是否为空)
        if (isFirst) {
            member.setFirst_order_staff_no(orderCreateInfoVO.getStaffNo());//首单下单员工
            member.setFirst_order_time(orderCreateInfoVO.getCreateTime());//首单下单时间
        }
        //memberMapper.updateLastOrderTime(memberCard,orderCreateInfoVO.getCreateTime());
        //最后一次订单的下单时间 = 当前订单的创建时间
        member.setLast_order_time(orderCreateInfoVO.getCreateTime());
        //下单时更新顾客信息
        result = memberMapper.updateMemberForOrderCreate(member);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"更新顾客基本信息异常!");
        }

        //设置reids缓存
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),memberCard);
        return ComResponse.success(true);
    }

    @Transactional
    @Override
    public int saveMemberReferral(MemberAndAddWorkOrderVO memberReferralVO) {
        Member memberVO = memberReferralVO.getMemberVO();
        WorkOrderBeanVO workOrderBeanVO = memberReferralVO.getWorkOrderBeanVO();
        //转介绍客户的获客渠道取自介绍客户
        if (2 == memberVO.getIntro_type() && StringUtils.isNotEmpty(memberVO.getIntro_no())){
            Member member = this.selectMemberByCard(memberVO.getIntro_no());
            if (member != null) {
                memberVO.setSource(member.getSource());
            }
        }

        //保存用户信息
        int result = 0;
        try {
            result = this.insert(memberVO);
        } catch (Exception e) {
            if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().endsWith("已经被使用!")){
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "用户已经存在!");
            }else{
                throw e;
            }
        }

        if (result > 0) {
            Date now = new Date();
            //保存工单
            String memberCard = memberVO.getMember_card();
            workOrderBeanVO.setMemberCard(memberCard);
            workOrderBeanVO.setAcceptStatus(2);//工单接收状态：已接收

            workOrderBeanVO.setAllocateTime(now);//分配时间
            workOrderBeanVO.setApplyUpStatus(0);//上交状态：未上交
            workOrderBeanVO.setCallFlag(0);//员工当日拨打状态：未拨打
            workOrderBeanVO.setCallTimes(0);//坐席已拨打次数：0次
            /*List<MemberPhone> memberPhoneList = memberVO.getMemberPhoneList();
            if (CollectionUtil.isNotEmpty(memberPhoneList)) {
                //默认为第一个
                workOrderBeanVO.setCalledPhone(memberPhoneList.get(0).getPhone_number());//被叫号码
                for (MemberPhone phone : memberPhoneList) {
                    if (phone != null && phone.getPhone_type() == 1) {
                        workOrderBeanVO.setCalledPhone(phone.getPhone_number());//被叫号码
                        break;
                    }
                }
            }*/
            //workOrderBeanVO.setCallerPhone("400-");//主叫号码
            if (workOrderBeanVO.getCreateTime() == null) {
                workOrderBeanVO.setCreateTime(now);//创建时间
            }

            workOrderBeanVO.setAllocateStatus(1);
            workOrderBeanVO.setActivity(3);
            workOrderBeanVO.setProductTypeCnt(0);
            workOrderBeanVO.setProductLastNum(0);

            workOrderBeanVO.setHistoryFlag(0);//非历史数据
            workOrderBeanVO.setIsVisiable(1);//可见
            workOrderBeanVO.setIsWorkOrder(0);//不是建档工单
            if (StringUtils.isEmpty(workOrderBeanVO.getMemberName())) {
                workOrderBeanVO.setMemberName(memberVO.getMember_name());//会员名称
            }
            workOrderBeanVO.setMGradeCode("1");
            workOrderBeanVO.setSouce(1);//工单来源：自有的
            workOrderBeanVO.setStatus(1);//工单处理状态：未处理
            workOrderBeanVO.setTradeStatus(2);//工单成交状态：未成交
            workOrderBeanVO.setTransTimes(0);//调整次数：0
            if (workOrderBeanVO.getUpdateTime() == null) {
                workOrderBeanVO.setUpdateTime(now);//修改时间
            }
            workOrderBeanVO.setVisitType(2);//工单类别：常规回访
            workOrderBeanVO.setWorkOrderMoney(new BigDecimal("0.00"));
            workOrderBeanVO.setWorkOrderType(1);//工单类型：热线工单
            ComResponse<Void> response = workOrderClient.addWorkOrder(workOrderBeanVO);
            if (response.getCode() != 200) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "创建工单失败!");
            }

            //设置缓存(2小时后同步)
            redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),memberVO.getMember_card());
        }
        return result;
    }

    @Override
    //@Transactional
    public boolean updateMemberGrandValidityInit() throws IOException {//MemberSysParamDetailResponse
        //获取DMC的会员到期时间
        //String validDate = ActivityClientAPI.getMemberGradeValidDate();
        MemberGradeValidDate validDateObj = ActivityClientAPI.getMemberGradeValidDateObj();
        if (validDateObj.getIsAlways() == null || validDateObj.getIsAlways()) {
            log.info("updateMemberGrandValidityInit:会员有效期类型为长期有效！");
            return true;
        }
        Date todayStart = DealDateUtil.getStart(new Date());

        Date yearValidDate = validDateObj.getCurrentYearValidDate();
        long between = DateUtil.between(todayStart,yearValidDate, DateUnit.DAY, false);
        if (between != 0) {
            log.info("updateMemberGrandValidityInit:会员未到期!当前时间为：{}，DMC会员到期时间为：{}！",todayStart,yearValidDate);
            return true;
        }

        int pageNo = 1,pageSize = 1_000;

        MemberSerchConditionDTO dto = new MemberSerchConditionDTO();
        dto.setPageSize(pageSize);
        List<Member> list = null;
        do {
            dto.setCurrentPage(pageNo);
            PageHelper.startPage(pageNo,pageSize);

            list = memberMapper.scanMemberByPage(dto);
            if (CollectionUtil.isEmpty(list)) {
                break;
            }
            for (Member member : list) {
                //当前顾客的会员级别信息
                MemberGradeRecordPo memberGradeRecord = new MemberGradeRecordPo();
                memberGradeRecord = new MemberGradeRecordPo();
                memberGradeRecord.setMemberCard(member.getMember_card());
                memberGradeRecord.setCreateTime(new Date());
                memberGradeRecord.setBeforeGradeId(member.getMGradeId());
                memberGradeRecord.setBeforeGradeName(member.getMGradeName());
                memberGradeRecord.setMGradeId(1);
                memberGradeRecord.setMGradeName("无卡");
                memberGradeRecordDao.insertSelective(memberGradeRecord);
                //更新顾客表的会员信息
                member.setMGradeId(memberGradeRecord.getMGradeId());
                member.setMGradeName(memberGradeRecord.getMGradeName());
                int ret = memberMapper.updateByMemberGradeByMember(member);
            }

            if (list.size() < pageSize) {
                list.clear();
                break;
            }
            list.clear();
            pageNo++;
        } while (true);
        return false;
    }

    @Override
    @Transactional
    public ComResponse<Boolean> memeberWorkOrderSubmit(MemeberWorkOrderSubmitVo vo) {
        if (StringUtils.isEmpty(vo.getMemberCard())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数:memberCard不能为空!");
        }else if (StringUtils.isEmpty(vo.getStaffNo())) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数:staffNo不能为空!");
        }
        //更新顾客信息
        ComResponse<Boolean> response = updateMember(vo);
        if (response.getCode() != 200) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response;
        }
        //更新顾客病症
        Integer result = updateMemberDisease(vo.getMemberCard(), vo.getStaffNo(), vo.getMemberDiseaseList());
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"(顾客病症)记录数据保存失败!");
        }

        //更新商品服用效果
        ComResponse comResponse = memberProductEffectService.batchModifyProductEffect(vo.getStaffNo(), vo.getProductEffectList());
        if (comResponse.getCode() != 200) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"(商品服用效果)记录数据保存失败!");
        }

        //更新顾客咨询商品
        ComResponse<String> consultation = batchSaveProductConsultation(vo.getProductConsultationInsertVOList());
        if (consultation.getCode() != 200) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"(顾客咨询商品)记录数据保存失败!");
        }
        //更新顾客行为偏好
        ComResponse<Boolean> response1 = memberActionRelationService.saveOrUpdateMemberActionRelation(vo.getMemberCard(), vo.getStaffNo(), vo.getMemberActionDIdList());
        if (response1.getCode() != 200) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(response1.getCode(),response1.getMessage());
        }

        //设置reids缓存
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),vo.getMemberCard());

        return ComResponse.success(true);
    }

    private static Integer getMonth(String date) {
        String[] str = date.split("-");
        if(str.length>2){
            return Integer.parseInt(str[1]);
        }
        return 0;
    }


    public Pair<Date,Date> getDateScope(String validDate) {
        Date startDate = null;
        Date endDate = null;
        //0表示不限制会员到期时间
        if ( StringUtils.isNotEmpty(validDate) || "0".equals(validDate) || "1".equals(validDate)) {
            return Pair.of(startDate, endDate);
        }
        //格式化会员到期时间
        Date valid = DateUtil.parse(validDate, "yyyy-MM-dd");

        //当前日期字符串，格式：yyyy-MM-dd
        String today= DateUtil.today();
        Date todayDate = DateUtil.parse(today);
        long between = DateUtil.between(todayDate, valid, DateUnit.DAY, false);
        if (between <= 0) {
            startDate = DateUtil.offsetDay(valid, 1);
            endDate = DateUtil.endOfDay(DateUtil.date());//一天的结束，结果：2017-03-01 23:59:59
        }else {
            //没过:到期时间一年前 到今天
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTime(valid);
            c.add(Calendar.YEAR, -1);
            c.add(Calendar.DATE,1);
            startDate = DateUtil.beginOfDay(c.getTime());//一年前 + 1天
            //今天的结束，结果：2017-03-01 23:59:59
            endDate = DateUtil.endOfDay(DateUtil.date());//今天
        }
        return Pair.of(startDate, endDate);
    }

    /**
     * 更新顾客病症
     * wangzhe
     * 2021-02-25
     * @param memberCard 会员卡号
     * @param createNo 员工编号
     * @param memberDiseaseList 病症
     * @return
     */
    @Transactional
    public Integer updateMemberDisease(String memberCard,String createNo, List<MemberWorkOrderDiseaseVo> memberDiseaseList){
        if (CollectionUtil.isEmpty(memberDiseaseList)) {
            return 1;
        }
        //删除顾客所有的病症，重新添加
        int result =  memberDiseaseMapper.deleteMemberDiseaseByMemberCard(memberCard);
        Date now = new Date();
        for (MemberWorkOrderDiseaseVo memberDisease : memberDiseaseList) {
            cn.net.yzl.crm.customer.model.db.MemberDisease disease = new cn.net.yzl.crm.customer.model.db.MemberDisease();
            disease.setCreateTime(now);
            disease.setMemberCard(memberCard);
            disease.setCreateNo(createNo);
            disease.setDiseaseId(memberDisease.getDiseaseId());
            disease.setDiseaseName(memberDisease.getDiseaseName());

            result = memberDiseaseMapper.insertSelective(disease);
        }

        return result;


    }

    @Override
    public boolean addredis(String memberCard) {
        //设置缓存
        redisUtil.sSet(CacheKeyUtil.syncMemberLabelCacheKey(),memberCard);
        return true;
    }

    /**
     *
     * @param vo
     * @return
     */
    @Transactional
    public ComResponse<Boolean> updateMember(MemeberWorkOrderSubmitVo vo){
        Map<Integer, String> phoneMapList = new HashMap<>();
        int result;
        String noZeroNumber = "";
        String haveZeroNumber = "";
        String errStr = "";
        //手机号
        if (StringUtils.isNotEmpty(vo.getMemberPhone())) {
            phoneMapList.put(1,vo.getMemberPhone());
        }
        //座机号
        if (StringUtils.isNotEmpty(vo.getFixedPhoneNum())) {
            phoneMapList.put(2,vo.getFixedPhoneNum());
        }

        if (CollectionUtil.isNotEmpty(phoneMapList)) {
            //查询顾客电话信息
            List<MemberPhone> memberPhoneList = phoneMapper.getMemberPhoneByMemberCard(vo.getMemberCard());
            for (Map.Entry<Integer, String> entry : phoneMapList.entrySet()) {
                Integer type = entry.getKey();
                String phoneNumber = entry.getValue();
                //是否以0开头 --> 去掉0
                if (phoneNumber.startsWith("0")){
                    noZeroNumber = phoneNumber.substring(1);
                    haveZeroNumber = phoneNumber;
                }else{
                    noZeroNumber = phoneNumber;
                    haveZeroNumber = "0" + phoneNumber;
                }
                //校验手机号
                int phoneType = 0;
                if (memberPhoneService.isMobile(noZeroNumber)) {
                    phoneType = 1;
                }
                //不是手机号时，要校验是否为电话号
                if (phoneType == 0 && memberPhoneService.isPhone(phoneNumber)){
                    phoneType = 2;
                }
                if (type.intValue() == 1){
                    errStr = "手机号码";
                }else {
                    errStr = "座机号码";
                }

                if (phoneType != type.intValue()) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),errStr + "格式不正确!",false);
                }

                String memberCard= phoneMapper.getMemberCardByPhoneNumber(Arrays.asList(haveZeroNumber,noZeroNumber));
                if (StringUtils.isNotEmpty(memberCard) && !memberCard.equals(vo.getMemberCard())) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),errStr + "已经被使用!",false);
                }
                MemberPhone memberPhone = new MemberPhone();
                if (CollectionUtil.isNotEmpty(memberPhoneList)) {
                    for (MemberPhone item : memberPhoneList) {
                        //类型相同的
                        if (haveZeroNumber.equals(item.getPhone_number()) || noZeroNumber.equals(item.getPhone_number()) ) {
                            memberPhone = item;
                            break;
                        }
                    }
                }
                memberPhone.setPhone_type(phoneType);
                memberPhone.setPhone_number(phoneNumber);
                memberPhone.setUpdator_no(vo.getStaffNo());//修改人id
                memberPhone.setUpdate_time(new Date());//修改时间
                memberPhone.setPhone_type(phoneType);
                if (memberPhone.getEnabled() == null) {
                    memberPhone.setEnabled(1);//默认可用
                }
                //新增记录
                if (memberPhone.getId() == null) {
                    memberPhone.setMember_card(vo.getMemberCard());
                    memberPhone.setCreator_no(memberPhone.getUpdator_no());
                    memberPhone.setCreate_time(memberPhone.getUpdate_time());
                    result = phoneMapper.insertSelective(memberPhone);
                    if (result < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"保存" + errStr + ":"+phoneNumber+"失败!",false);
                    }
                } else{
                    //更新记录
                    memberPhone.setPhone_place("");
                    memberPhone.setService_provider(0);
                    result = phoneMapper.updateByPrimaryKeySelective(memberPhone);
                    if (result < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"修改 + errStr + :"+phoneNumber+"失败!",false);
                    }
                }
            }
        }

        //保存顾客信息
        Member member = new Member();
        member.setMember_card(vo.getMemberCard());//会员卡号
        member.setMember_name(vo.getMemberName());//会员名称
        member.setSex(vo.getSex());//性别
        member.setAge(vo.getAge());//年龄
        member.setBirthday(vo.getBirthday());

        member.setEmail(vo.getEmail());
        member.setQq(vo.getQq());
        member.setWechat(vo.getWechat());

        member.setRegion_code(vo.getRegionCode());
        member.setRegion_name(vo.getRegionName());

        member.setProvince_code(vo.getProvinceCode());
        member.setProvince_name(vo.getProvinceName());
        if(vo.getCityCode()!=null){
            member.setCity_code(vo.getCityCode());
        }else{
            member.setCity_code(-9999);
        }
        member.setCity_name(vo.getCityName());
        if(vo.getAreaCode()!=null){
            member.setArea_code(vo.getAreaCode());
        }else{
            member.setArea_code(-9999);
        }
        member.setArea_name(vo.getAreaName());
        member.setUpdator_no(vo.getStaffNo());
        member.setUpdator_name(vo.getStaffName());//修改人
        member.setUpdate_time(new Date());

        member.setAddress(vo.getAddress());

        member.setBuy_intention(vo.getBuyIntention());//购买意向
        member.setActivity(vo.getActivity());//活跃度 1 活跃 2 冷淡 3 一般

        //1.更新顾客信息
        result = this.updateByMemberCardSelective(member);

        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录数据保存失败!",false);
        }
        return ComResponse.success(true);
    }


    /**
     * 定时器，定时获取redis中的set集合中的会员卡号进行同步会员标签信息到mongo
     * 注:目前暂定为2小时同步一次
     * wangzhe
     * 2021-02-27
     * @return
     */
    public boolean updateMemberLabelForTask() {
        //获取redis缓存
        String key = CacheKeyUtil.syncMemberLabelCacheKey();
        redisUtil.setRemove(key, "");//防止redis中有""
        Set<Object> memberSet = redisUtil.sGet(key);
        //判断当前是否有需要同步数据
        if (CollectionUtil.isEmpty(memberSet)) {
            log.info("定时器同步顾客标签数据:当前没有需要同步的数据！");
            return true;
        }
        //将Set<Object>转换为List<string>
        List<String> memberCards = memberSet.stream().map(String::valueOf).collect(Collectors.toList());
        //同步顾客的所有数据
        boolean result = syncMemberLabel(memberCards, 1, key);

        return result;
    }

    /**
     * 根据顾客会员卡号同步顾客标签
     * wangzhe
     * 2021-02-26
     * @param memberCodes
     * @param type : type = 1 同步所有的数 2.只同步顾客的基本资料(本服务数据)  3.去其他模块拉去数据
     *
     * @return
     */
    public boolean syncMemberLabel(List<String> memberCodes,int type,String redisKey) {
        if (CollectionUtil.isEmpty(memberCodes)) {
            return true;
        }
        //查询MySql数据库中客户的相关信息
        List<MemberLabel> list = memberMapper.queryMemberLabelByCodes(memberCodes);
        if (CollectionUtil.isEmpty(list)) {
            log.error("本次要同步数据的客户编号有{}条记录,但数据库都不存在,例如：{}",memberCodes.size(),memberCodes.get(0));
            return true;
        }

        log.info("本次要同步数据的类型为:{},redisKey为:{},同步数据的顾客会员卡号,包含:{}",type,redisKey,memberCodes.get(0));

        //获取MONGO里面的顾客标签
        Map<String,String> mongoMemberLabels = memberLabelDao.queryByCodes(memberCodes);

        Map<String, List<cn.net.yzl.crm.customer.model.mogo.MemberDisease>> memberDiseaseListMap = new HashMap<>();//顾客病症
        Map<String, List<ActionDict>> memberActionDictListMap = new HashMap<>();//顾客行为偏好
        Map<String, List<MemberProduct>> memberProductListMap = new HashMap<>();//顾客产品服用效果

        /**
         * 从本项目同步数据
         */
        if (type == 1 || type == 2) {
            //获取顾客病症
            List<cn.net.yzl.crm.customer.model.mogo.MemberDisease> memberDiseaseList = memberDiseaseMapper.queryByMemberCodes(memberCodes);
            memberDiseaseListMap = memberDiseaseList.stream()
                    .collect(Collectors.groupingBy(cn.net.yzl.crm.customer.model.mogo.MemberDisease::getMemberCard));

            //查询综合行为( from member_action_relation where member_card in)
            List<ActionDict> actionDictList = memberMapper.queryActionByMemberCodes(memberCodes);
            memberActionDictListMap = actionDictList.stream()
                    .collect(Collectors.groupingBy(ActionDict::getMemberCard));

            //通过会员卡号查询顾客服用效果( from member_product_effect where member_card in)
            List<MemberProduct> memberProducts = memberMapper.queryProductByMemberCodes(memberCodes);
            memberProductListMap = memberProducts.stream()
                    .collect(Collectors.groupingBy(MemberProduct::getMemberCard));
        }

        /**
         * 从其他模块同步数据
         */
        List<MemberLevelPagesResponse> levelList = null;
        MemberGradeValidDate validDateObj = null;
        if (type == 1 || type == 3) {
            //会员升级[已经按级别倒叙排序](DMC)
            levelList = ActivityClientAPI.getMemberLevelList();
            //获取DMC的会员到期时间
            validDateObj = ActivityClientAPI.getMemberGradeValidDateObj();
        }

        boolean result = true;
        String memberCard,_id;
        //封装标签数据
        for (MemberLabel memberLabel : list) {
            //获取会员卡号
            memberCard = memberLabel.getMemberCard();
            //获取mongo里历史数据的_id
            _id = mongoMemberLabels.get(memberCard);
            //设置历史数据的_id
            if(StringUtils.isNotEmpty(_id)){
                memberLabel.set_id(_id);
            }

            //同步基础数据
            if (type == 1 || type == 2) {
                //处理顾客基本信息
                dealMemberBase(memberLabel);
                //处理顾客的服用效果
                dealMemberProductEffect(memberLabel,memberProductListMap.get(memberCard));
                //处理顾客病症
                dealMemberDisease(memberLabel, memberDiseaseListMap.get(memberCard));
                //处理顾客的综合行为
                dealMemberAction(memberLabel,memberActionDictListMap.get(memberCard));
            }

            //同步其他应用数据
            if (type == 1 || type == 3) {
                //获取顾客的红包 积分 优惠券记录(DMC)
                result = dealMemberAmountRedbagIntegral(memberCard, memberLabel);
                //会员升级[已经按级别倒叙排序](DMC)
                result = upgradedMembarVipLevel(memberCard, levelList, validDateObj);
                //处理会员订单
                result = dealMemberOrder(memberCard, memberLabel);
                //处理最后一次进线、最后一次通话
                result = dealLastCallData(memberCard, memberLabel);
            }

            //判断是否操作成功
            if (!result) {
                log.error("同步其他应用数据,操作异常！当前处理的顾客的会员卡号为:{},请检查数据！",memberCard);
                continue;
            }

            //保存到mongo数据库
            memberLabelDao.save(memberLabel);

            //删除redis set集合里缓存的顾客会员卡号
            if (StringUtils.isNotEmpty(redisKey)) {
                redisUtil.setRemove(redisKey, memberCard);
            }
            log.info("数据同步,顾客卡号:{}数据同步完成！",memberCard);

        }
        log.info("数据同步完成,计划同步数据:{}条，实际从数据库中查询会员信息查询出:{}条",memberCodes.size(),list.size());
        return true;
    }

    /**
     * 保存会员积分红包记录
     * wangzhe
     * 2021-02-27
     * @param memberCard 顾客会员卡号
     * @param memberLabel 顾客标签对象
     * @return
     */
    public boolean dealMemberAmountRedbagIntegral(String memberCard, MemberLabel memberLabel){
        int result = 0;
        //获取顾客的红包 积分 优惠券记录
        MemberAmountRedbagIntegral memberAmountRedbagIntegral = memberAmountRedbagIntegralMapper.selectByMemberCard(memberCard);
        if (memberAmountRedbagIntegral == null) {
            memberAmountRedbagIntegral = new MemberAmountRedbagIntegral();
            memberAmountRedbagIntegral.setMemberCard(memberCard);
        }
        //From DMC:根据单个会员卡号获取 每个顾客的优惠券 积分 红包
        MemberAccountResponse accountResponse = ActivityClientAPI.getAccountByMemberCard(memberCard);
        if (accountResponse != null) {
            //积分
            if (accountResponse.getMemberIntegral() != null && accountResponse.getMemberIntegral() > 0) {
                memberLabel.setHasIntegral(true);
                memberAmountRedbagIntegral.setLastIntegral(accountResponse.getMemberIntegral());
            }else{
                memberLabel.setHasIntegral(false);
                memberAmountRedbagIntegral.setLastIntegral(0);
            }

            //红包
            if (accountResponse.getMemberRedBag() != null && accountResponse.getMemberRedBag().compareTo(BigDecimal.ZERO) > 0) {
                memberLabel.setHasTedBag(true);
                memberAmountRedbagIntegral.setLastRedBag(accountResponse.getMemberRedBag().intValue());
            }else{
                memberLabel.setHasTedBag(false);
                memberAmountRedbagIntegral.setLastRedBag(0);
            }

            //优惠券
            if (accountResponse.getMemberCouponSize() != null && accountResponse.getMemberCouponSize() > 0) {
                memberLabel.setHasIntegral(true);
            }else{
                memberLabel.setHasIntegral(false);
            }
            //新增
            if (memberAmountRedbagIntegral.getId() == null) {
                result = memberAmountRedbagIntegralMapper.insertSelective(memberAmountRedbagIntegral);
            }else{
                //更新
                result = memberAmountRedbagIntegralMapper.updateByPrimaryKeySelective(memberAmountRedbagIntegral);
            }
        }
        return result > 0;
    }


    /**
     * 会员升级
     * wangzhe
     * 2021-02-27
     * @param memberCard 会员卡号
     * @param levelList dmc会员等级列表
     * @param validDateObj
     * @return
     */
    public boolean upgradedMembarVipLevel(String memberCard, List<MemberLevelPagesResponse> levelList, MemberGradeValidDate validDateObj){
        //会员升级[已经按级别倒叙排序](DMC)
        //List<MemberLevelPagesResponse> levelList = ActivityClientAPI.getMemberLevelList();
        //获取到DMC的会晕级别列表和会员到期时间后才去判断会员是否升级(注意:有了会员到期时间 才能 从订单:获取 一次性预存款 一次性消费满多少 一年累计消费满)
        if (CollectionUtil.isNotEmpty(levelList) && validDateObj != null && validDateObj.getIsAlways() != null) {
            //从订单:获取 一次性预存款 一次性消费满多少 一年累计消费满
            MemberTotal memberTotalData = OrderClientAPI.queryMemberTotalByMemberCard(memberCard,validDateObj.getStartDate(),validDateObj.getEndDate());
            if (memberTotalData != null) {
                Integer totalSpend = memberTotalData.getTotalSpend() == null ? 0 : memberTotalData.getTotalSpend();//累计消费
                Integer maxSpend = memberTotalData.getMaxSpend() == null ? 0 : memberTotalData.getMaxSpend();//最高消费
                Integer maxCash1 = memberTotalData.getMaxCash1() == null ? 0 : memberTotalData.getMaxCash1();//最高预存

                MemberLevelPagesResponse level = null;
                //遍历DMC会员级别信息，判断顾客当前属于那个级别
                for (MemberLevelPagesResponse levelData : levelList) {
                    //必须是开启状态的
                    if (levelData.getIsOpen() != 1) {
                        continue;
                    }
                    //注:DMC返回的数据的单位是元
                    if (levelData.getYearTotalSpendMoney() != null && levelData.getYearTotalSpendMoney().compareTo(BigDecimal.ZERO) > 0 && totalSpend -levelData.getYearTotalSpendMoney().multiply(new BigDecimal(100)).intValue() >= 0) {//一年累计消费满
                        level = levelData;
                        break;
                    } else if (levelData.getDisposableSpendMoney() != null && levelData.getDisposableSpendMoney().compareTo(BigDecimal.ZERO) > 0 && maxSpend-levelData.getDisposableSpendMoney().multiply(new BigDecimal(100)).intValue() >= 0) {//一次性消费满多少
                        level = levelData;
                        break;
                    } else if (levelData.getDisposableAdvanceMoney() != null && levelData.getDisposableAdvanceMoney().compareTo(BigDecimal.ZERO) > 0 && maxCash1-levelData.getDisposableAdvanceMoney().multiply(new BigDecimal(100)).intValue() >= 0) {//一次性预存款
                        level = levelData;
                        break;
                    }
                }
                if (level != null) {
                    //查询顾客表信息
                    Member member = memberMapper.selectMemberByCard(memberCard);
                    //等级相同不更新
                    if (member.getMGradeId() == null || !member.getMGradeId().equals(level.getMemberLevelGrade())){
                        //当前顾客的会员级别信息
                        MemberGradeRecordPo memberGradeRecord = new MemberGradeRecordPo();
                        memberGradeRecord = new MemberGradeRecordPo();
                        memberGradeRecord.setMemberCard(memberCard);
                        memberGradeRecord.setCreateTime(new Date());
                        memberGradeRecord.setBeforeGradeId(member.getMGradeId());
                        memberGradeRecord.setBeforeGradeName(member.getMGradeName());
                        memberGradeRecord.setMGradeId(level.getMemberLevelGrade());
                        memberGradeRecord.setMGradeName(level.getMemberLevelName());
                        int result = memberGradeRecordDao.insertSelective(memberGradeRecord);
                        if (result < 1) {
                            return false;
                        }
                        //更新顾客表的会员信息
                        member.setMGradeId(memberGradeRecord.getMGradeId());
                        member.setMGradeName(memberGradeRecord.getMGradeName());
                        result = memberMapper.updateByMemberGradeByMember(member);
                        if (result < 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }


    /**
     * 获取顾客订单书记并保存到mamberLabel中
     * wangzhe
     * 2021-02-27
     * @param memberCard 顾客会员卡号
     * @param memberLabel
     * @return
     */
    public boolean dealMemberOrder(String memberCard,MemberLabel memberLabel){
        MemberOrderObject orderObject = OrderClientAPI.querymemberorder(memberCard);
        List<MemberOrder> memberRefOrders = new ArrayList<>();//用户存放转换后的订单数据
        if (orderObject != null) {
            List<MemberOrderDTO> orderDatas = orderObject.getOrders();//会员订单数据
            if (CollectionUtil.isNotEmpty(orderDatas)) {
                for (MemberOrderDTO order : orderDatas) {
                    MemberOrder memberOrder = new MemberOrder();
                    memberOrder.setMemberCard(orderObject.getMemberCardNo());
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
            }
        }
        //集合为空表示没有订单数据(存在调订单接口失败的情况，实际上可能是有数据的)
        if (CollectionUtils.isEmpty(memberRefOrders)){
            memberLabel.setHaveOrder(0);
            return true;
        }
        //获取订单中的活动
        List<Integer> activityCodes = new ArrayList<>();
        List<MemberOrder> hasActOrders = new ArrayList<>();//有活动id的订单
        for(MemberOrder memberOrder:memberRefOrders) {
            if (StringUtils.isEmpty(memberOrder.getActivityCode())) {
                continue;
            }
            activityCodes.add(Integer.valueOf(memberOrder.getActivityCode()));
            hasActOrders.add(memberOrder);
        }
        Map<Integer,Integer> activityMap = new HashMap<>();
        //调DMC获取活动
        if (CollectionUtil.isNotEmpty(activityCodes)) {
            List<ActivityDetailResponse> activityDetailResponses = ActivityClientAPI.getActivityProductByBusNos(activityCodes);
            for (ActivityDetailResponse detail : activityDetailResponses) {
                activityMap.put(detail.getBusNo().intValue(), detail.getActivityType());
            }
        }
        Integer activityType;
        for (MemberOrder order : hasActOrders) {
            activityType = activityMap.get(order.getActivityCode());
            if (activityType != null) {
                order.setActivityType(String.valueOf(activityType));
            }
            order.setActivityFlag(true);
        }

        //将订单数据保存到memberLabel中
        memberLabel.setMemberOrders(memberRefOrders);
        memberLabel.setHaveOrder(1);
        return true;
    }

    /**
     * 处理顾客最后一次进线、最后一次通话
     * wangzhe
     * 2021-02-27
     * @param memeberCard
     * @param memberLabel
     * @return
     */
    public boolean dealLastCallData(String memeberCard, MemberLabel memberLabel) {
        //顾客最后一次进线，最后一次通话
        MemberLastCallInDTO lastCallData = WorkOrderClientAPI.getLastCallManageByMemberCard(memeberCard);
        if (lastCallData != null) {
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
        return true;
    }


    /**
     * 处理顾客基本信息
     * wangzhe
     * 2021-02-27
     * @param memberLabel
     * @return
     */
    public boolean dealMemberBase(MemberLabel memberLabel){
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

        //设置会员卡号
//        String _id = mongoMemberLabels.get(memberCard);
//        if(StringUtils.isNotEmpty(_id)){
//            memberLabel.set_id(_id);
//        }
        return true;
    }


    /**
     * 处理顾客的服用效果
     * @param memberLabel
     * @param products
     */
    public void dealMemberProductEffect(MemberLabel memberLabel,List<MemberProduct> products){
        if(!CollectionUtils.isEmpty(products)){
            memberLabel.setMemberProductList(products);
        }
    }

    /**
     * 处理顾客的综合行为
     * wangzhe
     * 2021-02-27
     * @param memberLabel
     * @param actionDicts
     */
    public void dealMemberAction(MemberLabel memberLabel,List<ActionDict> actionDicts){
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

    }

    /**
     * 处理顾客病症
     * wangzhe
     * 2021-02-27
     * @param memberLabel
     * @param diseaseList
     */
    public void dealMemberDisease(MemberLabel memberLabel,List<cn.net.yzl.crm.customer.model.mogo.MemberDisease> diseaseList){
        if(CollectionUtils.isEmpty(diseaseList)){
            memberLabel.setMemberDiseaseList(diseaseList);
        }
    }


}
