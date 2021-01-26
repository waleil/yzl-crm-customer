package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.*;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelDao
 * @description 会员宽表数据
 * @date: 2021/1/25 5:36 下午
 */
@Component
public class MemberLabelDao extends MongoBaseDao<MemberLabel> {
    private static String COLLECTION_NAME = "member_label";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected Class<MemberLabel> getEntityClass() {
        return MemberLabel.class;
    }

    /**
     * @param memberCrowdGroup
     * @Author: lichanghong
     * @Description: 规则试算
     * @Date: 2021/1/26 11:42 上午
     * @Return: java.lang.Integer
     */
    public Integer memberCrowdGroupTrial(member_crowd_group memberCrowdGroup) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        //判断性别
        if (memberCrowdGroup.getSex() != null) {
            if (memberCrowdGroup.getSex() == 1) {
                criteria.and("sex").is(1);
            } else {
                criteria.and("sex").is(0);
            }
        }
        //邮箱
        if (memberCrowdGroup.getEmail() != null) {
            if (memberCrowdGroup.getEmail() == 1) {
                criteria.and("hasEmail").is(true);
            } else {
                criteria.and("hasEmail").is(false);
            }
        }
        //QQ
        if (memberCrowdGroup.getQq() != null) {
            if (memberCrowdGroup.getQq() == 1) {
                criteria.and("hasQQ").is(true);
            } else {
                criteria.and("hasQQ").is(false);
            }
        }
        //微信
        if (memberCrowdGroup.getWechat() != null) {
            if (memberCrowdGroup.getWechat() == 1) {
                criteria.and("hasWechat").is(true);
            } else {
                criteria.and("hasWechat").is(false);
            }
        }
        //是否拥有储值金额
        if (memberCrowdGroup.getRecharge() != null) {
            if (memberCrowdGroup.getRecharge() == 1) {
                criteria.and("hasMoney").is(true);
            } else {
                criteria.and("hasMoney").is(false);
            }
        }
        //是否拥有优惠券
        if (memberCrowdGroup.getTicket() != null) {
            if (memberCrowdGroup.getTicket() == 1) {
                criteria.and("hasTicket").is(true);
            } else {
                criteria.and("hasTicket").is(false);
            }
        }
        //是否拥有红包
        if (memberCrowdGroup.getRed_bag() != null) {
            if (memberCrowdGroup.getRed_bag() == 1) {
                criteria.and("hasTedBag").is(true);
            } else {
                criteria.and("hasTedBag").is(false);
            }
        }
        //是否为会员
        if (memberCrowdGroup.getVip() != null) {
            if (memberCrowdGroup.getRecharge() == 1) {
                criteria.and("vipFlag").is(true);
            } else {
                criteria.and("vipFlag").is(false);
            }
        }
        //座席偏好
        if (memberCrowdGroup.getRecharge() != null) {
//            if (memberCrowdGroup.getRecharge() == 1) {
//                criteria.and("hasMoney").is(true);
//            } else {
//                criteria.and("hasMoney").is(false);
//            }
        }
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAge())) {
            List<Member_Age> list = memberCrowdGroup.getAge();
            Map<Integer, List<Member_Age>> temps = list.stream().collect(Collectors.groupingBy(Member_Age::getInclude));
            List<Member_Age> in = temps.get(1);
            List<Member_Age> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                Criteria [] andArray = new Criteria[size];
               for(int i=0;i<size;i++){
                   Criteria c = new Criteria();
                    Member_Age age =in.get(i);
                   andArray[i]= c.andOperator(Criteria.where("age").gte(age.getStart_age()),Criteria.where("age").lt(age.getEnd_age()));
               }
                criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                Criteria [] exArray = new Criteria[size];
                for(int i=0;i<size;i++){
                    Criteria c = new Criteria();
                    Member_Age age =ex.get(i);
                    exArray[i]= c.andOperator(Criteria.where("age").gte(age.getStart_age()),Criteria.where("age").lt(age.getEnd_age()));
                }
                criteria.norOperator(exArray);
            }
        }
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAreas())) {
            List<crowd_area> list = memberCrowdGroup.getAreas();
            //进行分组
            Map<Integer, List<crowd_area>> listMap = list.stream().collect(Collectors.groupingBy(crowd_area::getLevel));
            //省
            List<crowd_area> provinceList = listMap.get(1);
            if (!CollectionUtils.isEmpty(provinceList)) {
                Map<Integer, List<crowd_area>> temps = provinceList.stream().collect(Collectors.groupingBy(crowd_area::getInclude));
                List<crowd_area> in = temps.get(1);
                if(!CollectionUtils.isEmpty(in)) {
                    int size = in.size();
                    Criteria [] andArray = new Criteria[size];
                    for(int i=0;i<size;i++){
                        crowd_area c = in.get(i);
                        andArray[i] =Criteria.where("provinceCode").is(c.getId());
                    }
                    criteria.andOperator(andArray);
                }
                List<crowd_area> ex = temps.get(0);
                if(!CollectionUtils.isEmpty(ex)) {
                    int size = ex.size();
                    Criteria [] exArray = new Criteria[size];
                    for(int i=0;i<size;i++){
                        crowd_area c = ex.get(i);
                        exArray[i] =Criteria.where("provinceCode").is(c.getId());
                    }
                    criteria.norOperator(exArray);
                }
            }
            //市
            List<crowd_area> cityList = listMap.get(2);
            if (!CollectionUtils.isEmpty(cityList)) {
                Map<Integer, List<crowd_area>> temps = cityList.stream().collect(Collectors.groupingBy(crowd_area::getInclude));
                List<crowd_area> in = temps.get(1);
                if(!CollectionUtils.isEmpty(in)){
                    int size = in.size();
                    Criteria [] andArray = new Criteria[size];
                    for(int i=0;i<size;i++){
                        crowd_area c = in.get(i);
                        andArray[i] =Criteria.where("cityCode").is(c.getId());
                    }
                    criteria.andOperator(andArray);
                }

                List<crowd_area> ex = temps.get(0);
                if(!CollectionUtils.isEmpty(ex)) {
                    int size = ex.size();
                    Criteria [] exArray = new Criteria[size];
                    for(int i=0;i<size;i++){
                        crowd_area c = ex.get(i);
                        exArray[i] =Criteria.where("cityCode").is(c.getId());
                    }
                    criteria.norOperator(exArray);
                }
            }
        }
        //级别
        if(!CollectionUtils.isEmpty(memberCrowdGroup.getMember_grade())){
            List<crowd_base_value> member_grade = memberCrowdGroup.getMember_grade();
            Map<Integer, List<crowd_base_value>> temps = member_grade.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if(!CollectionUtils.isEmpty(in)){
                int size = in.size();
                Criteria [] andArray = new Criteria[size];
                for(int i=0;i<size;i++){
                    crowd_base_value c = in.get(i);
                    andArray[i] =Criteria.where("mGradeCode").is(c.getId());
                }
                criteria.andOperator(andArray);
            }

            if(!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                Criteria [] exArray = new Criteria[size];
                for(int i=0;i<size;i++){
                    crowd_base_value c = ex.get(i);
                    exArray[i] =Criteria.where("mGradeCode").is(c.getId());
                }
            }
        }
        //活跃度
        if(!CollectionUtils.isEmpty(memberCrowdGroup.getActive_degree())){
            List<crowd_activity_degree> degree = memberCrowdGroup.getActive_degree();
            Map<Integer, List<crowd_activity_degree>> temps = degree.stream().collect(Collectors.groupingBy(crowd_activity_degree::getInclude));
            List<crowd_activity_degree> in = temps.get(1);
            List<crowd_activity_degree> ex = temps.get(0);
            if(!CollectionUtils.isEmpty(in)){
                int size = in.size();
                Criteria [] andArray = new Criteria[size];
                for(int i=0;i<size;i++){
                    crowd_activity_degree c = in.get(i);
                    andArray[i] =Criteria.where("activity").is(c.getId());
                }
                criteria.andOperator(andArray);
            }

            if(!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                Criteria [] exArray = new Criteria[size];
                for(int i=0;i<size;i++){
                    crowd_activity_degree c = ex.get(i);
                    exArray[i] =Criteria.where("activity").is(c.getId());
                }
                criteria.norOperator(exArray);
            }
        }
        //首单下单时间>=
        if(memberCrowdGroup.getFirst_order_to_days()!=null){
            int days = memberCrowdGroup.getFirst_order_to_days();
            if(days>10000){
                return 0;
            }
            Date date = getPastDate(days);
            criteria.and("firstOrderTime").lt(MongoDateHelper.getMongoDate(date));
        }
        //首次订单金额>=
        if(memberCrowdGroup.getFirst_order_am()!=null){
            int am = BigDecimal.valueOf(memberCrowdGroup.getFirst_order_am()*100).intValue();
            criteria.and("firstOrderAm").gte(am);
        }
        //最后一次下单时间>=
        if(memberCrowdGroup.getLast_order_to_days()!=null){
            int days = memberCrowdGroup.getLast_order_to_days();
            if(days>10000){
                return 0;
            }
            Date date = getPastDate(days);
            criteria.and("lastOrderTime").lt(MongoDateHelper.getMongoDate(date));
        }
        //最后一次下单时间>=
        if(memberCrowdGroup.getSign_date_to_days()!=null){
            int days = memberCrowdGroup.getSign_date_to_days();
            if(days>10000){
                return 0;
            }
            Date date = getPastDate(days);
            criteria.and("lastSignTime").lt(MongoDateHelper.getMongoDate(date));
        }
        //方便接电话时间

        query.addCriteria(criteria);
        return (int) mongoTemplate.count(query, MemberLabel.class, COLLECTION_NAME);
    }

    /**
     * @Author: lichanghong
     * @Description: 获取之前几天的日期
     * @Date: 2021/1/26 11:06 下午
     * @param past 天数
     * @Return: java.util.Date
     */
    private static Date getPastDate(int past) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return today;
    }
}
