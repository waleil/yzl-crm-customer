package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.model.MemberOrderObject;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import cn.net.yzl.order.model.vo.order.OrderTotal4MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 订单相关接口
 * wangzhe
 * 2021-02-26
 */
@Component
@Slf4j
public class OrderClientAPI {

    @Autowired
    OrderFien orderFien;

    public static OrderClientAPI orderClientAPI;

    public void setOrderFien(OrderFien orderFien) {
        this.orderFien = orderFien;
    }

    @PostConstruct
    public void init() {
        orderClientAPI = this;
        orderClientAPI.orderFien = this.orderFien;
    }

    /**
     * 统计本年度累计消费金额、本年度最高消费金额、本年度最高预存金额
     * wangzhe
     * 2021-02-26
     * @param memberCards
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<MemberTotal> queryMemberTotalByMemberCards(@RequestParam(required = true, value = "memberCards") List<String> memberCards,
                                                           @RequestParam(required = false, value = "startDate") Date startDate,
                                                           @RequestParam(required = false, value = "endDate") Date endDate){
        DateTime startDateTime = startDate == null ? null : new DateTime(startDate);
        DateTime endDateTime = endDate == null ? null : new DateTime(endDate);
        List<MemberTotal> data = Collections.emptyList();
        try {
            ComResponse<List<MemberTotal>> response = orderClientAPI.orderFien.queryMemberTotal(memberCards, startDateTime, endDateTime);
            if (response.getCode() == 200) {
                data = response.getData();
            }
        } catch (Exception e) {
            log.error("request order service:queryMemberTotalByMemberCards error",e);
        }
        return data;
    }

    /**
     * 根据会员卡号查询顾客累计消费金额
     * wangzhe
     * 2021-02-26
     * @param memberCard
     * @param startDate
     * @param endDate
     * @return
     */
    public static MemberTotal queryMemberTotalByMemberCard(@RequestParam(required = true, value = "memberCard") String memberCard,
                                                    @RequestParam(required = false, value = "startDate") Date startDate,
                                                    @RequestParam(required = false, value = "endDate") Date endDate){
        List<MemberTotal> memberTotals = queryMemberTotalByMemberCards(Arrays.asList(memberCard), startDate, endDate);
        if (CollectionUtil.isNotEmpty(memberTotals)) {
            return memberTotals.get(0);
        }
        return null;
    }


    /**
     * 通过批量会员卡号获取订单信息
     * wangzhe
     * 2021-02-26
     * @param memberCards
     * @return
     */
    public static List<MemberOrderObject> querymemberorder(List<String> memberCards){
        List<MemberOrderObject> data = null;

        try {
            ComResponse<List<MemberOrderObject>> response = orderClientAPI.orderFien.querymemberorder(memberCards);
            if (response != null && response.getCode() == 200) {
                data = response.getData();
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 通过会员卡号获取订单信息
     * wangzhe
     * 2021-02-26
     * @param memberCard
     * @return
     */
    public static MemberOrderObject querymemberorder(String memberCard){
        List<MemberOrderObject> data = querymemberorder(Arrays.asList(memberCard));
        if (CollectionUtil.isNotEmpty(data)) {
            return data.get(0);
        }
        return null;
    }

    /**
     * 根据顾客卡号查询退单/签收信息
     * @param memberCard
     * @return
     */
    public static OrderTotal4MemberDTO selectOrderTotal4Member(String memberCard){
        OrderTotal4MemberDTO data = null;
        try {
            ComResponse<OrderTotal4MemberDTO> response = orderClientAPI.orderFien.selectOrderTotal4Member(memberCard);
            if (response != null && response.getCode() == 200) {
                data = response.getData();
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return data;
    }


}
