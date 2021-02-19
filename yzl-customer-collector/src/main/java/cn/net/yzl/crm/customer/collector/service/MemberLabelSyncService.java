package cn.net.yzl.crm.customer.collector.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountResponse;
import cn.net.yzl.activity.model.responseModel.MemberLevelPagesResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.collector.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.collector.client.order.OrderFien;
import cn.net.yzl.crm.customer.collector.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.customer.collector.dao.*;
import cn.net.yzl.crm.customer.collector.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.collector.model.*;
import cn.net.yzl.crm.customer.collector.model.mogo.*;
import cn.net.yzl.crm.customer.collector.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.dto.member.MemberGradeRecordDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelSyncService
 * @description 初始化顾客标签数据
 * @date: 2021/2/1 1:58 下午
 */
@Service
@Slf4j
public class MemberLabelSyncService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMDao orderMDao;

    @Autowired
    private YixiangcustomerDao yixiangcustomerDao;
    @Autowired
    private MemberLabelDao memberLabelDao;

    @Autowired
    private MemberLastcallinDao memberLastcallinDao;

    @Autowired
    private CustomerDistinctDao customerDistinctDao;
    @Autowired
    private MemberAmountRedbagIntegralDao memberAmountRedbagIntegralDao;

    @Autowired
    OrderFien orderFien;
    @Autowired
    WorkOrderClient workOrderClient;

    @Autowired
    ActivityFien activityFien;

    @Autowired
    MemberAmountRedbagIntegralMapper memberAmountRedbagIntegralMapper;

    @Autowired
    MemberGradeRecordDao memberGradeRecordDao;
    /**
     * 每次同步1000条数据
     */
    private final static int pageSize = 1000;

    /**
     * @param id
     * @Author: lichanghong
     * @Description: 初始化顾客标签数据
     * @Date: 2021/2/1 2:01 下午
     * @Return: boolean
     */
    public boolean syncMember(int id) {
        int tempId = id;
        List<MemberLevelPagesResponse> dmcLevelData = null;

        try {
            //获取会员等级，判断是否升级
            PageParam pageParam = new PageParam();
            pageParam.setPageNo(1);
            pageParam.setPageSize(20);
            ComResponse<Page<MemberLevelPagesResponse>> dmcLevelResponse = activityFien.getMemberLevelPages(pageParam);
            Page<MemberLevelPagesResponse> data = dmcLevelResponse.getData();
            if (dmcLevelResponse != null && data != null && CollectionUtil.isNotEmpty(data.getItems())) {
                dmcLevelData = data.getItems();
                //等级倒叙排序
                Collections.sort(dmcLevelData, new Comparator<MemberLevelPagesResponse>() {
                    public int compare(MemberLevelPagesResponse o1, MemberLevelPagesResponse o2) {
                        return o2.getMemberLevelGrade() - o1.getMemberLevelGrade();
                    }
                });
            }
        } catch (Exception e) {
            log.error("初始化顾客标签数据:获取会员等级异常");
        }



        while (true) {
            //customer_distinct表中包含了：3年内有下单 1年内有通话记录 1年内有进线 有意向的客户的编号
            List<CustomerDistinct> customerDistincts = customerDistinctDao.queryAllByIdPage(tempId, pageSize);
            //判断是否获取到数据
            if(!CollectionUtils.isEmpty(customerDistincts)){
                int tempSize = customerDistincts.size();
                log.info("数据同步中... 同步主键:{},数量:{}",tempId,tempSize);
                List<String> memberCodes = new ArrayList<>(tempSize);
                //处理最大编号，封装顾客编号
                for(CustomerDistinct c : customerDistincts){
                    memberCodes.add(c.getMemberCard());
                    if(c.getId()>tempId){
                        tempId = c.getId();
                    }
                }
                //查询MySql数据库中客户的相关信息
                List<MemberLabel> list = memberMapper.queryMemberLabelByCodes(memberCodes);
                if (!CollectionUtils.isEmpty(list)) {
                    //查询相关病症信息
                    List<MemberDisease> memberDiseaseList = memberMapper.queryDiseaseByMemberCodes(memberCodes);
                    Map<String, List<MemberDisease>> memberDiseaseListMap = memberDiseaseList.stream()
                            .collect(Collectors.groupingBy(MemberDisease::getMemberCard));

                    //查询综合行为( from member_action_relation where member_card in)
                    List<ActionDict> actionDictList = memberMapper.queryActionByMemberCodes(memberCodes);
                    Map<String, List<ActionDict>> actionDictListMap = actionDictList.stream()
                            .collect(Collectors.groupingBy(ActionDict::getMemberCard));

                    //通过会员卡号查询顾客服用效果( from member_product_effect where member_card in)
                    List<MemberProduct> memberProducts=memberMapper.queryProductByMemberCodes(memberCodes);
                    Map<String, List<MemberProduct>> memberProductsMap = memberProducts.stream()
                            .collect(Collectors.groupingBy(MemberProduct::getMemberCard));

                    //封装标签数据
                    for (MemberLabel memberLabel : list) {
                        //订单接口:通过会员卡号查询订单表
                        ComResponse<List<MemberOrderObject>> querymemberorder = null;
                        Map<String, List<MemberOrder>> memberRefOrderMap = new HashMap<>();
                        try {
                            querymemberorder = orderFien.querymemberorder(Arrays.asList(memberLabel.getMemberCard()));
                            if (querymemberorder != null && CollectionUtil.isNotEmpty(querymemberorder.getData())) {
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
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("获取会员订单信息失败！");
                        }



                        //是否有QQ
                        if (StringUtils.hasText(memberLabel.getQq())) {
                            memberLabel.setHasQQ(true);
                        } else {
                            memberLabel.setHasQQ(false);
                        }
                        //是否有邮箱
                        if (StringUtils.hasText(memberLabel.getEmail())) {
                            memberLabel.setHasEmail(true);
                        } else {
                            memberLabel.setHasEmail(false);
                        }
                        //是否有微信
                        if (StringUtils.hasText(memberLabel.getWechat())) {
                            memberLabel.setHasWechat(true);
                        } else {
                            memberLabel.setHasWechat(false);
                        }
                        //生日月份
                        if (StringUtils.hasText(memberLabel.getBirthday())) {
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
                        if(StringUtils.hasText(memberLabel.getFirstBuyProductCod())){
                            Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getFirstBuyProductCod());
                            memberLabel.setFirstBuyProductCodes(new ArrayList<>(set));
                        }

                        //最后一次购买商品
                        if(StringUtils.hasText(memberLabel.getLastBuyProductCode())){
                            Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getLastBuyProductCode());
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
                        cn.net.yzl.crm.customer.model.MemberAmountRedbagIntegral memberAmountRedbagIntegral = memberAmountRedbagIntegralMapper.selectByMemberCard(memberCard);
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

                        //获取会员信息
                        //Member member1 = selectMemberByCard(memberCard);
                        //判断是否升级
                        if (CollectionUtil.isNotEmpty(dmcLevelData)) {
                            //一次性预存款 一次性消费满多少 一年累计消费满
                            ComResponse<List<MemberTotal>> memberTotalResponse = orderFien.queryMemberTotal(Arrays.asList(memberCard));
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
                        List<MemberDisease> diseaseList = memberDiseaseListMap.get(memberCard);
                        if(CollectionUtils.isEmpty(diseaseList)){
                            memberLabel.setMemberDiseaseList(diseaseList);
                        }
                        Map<String, Integer> activityMap = new HashMap<>();
                        //处理订单
                        List<MemberOrder> mros = memberRefOrderMap.get(memberCard);
                        if(!CollectionUtils.isEmpty(mros)){
                            for(MemberOrder memberOrder:mros){
                                if (org.apache.commons.lang3.StringUtils.isEmpty(memberOrder.getActivityCode())) {
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
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(lastDialTime)){
                                memberLabel.setLastCallTime(MongoDateHelper.getMongoDate(DateUtil.parse(lastDialTime)));
                            }
                            //设置最后一次进线时间
                            if(org.apache.commons.lang3.StringUtils.isNotEmpty(lastCallInTime)){
                                memberLabel.setLastCallInTime(MongoDateHelper.getMongoDate(DateUtil.parse(lastCallInTime)));
                            }
                        }

                        memberLabelDao.save(memberLabel);
                    }
                    list.clear();
                }
                if (tempSize < pageSize) {
                    break;
                }
                customerDistincts.clear();
            }else{
            log.info("数据同步完成");
            }
        }
        return true;
    }

    private static Integer getMonth(String date) {
        String[] str = date.split("-");
        if(str.length>2){
            return Integer.parseInt(str[1]);
        }
        return 0;
    }
}
