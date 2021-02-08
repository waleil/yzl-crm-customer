package cn.net.yzl.crm.customer.feign.client.order;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.order.model.vo.order.OrderInfoResDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "yzl-order-server")
public interface OrderFien {

    @GetMapping("/orderSearch/v1/selectOrderInfo")
    public ComResponse<List<OrderInfoResDTO>> selectOrderInfo(@RequestParam String orderNo);

}

