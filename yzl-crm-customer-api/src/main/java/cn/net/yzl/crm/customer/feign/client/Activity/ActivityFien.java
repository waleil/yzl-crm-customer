//package cn.net.yzl.crm.customer.feign.client.Activity;
//
//import cn.net.yzl.activity.model.responseModel.ActivityProductResponse;
//import cn.net.yzl.common.entity.ComResponse;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@FeignClient(name = "yzl-activity-db")
//public interface ActivityFien {
//
//    @ApiOperation(value = "通过活动编号查询活动商品信息")
//    @GetMapping("db/v1/productSales/getProductListByActivityBusNo")
//    public ComResponse<List<ActivityProductResponse>> getProductListByActivityBusNo(@RequestParam("activityBusNo") Long activityBusNo);
//
//
//
//}