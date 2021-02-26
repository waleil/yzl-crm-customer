package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.mongomodel.*;

import cn.net.yzl.crm.customer.sys.BizException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelESDao
 * @description todo
 * @date: 2021/2/22 3:22 下午
 */
@ConditionalOnBean(value = RestHighLevelClient.class)
@Component
public class MemberLabelESDao {
    private final static String index = "member_label";
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    public long memberCrowdGroupTrial(member_crowd_group memberCrowdGroup) {
        SearchSourceBuilder sourceBuilder = initQuery(memberCrowdGroup);
        CountRequest request = new CountRequest();
        request.source(sourceBuilder);
        request.indices(index);
        try {
            CountResponse response =restHighLevelClient.count(request, RequestOptions.DEFAULT);
            return response.getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private SearchSourceBuilder initQuery(member_crowd_group memberCrowdGroup) {

        //and查询
        List<QueryBuilder> filters = new ArrayList<>();
        //not and查询
        List<QueryBuilder> mustNots = new ArrayList<>();
        //判断性别
        if (memberCrowdGroup.getSex() != null) {
            if (memberCrowdGroup.getSex() == 1) {
                filters.add(QueryBuilders.termQuery("sex", 1));
            } else {
                filters.add(QueryBuilders.termQuery("sex", 0));
            }
        }
        //邮箱
        if (memberCrowdGroup.getEmail() != null) {
            if (memberCrowdGroup.getEmail() == 1) {
                filters.add(QueryBuilders.termQuery("hasEmail", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasEmail", false));
            }
        }
        //QQ
        if (memberCrowdGroup.getQq() != null) {
            if (memberCrowdGroup.getQq() == 1) {
                filters.add(QueryBuilders.termQuery("hasQQ", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasQQ", false));
            }
        }
        //微信
        if (memberCrowdGroup.getWechat() != null) {
            if (memberCrowdGroup.getWechat() == 1) {
                filters.add(QueryBuilders.termQuery("hasWechat", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasWechat", false));
            }
        }
        //是否拥有储值金额
        if (memberCrowdGroup.getRecharge() != null) {
            if (memberCrowdGroup.getRecharge() == 1) {
                filters.add(QueryBuilders.termQuery("hasMoney", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasMoney", false));
            }
        }
        //是否拥有优惠券
        if (memberCrowdGroup.getTicket() != null) {
            if (memberCrowdGroup.getTicket() == 1) {
                filters.add(QueryBuilders.termQuery("hasTicket", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasTicket", false));
            }
        }
        //是否拥有红包
        if (memberCrowdGroup.getRed_bag() != null) {
            if (memberCrowdGroup.getRed_bag() == 1) {
                filters.add(QueryBuilders.termQuery("hasTedBag", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasTedBag", false));
            }
        }

        //是否有积分
        if (memberCrowdGroup.getIntegral() != null) {
            if (memberCrowdGroup.getIntegral() == 1) {
                filters.add(QueryBuilders.termQuery("hasIntegral", true));
            } else {
                filters.add(QueryBuilders.termQuery("hasIntegral", false));
            }
        }
        //是否为会员
        if (memberCrowdGroup.getVip() != null) {
            if (memberCrowdGroup.getVip() == 1) {
                filters.add(QueryBuilders.termQuery("vipFlag", true));
            } else {
                filters.add(QueryBuilders.termQuery("vipFlag", false));
            }
        }
        //顾客年龄
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAge())) {
            List<Member_Age> list = memberCrowdGroup.getAge();
            Map<Integer, List<Member_Age>> temps = list.stream().collect(Collectors.groupingBy(Member_Age::getInclude));
            List<Member_Age> in = temps.get(1);
            List<Member_Age> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (Member_Age age : in) {
                    RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
                    rangeQuery.gte(age.getStart_age()).lte(age.getEnd_age());
                    filters.add(rangeQuery);
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (Member_Age age : ex) {
                    RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
                    rangeQuery.gte(age.getStart_age()).lte(age.getEnd_age());
                    mustNots.add(rangeQuery);
                }
            }
        }
        //归属地
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
                    for (crowd_area c : in) {
                        filters.add(QueryBuilders.termQuery("provinceCode", Integer.parseInt(c.getId())));
                    }
                }
                List<crowd_area> ex = temps.get(0);
                if (!CollectionUtils.isEmpty(ex)) {
                    for (crowd_area c : ex) {
                        mustNots.add(QueryBuilders.termQuery("provinceCode", Integer.parseInt(c.getId())));
                    }
                }
            }
            //市
            List<crowd_area> cityList = listMap.get(2);
            if (!CollectionUtils.isEmpty(cityList)) {
                Map<Integer, List<crowd_area>> temps = cityList.stream().collect(Collectors.groupingBy(crowd_area::getInclude));
                List<crowd_area> in = temps.get(1);
                if (!CollectionUtils.isEmpty(in)) {
                    for (crowd_area c : in) {
                        filters.add(QueryBuilders.termQuery("cityCode", Integer.parseInt(c.getId())));
                    }
                }

                List<crowd_area> ex = temps.get(0);
                if (!CollectionUtils.isEmpty(ex)) {
                    for (crowd_area c : ex) {
                        mustNots.add(QueryBuilders.termQuery("cityCode", Integer.parseInt(c.getId())));
                    }
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
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("mGradeId", Integer.parseInt(c.getId())));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("mGradeId", Integer.parseInt(c.getId())));
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
                for (crowd_member_type c : in) {
                    filters.add(QueryBuilders.termQuery("memberType", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_member_type c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberType", c.getId()));
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
                for (crowd_activity_degree c : ex) {
                    filters.add(QueryBuilders.termQuery("memberType", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_activity_degree c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberType", c.getId()));
                }
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
                Date date = getPastDate(days.getDay());
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("firstOrderTime");
                String symbol = days.getSymbol();
                if (">=".equals(symbol)) {
                    rangeQuery.gte(date.getTime());
                } else if (">".equals(symbol)) {
                    rangeQuery.gt(date.getTime());
                } else if ("=".equals(symbol)) {
                    date = formatDate(date);
                    rangeQuery.gte(date.getTime()).lte(date.getTime() + (24 * 60 * 60 * 1000));
                } else if ("<=".equals(symbol)) {
                    rangeQuery.lte(date.getTime());
                } else if ("<".equals(symbol)) {
                    rangeQuery.lt(date.getTime());
                }
                filters.add(rangeQuery);
            }
        }
        //首次订单金额>=
        if (memberCrowdGroup.getFirst_order_am() != null) {
            AmountParam firstOrderAm = memberCrowdGroup.getFirst_order_am();
            if (firstOrderAm.getAmount() != null && StringUtils.isNotEmpty(firstOrderAm.getSymbol())) {
                Double amount = firstOrderAm.getAmount();
                String symbol = firstOrderAm.getSymbol();
                int am = BigDecimal.valueOf(amount * 100).intValue();
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("firstOrderAm");
                if (">=".equals(symbol)) {
                    rangeQuery.gte(am);
                } else if (">".equals(symbol)) {
                    rangeQuery.gt(am);
                } else if ("=".equals(symbol)) {
                    rangeQuery.gte(am);
                    rangeQuery.lte(am);
                } else if ("<=".equals(symbol)) {
                    rangeQuery.lte(am);
                } else if ("<".equals(symbol)) {
                    rangeQuery.lt(am);
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
                Date date = getPastDate(days.getDay());
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("lastOrderTime");
                String symbol = days.getSymbol();
                if (">=".equals(symbol)) {
                    rangeQuery.gte(date.getTime());
                } else if (">".equals(symbol)) {
                    rangeQuery.gt(date.getTime());
                } else if ("=".equals(symbol)) {
                    date = formatDate(date);
                    rangeQuery.gte(date.getTime()).lte(date.getTime() + (24 * 60 * 60 * 1000));
                } else if ("<=".equals(symbol)) {
                    rangeQuery.lte(date.getTime());
                } else if ("<".equals(symbol)) {
                    rangeQuery.lt(date.getTime());
                }
                filters.add(rangeQuery);
            }
        }
        //生日月份
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMember_month())) {
            List<crowd_base_value> memberMonth = memberCrowdGroup.getMember_month();
            Map<Integer, List<crowd_base_value>> temps = memberMonth.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberMonth", Integer.parseInt(c.getId())));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberMonth", Integer.parseInt(c.getId())));
                }
            }
        }
        // todo 待做
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMember_type())) {

        }
        //获客媒体
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getMediaList())) {
            List<crowd_media> medias = memberCrowdGroup.getMediaList();
            Map<Integer, List<crowd_media>> temps = medias.stream().collect(Collectors.groupingBy(crowd_media::getInclude));
            List<crowd_media> in = temps.get(1);
            List<crowd_media> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_media c : in) {
                    filters.add(QueryBuilders.termQuery("mediaId", c.getMedia_id()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_media c : ex) {
                    mustNots.add(QueryBuilders.termQuery("mediaId", c.getMedia_id()));
                }
            }
        }
        //广告
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAdvers())) {
            List<crowd_adver> advers = memberCrowdGroup.getAdvers();
            Map<Integer, List<crowd_adver>> temps = advers.stream().collect(Collectors.groupingBy(crowd_adver::getInclude));
            List<crowd_adver> in = temps.get(1);
            List<crowd_adver> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_adver c : in) {
                    filters.add(QueryBuilders.termQuery("adverCode", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_adver c : ex) {
                    mustNots.add(QueryBuilders.termQuery("adverCode", c.getId()));
                }
            }
        }
        //签收时间时间
        if (memberCrowdGroup.getLast_order_to_days() != null) {
            DayParam days = memberCrowdGroup.getLast_order_to_days();
            if (days.getDay() != null && StringUtils.isNotEmpty(days.getSymbol())) {
                if (days.getDay() > 10000) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "最后一次签收时间不能大于10000");
                }
                Date date = getPastDate(days.getDay());
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("lastSignTime");
                String symbol = days.getSymbol();
                if (">=".equals(symbol)) {
                    rangeQuery.gte(date.getTime());
                } else if (">".equals(symbol)) {
                    rangeQuery.gt(date.getTime());
                } else if ("=".equals(symbol)) {
                    date = formatDate(date);
                    rangeQuery.gte(date.getTime()).lte(date.getTime() + (24 * 60 * 60 * 1000));
                } else if ("<=".equals(symbol)) {
                    rangeQuery.lte(date.getTime());
                } else if ("<".equals(symbol)) {
                    rangeQuery.lt(date.getTime());
                }
                filters.add(rangeQuery);
            }
        }
        //方便接电话时间
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getPhone_time())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getPhone_time();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("phoneDictList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("phoneDictList.id", c.getId()));
                }
            }
        }
        //性格偏好
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getNature())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getNature();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("memberCharacterList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("memberCharacterList.id", c.getId()));
                }
            }
        }
        //响应时间
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getResponse_time())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getResponse_time();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("memberResponseTimeList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("memberResponseTimeList.id", c.getId()));
                }
            }
        }
        //下单行为
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_action())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getOrder_action();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("orderBehaviorList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("orderBehaviorList.id", c.getId()));
                }
            }
        }
        //综合行为
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActions())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getActions();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("comprehensiveBehaviorList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("comprehensiveBehaviorList.id", c.getId()));
                }
            }
        }
        //活动偏好
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActive_like())) {
            List<crowd_action> phoneTimes = memberCrowdGroup.getActive_like();
            Map<Integer, List<crowd_action>> temps = phoneTimes.stream().collect(Collectors.groupingBy(crowd_action::getInclude));
            List<crowd_action> in = temps.get(1);
            List<crowd_action> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_action c : in) {
                    filters.add(QueryBuilders.termQuery("activityBehaviorList.id", c.getId()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_action c : in) {
                    mustNots.add(QueryBuilders.termQuery("activityBehaviorList.id", c.getId()));
                }
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
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.payMode", Integer.parseInt(c.getId())));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.payMode", Integer.parseInt(c.getId())));
                }
            }
        }
        //支付形式
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getPay_form())) {
            List<crowd_base_value> payTypes = memberCrowdGroup.getPay_form();
            Map<Integer, List<crowd_base_value>> temps = payTypes.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.payType", Integer.parseInt(c.getId())));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.payType", Integer.parseInt(c.getId())));
                }
            }
        }
        //订单状态
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_state())) {
            List<crowd_base_value> payTypes = memberCrowdGroup.getOrder_state();
            Map<Integer, List<crowd_base_value>> temps = payTypes.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.status", Integer.parseInt(c.getId())));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.status", Integer.parseInt(c.getId())));
                }
            }
        }
        //是否为活动单
        if (memberCrowdGroup.getActive_order() != null) {
            if (memberCrowdGroup.getActive_order() == 1) {
                filters.add(QueryBuilders.termQuery("memberOrders.activityFlag", true));
            } else {
                filters.add(QueryBuilders.termQuery("memberOrders.activityFlag", false));
            }
        }
        //订单参与的活动
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActiveCodeList())) {
            List<crowd_active> activeCodes = memberCrowdGroup.getActiveCodeList();
            Map<Integer, List<crowd_active>> temps = activeCodes.stream().collect(Collectors.groupingBy(crowd_active::getInclude));
            List<crowd_active> in = temps.get(1);
            List<crowd_active> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_active c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.activityCode", c.getActive_id()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_active c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.activityCode", c.getActive_id()));
                }
            }
        }

        //订单参与活动的类型
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getActiveTypeList())) {
            List<crowd_active> activeTypes = memberCrowdGroup.getActiveTypeList();
            Map<Integer, List<crowd_active>> temps = activeTypes.stream().collect(Collectors.groupingBy(crowd_active::getInclude));
            List<crowd_active> in = temps.get(1);
            List<crowd_active> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_active c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.activityType", c.getActive_type()));
                }
            }

            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_active c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.activityType", c.getActive_type()));
                }
            }
        }
        //订单来源
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getOrder_source())) {
            List<crowd_base_value> orderSources = memberCrowdGroup.getOrder_source();
            Map<Integer, List<crowd_base_value>> temps = orderSources.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.source", c.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.activityType", c.getId()));
                }
            }
        }

        //购买的商品
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getProducts())) {
            List<crowd_product> products = memberCrowdGroup.getProducts();
            Map<Integer, List<crowd_product>> temps = products.stream().collect(Collectors.groupingBy(crowd_product::getInclude));
            List<crowd_product> in = temps.get(1);
            List<crowd_product> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_product c : in) {
                    filters.add(QueryBuilders.termQuery("memberProductList.productCode", c.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_product c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberProductList.productCode", c.getId()));
                }
            }
        }
        //支付状态
        if (memberCrowdGroup.getPay_state() != null) {
            filters.add(QueryBuilders.termQuery("memberOrders.payStatus", memberCrowdGroup.getPay_state()));
        }
        //物流状态
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getLogistics_state())) {
            List<crowd_base_value> logisticsStates = memberCrowdGroup.getLogistics_state();
            Map<Integer, List<crowd_base_value>> temps = logisticsStates.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.logisticsStatus", Integer.parseInt(c.getId())));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.logisticsStatus", Integer.parseInt(c.getId())));
                }
            }
        }
        //物流公司
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getLogistics_company_id())) {
            List<crowd_base_value> logisticsCompanyIds = memberCrowdGroup.getLogistics_company_id();
            Map<Integer, List<crowd_base_value>> temps = logisticsCompanyIds.stream().collect(Collectors.groupingBy(crowd_base_value::getInclude));
            List<crowd_base_value> in = temps.get(1);
            List<crowd_base_value> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_base_value c : in) {
                    filters.add(QueryBuilders.termQuery("memberOrders.companyCode", c.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_base_value c : ex) {
                    mustNots.add(QueryBuilders.termQuery("memberOrders.companyCode", c.getId()));
                }
            }
        }

        //累计消费金额//大于等于
        if(memberCrowdGroup.getTotal_amount()!=null){
            AmountParam totalAmount = memberCrowdGroup.getTotal_amount();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("totalCounsumAmount");
            if (">=".equals(symbol)){
                rangeQuery.gte(am);
            }else if (">".equals(symbol)){
                rangeQuery.gt(am);
            }else if ("=".equals(symbol)){
                rangeQuery.gte(am);
                rangeQuery.lte(am);
            }else if ("<=".equals(symbol)){
                rangeQuery.lte(am);
            }else if ("<".equals(symbol)){
                rangeQuery.lt(am);
            }
            filters.add(rangeQuery);
        }
        //订单总金额//大于等于
        if(memberCrowdGroup.getOrder_total_amount()!=null){
            AmountParam totalAmount = memberCrowdGroup.getOrder_total_amount();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("totalOrderAmount");
            if (">=".equals(symbol)){
                rangeQuery.gte(am);
            }else if (">".equals(symbol)){
                rangeQuery.gt(am);
            }else if ("=".equals(symbol)){
                rangeQuery.gte(am);
                rangeQuery.lte(am);
            }else if ("<=".equals(symbol)){
                rangeQuery.lte(am);
            }else if ("<".equals(symbol)){
                rangeQuery.lt(am);
            }
            filters.add(rangeQuery);
        }
        //订单应收总金额//大于等于
        if(memberCrowdGroup.getOrder_rec_amount()!=null){
            AmountParam totalAmount = memberCrowdGroup.getOrder_rec_amount();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("orderRecAmount");
            if (">=".equals(symbol)){
                rangeQuery.gte(am);
            }else if (">".equals(symbol)){
                rangeQuery.gt(am);
            }else if ("=".equals(symbol)){
                rangeQuery.gte(am);
                rangeQuery.lte(am);
            }else if ("<=".equals(symbol)){
                rangeQuery.lte(am);
            }else if ("<".equals(symbol)){
                rangeQuery.lt(am);
            }
            filters.add(rangeQuery);
        }

        //最高订单金额（单）//大于等于
        if(memberCrowdGroup.getOrder_high_am()!=null){
            AmountParam totalAmount = memberCrowdGroup.getOrder_high_am();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("orderHighAm");
            if (">=".equals(symbol)){
                rangeQuery.gte(am);
            }else if (">".equals(symbol)){
                rangeQuery.gt(am);
            }else if ("=".equals(symbol)){
                rangeQuery.gte(am);
                rangeQuery.lte(am);
            }else if ("<=".equals(symbol)){
                rangeQuery.lte(am);
            }else if ("<".equals(symbol)){
                rangeQuery.lt(am);
            }
            filters.add(rangeQuery);
        }

        //最低订单金额（单）//大于等于
        if(memberCrowdGroup.getOrder_low_am()!=null){
            AmountParam totalAmount = memberCrowdGroup.getOrder_low_am();
            Double amount = totalAmount.getAmount();
            String symbol = totalAmount.getSymbol();

            int am = BigDecimal.valueOf(amount * 100).intValue();
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("orderLowAm");
            if (">=".equals(symbol)){
                rangeQuery.gte(am);
            }else if (">".equals(symbol)){
                rangeQuery.gt(am);
            }else if ("=".equals(symbol)){
                rangeQuery.gte(am);
                rangeQuery.lte(am);
            }else if ("<=".equals(symbol)){
                rangeQuery.lte(am);
            }else if ("<".equals(symbol)){
                rangeQuery.lt(am);
            }
            filters.add(rangeQuery);
        }
        //是否下单
        if(memberCrowdGroup.getHave_order()!=null){
            filters.add(QueryBuilders.termQuery("haveOrder",memberCrowdGroup.getHave_order()));
        }
        //病症
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getDiseases())) {
            List<crowd_disease> diseases = memberCrowdGroup.getDiseases();
            Map<Integer, List<crowd_disease>> temps = diseases.stream().collect(Collectors.groupingBy(crowd_disease::getInclude));
            List<crowd_disease> in = temps.get(1);
            List<crowd_disease> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_disease c : in) {
                    filters.add(QueryBuilders.termQuery("memberDiseaseList.diseaseId", c.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_disease c : in) {
                    mustNots.add(QueryBuilders.termQuery("memberDiseaseList.diseaseId", c.getId()));
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
                Date date = getPastDate(days.getDay());
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("lastCallInTime");
                String symbol = days.getSymbol();
                if (">=".equals(symbol)) {
                    rangeQuery.gte(date.getTime());
                } else if (">".equals(symbol)) {
                    rangeQuery.gt(date.getTime());
                } else if ("=".equals(symbol)) {
                    date = formatDate(date);
                    rangeQuery.gte(date.getTime()).lte(date.getTime() + (24 * 60 * 60 * 1000));
                } else if ("<=".equals(symbol)) {
                    rangeQuery.lte(date.getTime());
                } else if ("<".equals(symbol)) {
                    rangeQuery.lt(date.getTime());
                }
                filters.add(rangeQuery);

            }
        }
        //购买的商品
        if (!CollectionUtils.isEmpty(memberCrowdGroup.getAdvertProducts())) {
            List<crowd_product> advertProducts = memberCrowdGroup.getAdvertProducts();
            Map<Integer, List<crowd_product>> temps = advertProducts.stream().collect(Collectors.groupingBy(crowd_product::getInclude));
            List<crowd_product> in = temps.get(1);
            List<crowd_product> ex = temps.get(0);
            if (!CollectionUtils.isEmpty(in)) {
                for (crowd_product c : in) {
                    filters.add(QueryBuilders.termQuery("advertProducts.productCode", c.getId()));
                }
            }
            if (!CollectionUtils.isEmpty(ex)) {
                for (crowd_product c : in) {
                    mustNots.add(QueryBuilders.termQuery("advertProducts.productCode", c.getId()));
                }
            }
        }
        //外层循环
        BoolQueryBuilder firstBool = QueryBuilders.boolQuery();
        //拼接and
        if(!CollectionUtils.isEmpty(filters)){
            for (QueryBuilder filter : filters) {
                firstBool.filter(filter);
            }
        }
        //拼接排除
        if(!CollectionUtils.isEmpty(mustNots)){
            for (QueryBuilder not : filters) {
                firstBool.mustNot(not);
            }
        }
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(firstBool);
        return builder;
    }

    public static Date formatDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }

    private static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return today;
    }

    /**
     * 构造int范围查询调教
     * wangzhe
     * 2021-02-24
     * @param esAttr es的属性
     * @param symbol 符号
     * @param num 数值
     * @return
     */
    private RangeQueryBuilder intRangeQuery (String esAttr,String symbol,int num){
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(esAttr);
        if (">=".equals(symbol)){
            rangeQuery.gte(num);
        }else if (">".equals(symbol)){
            rangeQuery.gt(num);
        }else if ("=".equals(symbol)){
            rangeQuery.gte(num);
            rangeQuery.lte(num);
        }else if ("<=".equals(symbol)){
            rangeQuery.lte(num);
        }else if ("<".equals(symbol)){
            rangeQuery.lt(num);
        }
        return rangeQuery;
    }

    /**
     * 构造int范围查询调教
     * wangzhe
     * 2021-02-24
     * @param esAttr es的属性
     * @param symbol 符号
     * @param date 目标日期
     * @return
     */
    private RangeQueryBuilder dateRangeQuery (String esAttr,String symbol,Date date){
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(esAttr);
        if (">=".equals(symbol)) {
            rangeQuery.gte(date.getTime());
        } else if (">".equals(symbol)) {
            rangeQuery.gt(date.getTime());
        } else if ("=".equals(symbol)) {
            date = formatDate(date);
            rangeQuery.gte(date.getTime()).lte(date.getTime() + (24 * 60 * 60 * 1000));
        } else if ("<=".equals(symbol)) {
            rangeQuery.lte(date.getTime());
        } else if ("<".equals(symbol)) {
            rangeQuery.lt(date.getTime());
        }
        return rangeQuery;
    }
}
