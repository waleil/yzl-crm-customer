package cn.net.yzl.crm.customer.collector.client.product;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yzl-product-server")
public interface ProductFien {

    @ApiOperation("根据商品编号获取商品信息")
    @GetMapping("product/v1/queryByProductCodes")
    ComResponse<List<ProductMainDTO>> queryByProductCodes(@RequestParam("codes") String[] codes);

}