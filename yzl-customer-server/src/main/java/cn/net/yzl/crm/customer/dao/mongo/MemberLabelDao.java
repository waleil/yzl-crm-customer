package cn.net.yzl.crm.customer.dao.mongo;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.label.MemberLabelDto;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.*;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.date.DealDateUtil;
import cn.net.yzl.crm.customer.utils.mongo.PageUtil;
import cn.net.yzl.logger.annotate.SysAccessLog;
import cn.net.yzl.logger.enums.DefaultDataEnums;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
@Slf4j
public class MemberLabelDao extends MongoBaseDao<MemberLabel> {
    private static String COLLECTION_NAME = "member_label";
    private static String group_ref_member= "group_ref_member";
    private static String MEMBER_CROWD_GROUP= "member_crowd_group";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected Class<MemberLabel> getEntityClass() {
        return MemberLabel.class;
    }

    /**
     * @param memberCrowdGroup
     * @Author: lichanghong
     * @Description: 员工圈选
     * @Date: 2021/1/26 11:42 上午
     * @Return
     */
/*    public List<MemberLabel> memberCrowdGroupRun(member_crowd_group memberCrowdGroup){
        Query query = initQuery(memberCrowdGroup);
        query.fields().include("memberCard").include("memberName").exclude("_id");
        return mongoTemplate.find(query, MemberLabel.class, COLLECTION_NAME);
    }*/

    public Page<MemberLabel> memberCrowdGroupRunUsePage(Integer pageNo ,Integer pageSize,Query query) {
        Long totalCount = mongoTemplate.count(query, MemberLabel.class);
        List<MemberLabel> memberLabels = null;
        if (pageNo > 0 && pageSize > 0) {
            query.skip(getStartIndex(pageNo,pageSize));
            query.limit(pageSize);
        }
        if (totalCount > 0) {
            memberLabels = mongoTemplate.find(query, MemberLabel.class, COLLECTION_NAME);
        }
        query.skip(0);
        query.limit(0);

        return PageUtil.resultAssembler(memberLabels, pageNo, pageSize, totalCount.intValue());
    }
    public Page<MemberLabelDto> memberCrowdGroupRunUsePage(Integer pageNo , Integer pageSize, Query query, Long totalCount) {
        if (totalCount == null) {
            totalCount = mongoTemplate.count(query, MemberLabel.class);
        }
        List<MemberLabelDto> memberLabels = null;
        if (pageNo > 0 && pageSize > 0) {
            query.skip(getStartIndex(pageNo,pageSize));
            query.limit(pageSize);
        }
        if (totalCount > 0) {
            memberLabels = mongoTemplate.find(query, MemberLabelDto.class, COLLECTION_NAME);
        }
        query.skip(0);
        query.limit(0);

        return PageUtil.resultAssembler(memberLabels, pageNo, pageSize, totalCount.intValue());
    }




    /**
     * 分页开始位置
     * @param pageNo
     * @param pageSize
     * @return
     */
    private Integer getStartIndex(int pageNo ,int pageSize) {
        pageNo=pageNo < 1 ? 1 : pageNo;
        Integer startIndex=(pageNo - 1) * pageSize;
        return startIndex == null ? 0 : startIndex;
    }



    public <T> Boolean insertAll(List<GroupRefMember> list,String collectionName) {
        BulkOperations bulkOperations= mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,GroupRefMember.class,collectionName);
        bulkOperations.insert(list).execute();
        return true;
    }

    public DeleteResult remove(Query query, Class<?> entityClass) {
        return mongoTemplate.remove(query, GroupRefMember.class);
    }

    public UpdateResult updateFirst(Query query, UpdateDefinition update, Class<?> entityClass) {
        return mongoTemplate.updateFirst(query, update,entityClass);
    }
    /**
     * @Author: lichanghong
     * @Description: 根据分组编号查询关联的顾客
     * @Date: 2021/1/28 12:54 上午
     * @param groupId
     * @Return: java.util.List<cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember>
     */
    public List<GroupRefMember> queryMembersByGroupId(String groupId){

        Query query = new Query();
        query.addCriteria(Criteria.where("groupId").is(groupId));
        List<GroupRefMember> list = mongoTemplate.find(query,GroupRefMember.class,group_ref_member);
        return list;
    }
    @SysAccessLog(logKeyParamName = "query",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public long count(Query query, Class<?> entityClass) {
        Long totalCount = mongoTemplate.count(query, entityClass);
        return totalCount;
    }
    /**
     * @Author: lichanghong
     * @Description: 根据顾客编号查询顾客所属圈选群
     * @Date: 2021/1/28 12:59 上午
     * @param memberCard
     * @Return: java.lang.String
     */
    public String queryGroupIdByMemberCard(String memberCard){

        Query query = new Query();
        query.addCriteria(Criteria.where("memberCard").is(memberCard));
        query.fields().exclude("_id").include("groupId");
        GroupRefMember member = mongoTemplate.findOne(query,GroupRefMember.class,group_ref_member);
        if(member!=null){
            return member.getGroupId();
        }else{
            return null;
        }
    }
    /**
     * @param query
     * @Author: lichanghong
     * @Description: 规则试算
     * @Date: 2021/1/26 11:42 上午
     * @Return: java.lang.Integer
     */
    @SysAccessLog(logKeyParamName = "query",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public long memberCrowdGroupTrial(Query query) {
        return mongoTemplate.count(query, MemberLabel.class, COLLECTION_NAME);
    }

    /**
     * @param memberCrowdGroup
     * @Author: lichanghong
     * @Description: 初始化MongoDB查询条件
     * @Date: 2021/1/27 10:06 上午
     * @Return: org.springframework.data.mongodb.core.query.Query
     */
    public  Query initQuery(member_crowd_group memberCrowdGroup) {
        Query query = new Query();
        //and查询
        List<Criteria> and = new ArrayList<>();
        //not查询
        List<Criteria> not = new ArrayList<>();
        //判断性别
        if (memberCrowdGroup.getSex() != null) {
            if (memberCrowdGroup.getSex() == 0 || memberCrowdGroup.getSex() == 1) {
                and.add(Criteria.where("sex").is(memberCrowdGroup.getSex()));
            } else {
                and.add(Criteria.where("sex").is(2));
            }
        }
        //邮箱
        if (memberCrowdGroup.getEmail() != null) {
            if (memberCrowdGroup.getEmail() == 1) {
                and.add(Criteria.where("hasEmail").is(true));
            } else {
                and.add(Criteria.where("hasEmail").is(false));
            }
        }
        //QQ
        if (memberCrowdGroup.getQq() != null) {
            if (memberCrowdGroup.getQq() == 1) {
                and.add(Criteria.where("hasQQ").is(true));
            } else {
                and.add(Criteria.where("hasQQ").is(false));
            }
        }
        //微信
        if (memberCrowdGroup.getWechat() != null) {
            if (memberCrowdGroup.getWechat() == 1) {
                and.add(Criteria.where("hasWechat").is(true));
            } else {
                and.add(Criteria.where("hasWechat").is(false));
            }
        }
        //是否拥有储值金额
        if (memberCrowdGroup.getRecharge() != null) {
            if (memberCrowdGroup.getRecharge() == 1) {
                and.add(Criteria.where("hasMoney").is(true));
            } else {
                and.add(Criteria.where("hasMoney").is(false));
            }
        }
        //是否拥有优惠券
        if (memberCrowdGroup.getTicket() != null) {
            if (memberCrowdGroup.getTicket() == 1) {
                and.add(Criteria.where("hasTicket").is(true));
            } else {
                and.add(Criteria.where("hasTicket").is(false));
            }
        }
        //是否拥有红包
        if (memberCrowdGroup.getRed_bag() != null) {
            if (memberCrowdGroup.getRed_bag() == 1) {
                and.add(Criteria.where("hasTedBag").is(true));
            } else {
                and.add(Criteria.where("hasTedBag").is(false));
            }
        }

        //是否有积分
        if (memberCrowdGroup.getIntegral() != null) {
            if (memberCrowdGroup.getIntegral() == 1) {
                and.add(Criteria.where("hasIntegral").is(true));
            } else {
                and.add(Criteria.where("hasIntegral").is(false));
            }
        }
        //是否为会员
        if (memberCrowdGroup.getVip() != null) {
            if (memberCrowdGroup.getVip() == 1) {
                and.add(Criteria.where("vipFlag").is(true));
            } else {
                and.add(Criteria.where("vipFlag").is(false));
            }
        }
        //座席偏好--暂时不做
       //顾客年龄
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAge())) {
            List<Member_Age> list = memberCrowdGroup.getAge();
            Map<Integer, List<Member_Age>> temps = list.stream().collect(Collectors.groupingBy(Member_Age::getInclude));
            List<Member_Age> in = temps.get(1);
            List<Member_Age> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    Criteria c = new Criteria();
                    Member_Age age = in.get(i);
                    //   >=     getStart_age    < getEnd_age
                    andArray[i] = c.andOperator(Criteria.where("age").gte(age.getStart_age()), Criteria.where("age").lt(age.getEnd_age()));
                    and.add(c);
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                for (int i = 0; i < size; i++) {
                    Criteria c = new Criteria();
                    Member_Age age = ex.get(i);
                 c.andOperator(Criteria.where("age").gte(age.getStart_age()), Criteria.where("age").lt(age.getEnd_age()));
                    not.add(c);
                }
            }
        }
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAreas())) {
            List<crowd_area> areas = memberCrowdGroup.getAreas();
            //进行分组
            Map<Integer, List<crowd_area>> listMap = areas.stream().collect(Collectors.groupingBy(crowd_area::getLevel));
            //省
            List<crowd_area> provinceList = listMap.get(1);
            if (!CollectionUtils.isEmpty(provinceList)) {
                Map<Integer, List<crowd_area>> temps = provinceList.stream().collect(Collectors.groupingBy(crowd_area::getInclude));
                List<crowd_area> in = temps.get(1);
                if (!CollectionUtils.isEmpty(in)) {
                    int size = in.size();
                    for (int i = 0; i < size; i++) {
                        crowd_area c = in.get(i);
                       and.add(Criteria.where("provinceCode").is(Integer.parseInt(c.getId())));
                    }
                }
                List<crowd_area> ex = temps.get(0);
                if (!CollectionUtils.isEmpty(ex)) {
                    int size = ex.size();
                    for (int i = 0; i < size; i++) {
                        crowd_area c = ex.get(i);
                      not.add(Criteria.where("provinceCode").is(Integer.parseInt(c.getId())));
                    }
                }
            }
            //市
            List<crowd_area> cityList = listMap.get(2);
            if (!CollectionUtils.isEmpty(cityList)) {
                Map<Integer, List<crowd_area>> temps = cityList.stream().collect(Collectors.groupingBy(crowd_area::getInclude));
                List<crowd_area> in = temps.get(1);
                if (!CollectionUtils.isEmpty(in)) {
                    int size = in.size();
                  //  Criteria[] andArray = new Criteria[size];
                    for (int i = 0; i < size; i++) {
                        crowd_area c = in.get(i);
                        and.add(Criteria.where("cityCode").is(Integer.parseInt(c.getId())));
                    }
                  //  criteria.andOperator(andArray);
                }

                List<crowd_area> ex = temps.get(0);
                if (!CollectionUtils.isEmpty(ex)) {
                    int size = ex.size();
                  //  Criteria[] exArray = new Criteria[size];
                    for (int i = 0; i < size; i++) {
                        crowd_area c = ex.get(i);
                        not.add(Criteria.where("cityCode").is(Integer.parseInt(c.getId())));
                    }
                  //  criteria.norOperator(exArray);
                }
            }
        }
        //级别
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMember_grade())) {
            List<crowd_base_value> memberGrades = memberCrowdGroup.getMember_grade();
            Map<Integer, List<crowd_base_value>> temps = memberGrades.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("mGradeId").is(Integer.parseInt(c.getId())));
                    }
                }
                //criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
             //   Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("mGradeId").is(Integer.parseInt(c.getId())));
                    }
                }
            }
        }
        //类别
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMember_type())) {
            List<crowd_member_type> memberTypes = memberCrowdGroup.getMember_type();
            Map<Integer, List<crowd_member_type>> temps = memberTypes.stream().collect(Collectors.groupingBy(crowd_member_type::getInclude));
            List<crowd_member_type> in = temps.get(1);
            List<crowd_member_type> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                for (int i = 0; i < size; i++) {
                    crowd_member_type c = in.get(i);
                    and.add(Criteria.where("memberType").is(c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                for (int i = 0; i < size; i++) {
                    crowd_member_type c = ex.get(i);
                    not.add(Criteria.where("memberType").is(c.getId()));
                }
            }
        }

        //活跃度
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActive_degree())) {
            List<crowd_activity_degree> activeDegrees = memberCrowdGroup.getActive_degree();
            Map<Integer, List<crowd_activity_degree>> temps = activeDegrees.stream().collect(Collectors.groupingBy(crowd_activity_degree::getInclude));
            List<crowd_activity_degree> in = temps.get(1);
            List<crowd_activity_degree> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_activity_degree c = in.get(i);
                    and.add(Criteria.where("activity").is(c.getId()));
                }
              //  criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_activity_degree c = ex.get(i);
                    not.add(Criteria.where("activity").is(c.getId()));
                }
               // criteria.norOperator(exArray);
            }
        }
        //首单下单时间>=
        if (memberCrowdGroup.getFirst_order_to_days() != null) {
//            int days = memberCrowdGroup.getFirst_order_to_days();
            DayParam days = memberCrowdGroup.getFirst_order_to_days();
            if (days.getDay() != null && StringUtils.isNotEmpty(days.getSymbol())) {
                if (days.getDay() > 10000) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "首单下单时间不能大于10000");
                }
                Criteria where = dayParamCondition(days, "firstOrderTime");
                if (where != null) {
                    and.add(where);
                }
            }
        }

        //首次订单金额>=
        if (memberCrowdGroup.getFirst_order_am() != null) {
            AmountParam firstOrderAm = memberCrowdGroup.getFirst_order_am();
            if (firstOrderAm.getAmount() != null && StringUtils.isNotEmpty(firstOrderAm.getSymbol())) {
                Double amount = firstOrderAm.getAmount();
                String symbol = firstOrderAm.getSymbol();
                int am = BigDecimal.valueOf(amount * 100).intValue();
                if (">=".equals(symbol)){
                    and.add(Criteria.where("firstOrderAm").gte(am));
                }else if (">".equals(symbol)){
                    and.add(Criteria.where("firstOrderAm").gt(am));
                }else if ("=".equals(symbol)){
                    and.add(Criteria.where("firstOrderAm").is(am));
                }else if ("<=".equals(symbol)){
                    and.add(Criteria.where("firstOrderAm").lte(am));
                }else if ("<".equals(symbol)){
                    and.add(Criteria.where("firstOrderAm").lt(am));
                }
            }


        }
        //最后一次下单时间>=
        if (memberCrowdGroup.getLast_order_to_days() != null) {
            DayParam days = memberCrowdGroup.getLast_order_to_days();
            if (days.getDay() != null && StringUtils.isNotEmpty(days.getSymbol())) {
                if (days.getDay() > 10000) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "最后一次下单时间不能大于10000");
                }
                Criteria where = dayParamCondition(days, "lastOrderTime");
                if (where != null) {
                    and.add(where);
                }
            }
        }
        //生日月份
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMember_month())) {
            List<crowd_base_value> memberMonth = memberCrowdGroup.getMember_month();
            Map<Integer, List<crowd_base_value>> temps = memberMonth.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("memberMonth").is(Integer.parseInt(c.getId())));
                    }
                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                //Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberMonth").is(Integer.parseInt(c.getId())));
                    }
                }
               // criteria.norOperator(exArray);
            }
        }
            // todo 待做
            if(!CollectionUtils.isEmpty(memberCrowdGroup.getMember_type())){

            }
        //获客媒体
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMediaList())) {
            List<crowd_media> medias = memberCrowdGroup.getMediaList();
            Map<Integer, List<crowd_media>> temps = medias.stream().collect(Collectors.groupingBy(crowd_media::getInclude));
            List<crowd_media> in = temps.get(1);
            List<crowd_media> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_media c = in.get(i);
                    and.add(Criteria.where("mediaId").is(c.getMedia_id()));
                }
                //criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                //Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_media c = ex.get(i);
                    not.add(Criteria.where("mediaId").is(c.getMedia_id()));
                }
               // criteria.norOperator(exArray);
            }
        }
        //广告
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAdvers())) {
            List<crowd_adver> advers = memberCrowdGroup.getAdvers();
            Map<Integer, List<crowd_adver>> temps = advers.stream().collect(Collectors.groupingBy(crowd_adver::getInclude));
            List<crowd_adver> in = temps.get(1);
            List<crowd_adver> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_adver c = in.get(i);
                    and.add(Criteria.where("adverCode").is(c.getId()));
                }
                //criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_adver c = ex.get(i);
                    not.add(Criteria.where("adverCode").is(c.getId()));
                }
              //  criteria.norOperator(exArray);
            }
        }
        //签收时间时间>=
        if (memberCrowdGroup.getSign_date_to_days() != null) {
            DayParam days = memberCrowdGroup.getSign_date_to_days();
            if (days.getDay() != null && StringUtils.isNotEmpty(days.getSymbol())) {
                if (days.getDay() > 10000) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "最后一次签收时间不能大于10000");
                }
                Criteria where = dayParamCondition(days, "lastSignTime");
                if (where != null) {
                    and.add(where);
                }
            }
        }
        //方便接电话时间
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getPhone_time())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getPhone_time();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("phoneDictList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("phoneDictList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //性格偏好
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getNature())) {
            List<crowd_action> natures = memberCrowdGroup.getNature();
            Map<Integer, List<crowd_action>> temps = natures.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("memberCharacterList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("memberCharacterList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //响应时间
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getResponse_time())) {
            List<crowd_action> responseTimes = memberCrowdGroup.getResponse_time();
            Map<Integer, List<crowd_action>> temps = responseTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
              //  Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("memberResponseTimeList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("memberResponseTimeList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //下单行为
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_action())) {
            List<crowd_action> orderActions = memberCrowdGroup.getOrder_action();
            Map<Integer, List<crowd_action>> temps = orderActions.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("orderBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("orderBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //综合行为
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActions())) {
            List<crowd_action> actions = memberCrowdGroup.getActions();
            Map<Integer, List<crowd_action>> temps = actions.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("comprehensiveBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("comprehensiveBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //活动偏好
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActive_like())) {
            List<crowd_action> activeLikes = memberCrowdGroup.getActive_like();
            Map<Integer, List<crowd_action>> temps = activeLikes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = in.get(i);
                    and.add(Criteria.where("activityBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));

                }
                //criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
             //   Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_action c = ex.get(i);
                    not.add(Criteria.where("activityBehaviorList").elemMatch(Criteria.where("_id").is(c.getId())));
                }
              //  criteria.norOperator(exArray);
            }
        }
        //------订单相关
        //支付方式
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getPay_type())) {
            List<crowd_base_value> payTypes = memberCrowdGroup.getPay_type();
            Map<Integer, List<crowd_base_value>> temps = payTypes.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("payMode").is(Integer.parseInt(c.getId()))));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("payMode").is(Integer.parseInt(c.getId()))));
                    }
                }
              //  criteria.norOperator(exArray);
            }
        }
        //支付形式
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getPay_form())) {
            List<crowd_base_value> payForms = memberCrowdGroup.getPay_form();
            Map<Integer, List<crowd_base_value>> temps = payForms.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
             //   Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("payType").is(Integer.parseInt(c.getId()))));
                    }

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("payType").is(Integer.parseInt(c.getId()))));
                    }
                }
                //criteria.norOperator(exArray);
            }
        }
        //订单状态
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_state())) {
            List<crowd_base_value> orderStatus = memberCrowdGroup.getOrder_state();
            Map<Integer, List<crowd_base_value>> temps = orderStatus.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                //Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("status").is(Integer.parseInt(c.getId()))));
                    }

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("status").is(Integer.parseInt(c.getId()))));
                    }
                }
               // criteria.norOperator(exArray);
            }
        }
        //是否为活动单
        if (memberCrowdGroup.getActive_order() != null) {
          //  and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("activityFlag").is(memberCrowdGroup.getActive_order()))));
            if (memberCrowdGroup.getActive_order() == 1) {
                and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("activityFlag").is(true))));
            } else {
                and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("activityFlag").is(false))));
            }
        }
        //订单参与的活动
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActiveCodeList())) {
            List<crowd_active> activeCodes = memberCrowdGroup.getActiveCodeList();
            Map<Integer, List<crowd_active>> temps = activeCodes.stream().collect(Collectors.groupingBy(crowd_active::getInclude));
            List<crowd_active> in = temps.get(1);
            List<crowd_active> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_active c = in.get(i);
                    and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("activityCode").is(c.getActive_id())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_active c = ex.get(i);
                    not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("activityCode").is(c.getActive_id())));
                }
              //  criteria.norOperator(exArray);
            }
        }
        //订单参与活动的类型
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActiveTypeList())) {
            List<crowd_active> activeTypes = memberCrowdGroup.getActiveTypeList();
            Map<Integer, List<crowd_active>> temps = activeTypes.stream().collect(Collectors.groupingBy(crowd_active::getInclude));
            List<crowd_active> in = temps.get(1);
            List<crowd_active> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_active c = in.get(i);
                    and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("activityType").is(c.getActive_type())));

                }
               // criteria.andOperator(andArray);
            }

            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_active c = ex.get(i);
                    not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("activityType").is(c.getActive_type())));
                }
              //  criteria.norOperator(exArray);
            }
        }
        //订单来源
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_source())) {
            List<crowd_base_value> orderSources = memberCrowdGroup.getOrder_source();
            Map<Integer, List<crowd_base_value>> temps = orderSources.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
               // Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("source").is(c.getId())));

                }
               // criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
             //   Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("source").is(c.getId())));
                }
              //  criteria.norOperator(exArray);
            }
        }
        //购买的商品
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getProducts())) {
            List<crowd_product> products = memberCrowdGroup.getProducts();
            Map<Integer, List<crowd_product>> temps = products.stream().collect(Collectors.groupingBy(crowd_product::getInclude));
            List<crowd_product> in = temps.get(1);
            List<crowd_product> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_product c = in.get(i);
                    and.add(Criteria.where("memberProductList").elemMatch(Criteria.where("productCode").is(c.getId())));

                }
//                criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
              //  Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_product c = ex.get(i);
                    not.add(Criteria.where("memberProductList").elemMatch(Criteria.where("productCode").is(c.getId())));
                }
               // criteria.norOperator(exArray);
            }
        }
        //支付状态
        if (memberCrowdGroup.getPay_state() != null) {
            and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("payStatus").is(memberCrowdGroup.getPay_state()))));
//            if (memberCrowdGroup.getPay_state() == 1) {
//                and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("payStatus").is(true))));
//            } else {
//                and.add((Criteria.where("memberOrders").elemMatch(Criteria.where("payStatus").is(false))));
//            }
        }
        //物流状态
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getLogistics_state())) {
            List<crowd_base_value> logisticsStates = memberCrowdGroup.getLogistics_state();
            Map<Integer, List<crowd_base_value>> temps = logisticsStates.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
              //  Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("logisticsStatus").is(Integer.parseInt(c.getId()))));
                    }

                }
               // criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("logisticsStatus").is(Integer.parseInt(c.getId()))));
                    }
                }
               // criteria.norOperator(exArray);
            }
        }
        //物流公司
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getLogistics_company_id())) {
            List<crowd_base_value> logisticsCompanyIds = memberCrowdGroup.getLogistics_company_id();
            Map<Integer, List<crowd_base_value>> temps = logisticsCompanyIds.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
              //  Criteria[] andArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = in.get(i);
                    if (c.getId() != null) {
                        and.add(Criteria.where("memberOrders").elemMatch(Criteria.where("companyCode").is(c.getId())));
                    }

                }
               // criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
               // Criteria[] exArray = new Criteria[size];
                for (int i = 0; i < size; i++) {
                    crowd_base_value c = ex.get(i);
                    if (c.getId() != null) {
                        not.add(Criteria.where("memberOrders").elemMatch(Criteria.where("companyCode").is(c.getId())));
                    }
                }
               // criteria.norOperator(exArray);
            }
        }
        //累计消费金额//大于等于
        if(memberCrowdGroup.getTotal_amount()!=null){
            AmountParam totalAmount = memberCrowdGroup.getTotal_amount();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            if (">=".equals(symbol)){
                and.add(Criteria.where("totalCounsumAmount").gte(am));
            }else if (">".equals(symbol)){
                and.add(Criteria.where("totalCounsumAmount").gt(am));
            }else if ("=".equals(symbol)){
                and.add(Criteria.where("totalCounsumAmount").is(am));
            }else if ("<=".equals(symbol)){
                and.add(Criteria.where("totalCounsumAmount").lte(am));
            }else if ("<".equals(symbol)){
                and.add(Criteria.where("totalCounsumAmount").lt(am));
            }
        }
        //订单总金额//大于等于
        if(memberCrowdGroup.getOrder_total_amount()!=null){
            AmountParam orderTotalAmount = memberCrowdGroup.getOrder_total_amount();
            Double amount = orderTotalAmount.getAmount();
            String symbol = orderTotalAmount.getSymbol();
            int am = BigDecimal.valueOf(amount * 100).intValue();
            if (">=".equals(symbol)){
                and.add(Criteria.where("totalOrderAmount").gte(am));
            }else if (">".equals(symbol)){
                and.add(Criteria.where("totalOrderAmount").gt(am));
            }else if ("=".equals(symbol)){
                and.add(Criteria.where("totalOrderAmount").is(am));
            }else if ("<=".equals(symbol)){
                and.add(Criteria.where("totalOrderAmount").lte(am));
            }else if ("<".equals(symbol)){
                and.add(Criteria.where("totalOrderAmount").lt(am));
            }
        }
        //订单应收总金额//大于等于
        if(memberCrowdGroup.getOrder_rec_amount()!=null){
            AmountParam orderTotalAmount = memberCrowdGroup.getOrder_rec_amount();
            Double amount = orderTotalAmount.getAmount();
            String symbol = orderTotalAmount.getSymbol();
            int am = BigDecimal.valueOf(amount * 100).intValue();
            if (">=".equals(symbol)){
                and.add(Criteria.where("orderRecAmount").gte(am));
            }else if (">".equals(symbol)){
                and.add(Criteria.where("orderRecAmount").gt(am));
            }else if ("=".equals(symbol)){
                and.add(Criteria.where("orderRecAmount").is(am));
            }else if ("<=".equals(symbol)){
                and.add(Criteria.where("orderRecAmount").lte(am));
            }else if ("<".equals(symbol)){
                and.add(Criteria.where("orderRecAmount").lt(am));
            }

        }

        //
        //最高订单金额（单）//大于等于
        if(memberCrowdGroup.getOrder_high_am()!=null){
            AmountParam orderRecAmount = memberCrowdGroup.getOrder_high_am();
            Double amount = orderRecAmount.getAmount();
            String symbol = orderRecAmount.getSymbol();
            int am = BigDecimal.valueOf(amount * 100).intValue();
            if (">=".equals(symbol)){
                and.add(Criteria.where("orderHighAm").gte(am));
            }else if (">".equals(symbol)){
                and.add(Criteria.where("orderHighAm").gt(am));
            }else if ("=".equals(symbol)){
                and.add(Criteria.where("orderHighAm").is(am));
            }else if ("<=".equals(symbol)){
                and.add(Criteria.where("orderHighAm").lte(am));
            }else if ("<".equals(symbol)){
                and.add(Criteria.where("orderHighAm").lt(am));
            }
        }

        //最低订单金额（单）//大于等于
        if(memberCrowdGroup.getOrder_low_am()!=null){
            AmountParam orderLowAm = memberCrowdGroup.getOrder_low_am();
            Double amount = orderLowAm.getAmount();
            String symbol = orderLowAm.getSymbol();
            int am = BigDecimal.valueOf(amount * 100).intValue();
            if (">=".equals(symbol)){
                and.add(Criteria.where("orderLowAm").gte(am));
            }else if (">".equals(symbol)){
                and.add(Criteria.where("orderLowAm").gt(am));
            }else if ("=".equals(symbol)){
                and.add(Criteria.where("orderLowAm").is(am));
            }else if ("<=".equals(symbol)){
                and.add(Criteria.where("orderLowAm").lte(am));
            }else if ("<".equals(symbol)){
                and.add(Criteria.where("orderLowAm").lt(am));
            }
        }
        //是否下单
        if(memberCrowdGroup.getHave_order()!=null){
            if (memberCrowdGroup.getHave_order() == 1) {
                and.add(Criteria.where("haveOrder").is(1));
            } else {
                and.add(Criteria.where("haveOrder").is(0));
            }
        }
        //病症
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getDiseases())) {
            List<crowd_disease> diseases = memberCrowdGroup.getDiseases();
            Map<Integer, List<crowd_disease>> temps = diseases.stream().collect(Collectors.groupingBy(crowd_disease::getInclude));
            List<crowd_disease> in = temps.get(1);
            List<crowd_disease> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                for (int i = 0; i < size; i++) {
                    crowd_disease c = in.get(i);
                    and.add(Criteria.where("memberDiseaseList").elemMatch(Criteria.where("diseaseId").is(c.getId())));

                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                for (int i = 0; i < size; i++) {
                    crowd_disease c = ex.get(i);
                    not.add(Criteria.where("memberDiseaseList").elemMatch(Criteria.where("diseaseId").is(c.getId())));
                }
            }
        }
        //最后一次进线时间
        if(memberCrowdGroup.getLastCallDays()!=null){
            DayParam days = memberCrowdGroup.getLastCallDays();
            if (days.getDay() != null && StringUtils.isNotEmpty(days.getSymbol())) {
                if (days.getDay() > 10000) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "最后一次下单时间不能大于10000");
                }
                Criteria where = dayParamCondition(days, "lastCallInTime");
                if (where != null) {
                    and.add(where);
                }
            }
        }
        //购买的商品
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAdvertProducts())) {
            List<crowd_product> advertProducts = memberCrowdGroup.getAdvertProducts();
            Map<Integer, List<crowd_product>> temps = advertProducts.stream().collect(Collectors.groupingBy(crowd_product::getInclude));
            List<crowd_product> in = temps.get(1);
            List<crowd_product> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                int size = in.size();
                for (int i = 0; i < size; i++) {
                    crowd_product c = in.get(i);
                    Criteria criteria = new Criteria();
                    and.add(Criteria.where("advertProducts").in(String.valueOf(c.getId())));

                }
//                criteria.andOperator(andArray);
            }
            if (!CollectionUtils.isEmpty(ex)) {
                int size = ex.size();
                for (int i = 0; i < size; i++) {
                    crowd_product c = ex.get(i);
                    Criteria criteria = new Criteria();
                    not.add(Criteria.where("advertProducts").in(String.valueOf(c.getId())));
                }
                // criteria.norOperator(exArray);
            }
        }
        Criteria criteria1 = new Criteria();
        if(and.size()>0){
            int andSize = and.size();
            Criteria[] andArray = new Criteria[andSize];
            for (int i = 0; i < andSize; i++) {
                andArray[i] = and.get(i);

            }
            criteria1.andOperator(andArray);
        }
        if(not.size()>0){
            int notSize = not.size();
            Criteria[] notArray = new Criteria[notSize];
            for (int i = 0; i < notSize; i++) {
                notArray[i] = not.get(i);

            }
            criteria1.norOperator(notArray);
        }

        query.addCriteria(criteria1);
        return query;
    }

    /**
     * @param past 天数
     * @Author: lichanghong
     * @Description: 获取之前几天的日期
     * @Date: 2021/1/26 11:06 下午
     * @Return: java.util.Date
     */
    private static Date getPastDate(int past) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return today;
    }
    /**
     * @Author: lichanghong
     * @Description: 根据顾客编号查询objectId
     * @Date: 2021/2/20 1:59 下午
     * @param memberCodes
     * @Return: java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String,String> queryByCodes(List<String> memberCodes) {

        Query query = new Query();
        query.addCriteria(Criteria.where("memberCard").in(memberCodes));
        query.fields().include("_id").include("memberCard");
        List<MemberLabel> list = mongoTemplate.find(query,this.getEntityClass(),COLLECTION_NAME);
        if(!CollectionUtil.isEmpty(list)){
            Map<String,String> map = new HashMap<>(list.size()*2);
            for (MemberLabel label : list) {
                map.put(label.getMemberCard(),label.get_id());
            }
            return map;
        }

        return Collections.EMPTY_MAP;
    }


    public Criteria dayParamCondition(DayParam dayParam,String attr){
        String symbol = dayParam.getSymbol();
        Integer days = dayParam.getDay();
        Date date = getPastDate(days);

        Date start = DealDateUtil.getStart(date);//开始时间
        Date end = DealDateUtil.getEnd(date);//结束时间

        Criteria where = null;
        if (">=".equals(symbol)){
            where = Criteria.where(attr).lte(MongoDateHelper.getMongoDate(end));
        }else if (">".equals(symbol)){
            where = Criteria.where(attr).lt(MongoDateHelper.getMongoDate(start));
        }else if ("=".equals(symbol)){
            where = Criteria.where(attr).gte(MongoDateHelper.getMongoDate(start)).lt(MongoDateHelper.getMongoDate(end));
        }else if ("<=".equals(symbol)){
            where = Criteria.where(attr).gte(MongoDateHelper.getMongoDate(start));
        }else if ("<".equals(symbol)){
            where = Criteria.where(attr).gt(MongoDateHelper.getMongoDate(end));
        }
        return where;
    }
}
