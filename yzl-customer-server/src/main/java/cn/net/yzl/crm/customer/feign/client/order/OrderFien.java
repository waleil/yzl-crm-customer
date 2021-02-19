package cn.net.yzl.crm.customer.feign.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.MemberOrderObject;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yzl-order-server")
public interface OrderFien {

//    @GetMapping("/orderSearch/v1/selectOrderInfo")
//    public ComResponse<List<OrderInfoResDTO>> selectOrderInfo(@RequestParam String orderNo);

    @GetMapping("/order/v1/querymemberorder")
    public ComResponse<List<MemberOrderObject>> querymemberorder(@RequestParam("memberCards") List<String> memberCards);

    @GetMapping("/order/v1/querymembertotal")
    @ApiOperation(value = "统计本年度累计消费金额、本年度最高消费金额、本年度最高预存金额", notes = "统计本年度累计消费金额、本年度最高消费金额、本年度最高预存金额")
    public ComResponse<List<MemberTotal>> queryMemberTotal(@RequestParam(required = true, value = "memberCards") List<String> memberCards);

}

