//package cn.net.yzl.crm.customer.feign.client.product;
//
//import cn.net.yzl.common.entity.ComResponse;
//import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.validation.constraints.NotBlank;
//import java.util.List;
//
//@FeignClient(name = "yzl-product-server")
//public interface ProductFien {
//
//    @GetMapping("/v1/queryByProductCodes")
//    ComResponse<List<ProductMainDTO>> queryByProductCodes(@RequestParam @NotBlank String codes);
//
//}