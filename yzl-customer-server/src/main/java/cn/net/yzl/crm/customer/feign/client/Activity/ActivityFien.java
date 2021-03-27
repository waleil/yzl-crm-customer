package cn.net.yzl.crm.customer.feign.client.Activity;

import cn.net.yzl.activity.model.requestModel.SendCouponByMemberLevelRequest;
import cn.net.yzl.activity.model.responseModel.*;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.model.PageParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name = "yzl-activity-db")
@FeignClient(name = "activityFien", url = "${api.gateway.url}/activityDB")
public interface ActivityFien {

    @ApiOperation(value = "通过活动编号查询活动商品信息")
    @GetMapping("db/v1/productSales/getProductListByActivityBusNo")
    public ComResponse<List<ActivityProductResponse>> getProductListByActivityBusNo(@RequestParam("activityBusNo") Long activityBusNo);

    @ApiOperation(value = "通过活动编号查询活动商品信息")
    @PostMapping("/db/v1/productSales/getListByBusNos")
    public ComResponse<List<ActivityDetailResponse>> getListByBusNos(List<Integer> activityBusNo);



    @ApiOperation(value = "会员管理-会员级别管理-会员级别设置列表")
    @PostMapping("/db/v1/memberLevelManager/getMemberLevelPages")
    public ComResponse<Page<MemberLevelPagesResponse>> getMemberLevelPages(PageParam page);

    @ApiOperation(value = "会员管理-会员级别管理-根据id查看详情")
    @GetMapping("/db/v1/memberLevelManager/getById")
    public ComResponse<MemberLevelDetailResponse> getGrandById(@RequestParam("id")Integer id);


    @ApiOperation(value = "根据单个会员卡号获取 每个顾客的优惠券 积分 红包")
    @GetMapping("/db/v1/getAccountByMemberCard")
    public ComResponse<MemberAccountResponse> getAccountByMemberCard(@RequestParam("memberCard")String memberCard);

    @ApiOperation(value = "会员管理-会员级别管理-查看会员等级有效期限详情")
    @GetMapping("/db/v1/memberLevelManager/getMemberSysParamByType")
    public ComResponse<MemberSysParamDetailResponse> getMemberSysParamByType(@RequestParam("type") Integer type);

    @ApiOperation(value = "优惠券-会员级别升级送优惠券")
    @GetMapping("/db/v1/memberCoupon/sendCouponByMemberLevel")
    public ComResponse<Boolean> sendCouponByMemberLevel(@RequestBody SendCouponByMemberLevelRequest request);

}