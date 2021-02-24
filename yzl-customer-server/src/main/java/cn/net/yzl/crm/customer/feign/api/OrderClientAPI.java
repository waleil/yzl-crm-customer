package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class OrderClientAPI {

    @Autowired
    OrderFien orderFien;


    /**
     * 查询多个顾客累计消费金额
     * @param memberCards
     * @param startDate
     * @param endDate
     * @return
     */
    public List<MemberTotal> queryMemberTotalByMemberCards(@RequestParam(required = true, value = "memberCards") List<String> memberCards,
                                                           @RequestParam(required = false, value = "startDate") Date startDate,
                                                           @RequestParam(required = false, value = "endDate") Date endDate){
        DateTime startDateTime = startDate == null ? null : new DateTime(startDate);
        DateTime endDateTime = endDate == null ? null : new DateTime(endDate);
        ComResponse<List<MemberTotal>> response = orderFien.queryMemberTotal(memberCards,startDateTime , endDateTime);
        if (response.getCode() != 200) {
            return null;
        }
        return response.getData();
    }

    /**
     * 根据会员卡号查询顾客累计消费金额
     * @param memberCard
     * @param startDate
     * @param endDate
     * @return
     */
    public MemberTotal queryMemberTotalByMemberCard(@RequestParam(required = true, value = "memberCard") String memberCard,
                                                    @RequestParam(required = false, value = "startDate") Date startDate,
                                                    @RequestParam(required = false, value = "endDate") Date endDate){
        List<MemberTotal> memberTotals = queryMemberTotalByMemberCards(Arrays.asList(memberCard), startDate, endDate);
        if (CollectionUtil.isNotEmpty(memberTotals)) {
            return memberTotals.get(0);
        }
        return null;
    }

}
