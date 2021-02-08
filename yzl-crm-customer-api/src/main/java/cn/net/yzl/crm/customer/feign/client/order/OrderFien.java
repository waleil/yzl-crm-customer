package cn.net.yzl.crm.customer.feign.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.memberOrderObject;
import cn.net.yzl.order.model.vo.member.MemberOrder;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yzl-order-server")
public interface OrderFien {

    @GetMapping("/orderSearch/v1/selectOrderInfo")
    public ComResponse<List<OrderInfoResDTO>> selectOrderInfo(@RequestParam String orderNo);

    @GetMapping("/order/v1/querymemberorder")
    public ComResponse<List<memberOrderObject>> querymemberorder(@RequestParam List<String> memberCards);

}

