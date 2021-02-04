package cn.net.yzl.crm.customer.collector.service;

import cn.net.yzl.crm.customer.collector.dao.*;
import cn.net.yzl.crm.customer.collector.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.collector.model.CustomerDistinct;
import cn.net.yzl.crm.customer.collector.model.MemberLastcallin;
import cn.net.yzl.crm.customer.collector.model.Yixiangcustomer;
import cn.net.yzl.crm.customer.collector.model.mogo.*;
import cn.net.yzl.crm.customer.collector.utils.MongoDateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

                    ////通过会员卡号查询订单表( from order_m where member_card in)
                    List<MemberOrder> memberRefOrders = orderMDao.queryOrderByMemberCard(memberCodes);
                    Map<String, List<MemberOrder>> memberRefOrderMap = memberRefOrders.stream()
                            .collect(Collectors.groupingBy(MemberOrder::getMemberCard));

                    //查询进线(意向客户 from yixiangcustomer where member_card in)
                    List<Yixiangcustomer> yixiangcustomers= yixiangcustomerDao.queryByMemberCard(memberCodes);
                    Map<String, List<Yixiangcustomer>> yixiangcustomerMap = yixiangcustomers.stream()
                            .collect(Collectors.groupingBy(Yixiangcustomer::getMemberCard));

                    //查询最后一次通话记录( from member_lastcallin where member_card_no in)
                    List<MemberLastcallin> lastcallinList = memberLastcallinDao.queryCallInByMemberCard(memberCodes);
                    Map<String, List<MemberLastcallin>> lastcallinListMap = lastcallinList.stream()
                            .collect(Collectors.groupingBy(MemberLastcallin::getMemberCard));
                    //封装标签数据
                    for (MemberLabel memberLabel : list) {
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




                        //TODO 是否有积分、红包、优惠券要从DMC获取



                        //获取当前顾客的综合行为
                        List<ActionDict> actionDicts =actionDictListMap.get(memberCard);
                        //设置当前顾客的综合行为
                        if(!CollectionUtils.isEmpty(actionDicts)){
                            Map<Integer,List<ActionDict>> temp=actionDicts.stream().filter(s->s.getType()!=null).collect(Collectors.groupingBy(ActionDict::getType));
                            //方便接电话时间
                            if(CollectionUtils.isEmpty(temp.get(1))){
                                memberLabel.setPhoneDictList(temp.get(1));
                            }
                            //2性格偏好
                            if(CollectionUtils.isEmpty(temp.get(2))){
                                memberLabel.setMemberCharacterList(temp.get(2));
                            }
                            //3响应时间
                            if(CollectionUtils.isEmpty(temp.get(3))){
                                memberLabel.setMemberResponseTimeList(temp.get(3));
                            }
                            //综合行为
                            if(CollectionUtils.isEmpty(temp.get(5))){
                                memberLabel.setComprehensiveBehaviorList(temp.get(5));
                            }
                            //下单行为
                            if(CollectionUtils.isEmpty(temp.get(6))){
                                memberLabel.setOrderBehaviorList(temp.get(6));
                            }
                        }
                        List<MemberDisease> diseaseList = memberDiseaseListMap.get(memberCard);
                        if(CollectionUtils.isEmpty(diseaseList)){
                            memberLabel.setMemberDiseaseList(diseaseList);
                        }
                        //处理订单
                        List<MemberOrder> mros = memberRefOrderMap.get(memberCard);
                        if(!CollectionUtils.isEmpty(mros)){
                            for(MemberOrder memberOrder:mros){
                                //todo 要从DMC获取活动类型
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
                        //最后一次拨打时间
                        Date lastCallTime = null;
                        //最后一次进线时间
                        Date lastCallInTime = null;
                        //处理进线记录
                        List<Yixiangcustomer> yixiangcustomerList= yixiangcustomerMap.get(memberCard);
                        if(!CollectionUtils.isEmpty(yixiangcustomerList)){
                            Set<String> set = new HashSet<>();
                            for(Yixiangcustomer y:yixiangcustomerList){
                                set.add(y.getProductCode());
                                //最后一次拨打时间
                                if(lastCallTime==null && y.getLastCallTime()!=null){
                                    lastCallTime = y.getLastCallTime();
                                }
                                if(lastCallTime!=null && y.getLastCallTime()!=null){
                                    if(y.getLastCallTime().after(lastCallTime)){
                                        lastCallTime = y.getLastCallTime();
                                    }
                                }
                                //最后一次进线时间
                                if(lastCallInTime==null && y.getLastCallInTime()!=null){
                                    lastCallInTime = y.getLastCallInTime();
                                }
                                if(lastCallInTime!=null && y.getLastCallInTime()!=null){
                                    if(y.getLastCallInTime().after(lastCallInTime)){
                                        lastCallInTime = y.getLastCallInTime();
                                    }
                                }
                            }
                            //最后一次进线广告关联的商品编号
                            memberLabel.setAdvertProducts(new ArrayList<>(set));
                        }
                        //最后一次拨打时间
                        if(lastCallTime!=null){
                            memberLabel.setLastCallTime(MongoDateHelper.getMongoDate(lastCallTime));
                        }
                        //设置最后一次进线时间
                        if(lastCallInTime!=null){
                            memberLabel.setLastCallInTime(MongoDateHelper.getMongoDate(lastCallInTime));
                        }

                        //处理最后一次通话记录
                        List<MemberLastcallin> lastcallins =lastcallinListMap.get(memberCard);
                        if(!CollectionUtils.isEmpty(lastcallins)){
                            for(MemberLastcallin in :lastcallins){
                                //最后一次拨打时间
                                if(lastCallTime==null && in.getLastCallTime()!=null){
                                    lastCallTime = in.getLastCallTime();
                                }
                                if(lastCallTime!=null && in.getLastCallTime()!=null){
                                    if(in.getLastCallTime().after(lastCallTime)){
                                        lastCallTime = in.getLastCallTime();
                                    }
                                }
                                //最后一次进线时间
                                if(lastCallInTime==null && in.getLastCallInTime()!=null){
                                    lastCallInTime = in.getLastCallInTime();
                                }
                                if(lastCallInTime!=null && in.getLastCallInTime()!=null){
                                    if(in.getLastCallInTime().after(lastCallInTime)){
                                        lastCallInTime = in.getLastCallInTime();
                                    }
                                }
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
