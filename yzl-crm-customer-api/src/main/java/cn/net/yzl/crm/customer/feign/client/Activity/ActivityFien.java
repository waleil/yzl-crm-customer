package cn.net.yzl.crm.customer.feign.client.Activity;

import cn.net.yzl.activity.model.responseModel.ActivityProductResponse;
import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "activityDB", url = "http://192.168.32.85:8099/activityDB")
public interface ActivityFien {

    @ApiOperation(value = "通过活动编号查询活动商品信息")
    @GetMapping("db/v1/productSales/getProductListByActivityBusNo")
    public ComResponse<List<ActivityProductResponse>> getProductListByActivityBusNo(@RequestParam("activityBusNo") Long activityBusNo);



}