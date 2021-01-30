package cn.net.yzl.crm.customer.collector.service;

import cn.net.yzl.crm.customer.collector.dao.MemberMapper;
import cn.net.yzl.crm.customer.collector.dao.OrderMDao;
import cn.net.yzl.crm.customer.collector.dao.YixiangcustomerDao;
import cn.net.yzl.crm.customer.collector.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.collector.model.MemberRefOrder;
import cn.net.yzl.crm.customer.collector.model.Yixiangcustomer;
import cn.net.yzl.crm.customer.collector.model.mogo.*;
import cn.net.yzl.crm.customer.collector.utils.MongoDateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLableService
 * @description 初始化标签数据
 * @date: 2021/1/29 8:05 下午
 */
@Service
public class MemberLabelService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMDao orderMDao;

    @Autowired
    private YixiangcustomerDao yixiangcustomerDao;
    @Autowired
    private MemberLabelDao memberLabelDao;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MongoDateHelper.DATE_TIME_PATTERN);

    public boolean syncMember(long times){
        int id = 0;
        int pageSize = 1000;
        long tem = times;
        long lastTime = 0;
        int pageIndex = 0;
        while (true){
            Date date = new Date(tem);
            String timestr =  simpleDateFormat.format(date);
            List<MemberLabel> list = memberMapper.queryAllMemberByPage(timestr,pageSize*pageIndex, pageSize);
            if (!CollectionUtils.isEmpty(list)) {
                System.out.println(String.format("同步顾客数据至MongoDB,当前页数:%s,分页大小:%s,数据大小:%s", pageIndex, pageSize, list.size()));
                //处理顾客编号
                List<String> memberCodes = list.stream().map(MemberLabel::getMemberCard)
                        .collect(Collectors.toList());
                //查询相关病症信息
                List<MemberDisease> memberDiseaseList = memberMapper.queryDiseaseByMemberCodes(memberCodes);
                Map<String, List<MemberDisease>> memberDiseaseListMap = memberDiseaseList.stream()
                        .collect(Collectors.groupingBy(MemberDisease::getMemberCard));
                //查询综合行为
                List<ActionDict> actionDictList = memberMapper.queryActionByMemberCodes(memberCodes);
                Map<String, List<ActionDict>> actionDictListMap = actionDictList.stream()
                        .collect(Collectors.groupingBy(ActionDict::getMemberCard));
                //通过会员卡号查询顾客服用效果
                List<MemberProduct> memberProducts=memberMapper.queryProductByMemberCodes(memberCodes);
                Map<String, List<MemberProduct>> memberProductsMap = memberProducts.stream()
                        .collect(Collectors.groupingBy(MemberProduct::getMemberCard));
                List<MemberOrder> memberRefOrders = orderMDao.queryOrderByMemberCard(memberCodes);
                Map<String, List<MemberOrder>> memberRefOrderMap = memberRefOrders.stream()
                        .collect(Collectors.groupingBy(MemberOrder::getMemberCard));

                //查询进线
                List<Yixiangcustomer> yixiangcustomers= yixiangcustomerDao.queryByMemberCard(memberCodes);
                Map<String, List<Yixiangcustomer>> yixiangcustomerMap = yixiangcustomers.stream()
                        .collect(Collectors.groupingBy(Yixiangcustomer::getMemberCard));
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

                    if(StringUtils.hasText(memberLabel.getFirstBuyProductCod())){
                        Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getFirstBuyProductCod());
                        memberLabel.setFirstBuyProductCodes(new ArrayList<>(set));
                    }
                    if(StringUtils.hasText(memberLabel.getLastBuyProductCode())){
                        Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getLastBuyProductCode());
                        memberLabel.setLastBuyProductCodes(new ArrayList<>(set));
                    }
                    memberLabel.set_id(memberLabel.getMemberCard());
                    String memberCard = memberLabel.getMemberCard();
                    //获取对应会员卡号的顾客的服用效果下信息
                    List<MemberProduct> products = memberProductsMap.get(memberCard);
                    //设置顾客服用效果
                    if(!CollectionUtils.isEmpty(products)){
                        memberLabel.setMemberProductList(products);
                    }
                    //todo 是否有积分、红包、优惠券要从DMC获取
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
                    //处理进线记录
                    List<Yixiangcustomer> yixiangcustomerList= yixiangcustomerMap.get(memberCard);
                    if(!CollectionUtils.isEmpty(yixiangcustomerList)){
                        Set<String> set = new HashSet<>();
                        Date tt = null;
                        for(Yixiangcustomer y:yixiangcustomerList){
                            set.add(y.getProductCode());
                            tt =y.getLastCallTime();
                        }
                        memberLabel.setAdvertProducts(new ArrayList<>(set));
                        memberLabel.setLastCallTime(MongoDateHelper.getMongoDate(tt));
                    }
                    if(lastTime==0||memberLabel.getLastOrderTime().getTime()>lastTime){
                        lastTime = memberLabel.getLastOrderTime().getTime();
                    }
                    if(Objects.nonNull(memberLabel.getLastOrderTime())){
                        memberLabel.setLastOrderTime(MongoDateHelper.getMongoDate(memberLabel.getLastOrderTime()));
                    }
                    memberLabelDao.save(memberLabel);


                }
                if(lastTime==tem){
                    pageIndex++;
                }else{
                    pageIndex=0;
                }
                tem = lastTime;
                if(list.size()<pageSize){
                    break;
                }
                //批量保存至MongoDB
                //memberLabelDao.bathSave(list);
                list.clear();

            } else {
                break;
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

    public boolean syncById(int id){
        int pageSize = 1000;
        int lastId = id;
        while (true){
            List<Yixiangcustomer> yixiangcustomers= yixiangcustomerDao.queryAllByPage(lastId,pageSize);
            if(!CollectionUtils.isEmpty(yixiangcustomers)){
                System.out.println(String.format("同步顾客数据至MongoDB,分页大小:%s,数据大小:%s", pageSize, yixiangcustomers.size()));
                Map<String, List<Yixiangcustomer>> yixiangcustomerMap = yixiangcustomers.stream()
                        .collect(Collectors.groupingBy(Yixiangcustomer::getMemberCard));
                //处理顾客编号
                List<String> memberCodes = yixiangcustomers.stream().map(Yixiangcustomer::getMemberCard)
                        .collect(Collectors.toList());
                List<MemberLabel> list = memberMapper.queryMemberLabelByCodes(memberCodes);
                if (!CollectionUtils.isEmpty(list)) {
                    //查询相关病症信息
                    List<MemberDisease> memberDiseaseList = memberMapper.queryDiseaseByMemberCodes(memberCodes);
                    Map<String, List<MemberDisease>> memberDiseaseListMap = memberDiseaseList.stream()
                            .collect(Collectors.groupingBy(MemberDisease::getMemberCard));
                    //查询综合行为
                    List<ActionDict> actionDictList = memberMapper.queryActionByMemberCodes(memberCodes);
                    Map<String, List<ActionDict>> actionDictListMap = actionDictList.stream()
                            .collect(Collectors.groupingBy(ActionDict::getMemberCard));
                    //通过会员卡号查询顾客服用效果
                    List<MemberProduct> memberProducts=memberMapper.queryProductByMemberCodes(memberCodes);
                    Map<String, List<MemberProduct>> memberProductsMap = memberProducts.stream()
                            .collect(Collectors.groupingBy(MemberProduct::getMemberCard));
                    List<MemberOrder> memberRefOrders = orderMDao.queryOrderByMemberCard(memberCodes);
                    Map<String, List<MemberOrder>> memberRefOrderMap = memberRefOrders.stream()
                            .collect(Collectors.groupingBy(MemberOrder::getMemberCard));
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

                        if(StringUtils.hasText(memberLabel.getFirstBuyProductCod())){
                            Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getFirstBuyProductCod());
                            memberLabel.setFirstBuyProductCodes(new ArrayList<>(set));
                        }
                        if(StringUtils.hasText(memberLabel.getLastBuyProductCode())){
                            Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getLastBuyProductCode());
                            memberLabel.setLastBuyProductCodes(new ArrayList<>(set));
                        }
                        memberLabel.set_id(memberLabel.getMemberCard());
                        String memberCard = memberLabel.getMemberCard();
                        //获取对应会员卡号的顾客的服用效果下信息
                        List<MemberProduct> products = memberProductsMap.get(memberCard);
                        //设置顾客服用效果
                        if(!CollectionUtils.isEmpty(products)){
                            memberLabel.setMemberProductList(products);
                        }
                        //todo 是否有积分、红包、优惠券要从DMC获取
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
                        //处理进线记录
                        List<Yixiangcustomer> yixiangcustomerList= yixiangcustomerMap.get(memberCard);
                        if(!CollectionUtils.isEmpty(yixiangcustomerList)){
                            Set<String> set = new HashSet<>();
                            Date tt = null;
                            for(Yixiangcustomer y:yixiangcustomerList){
                                set.add(y.getProductCode());
                                tt =y.getLastCallTime();
                                if(y.getId()>lastId){
                                    lastId = y.getId();
                                }
                            }
                            memberLabel.setAdvertProducts(new ArrayList<>(set));
                            memberLabel.setLastCallTime(MongoDateHelper.getMongoDate(tt));
                        }
                        if(Objects.nonNull(memberLabel.getLastOrderTime())){
                            memberLabel.setLastOrderTime(MongoDateHelper.getMongoDate(memberLabel.getLastOrderTime()));
                        }
                        memberLabelDao.save(memberLabel);


                    }
                    list.clear();
                }

                if(yixiangcustomers.size()<pageSize){
                    break;
                }
                yixiangcustomers.clear();
            }else{
                break;
            }
        }

        return true;
    }

}
