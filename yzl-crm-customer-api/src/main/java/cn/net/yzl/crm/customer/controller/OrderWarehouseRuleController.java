//package cn.net.yzl.crm.customer.controller;
//
//
//import cn.net.yzl.common.entity.GeneralResult;
//import cn.net.yzl.crm.customer.dto.OrderWarehourceRuleUpdateDTO;
//import cn.net.yzl.crm.customer.dto.OrderWarehouseRuleSelectDTO;
//import cn.net.yzl.crm.customer.model.OrderWarehouseRule;
//import cn.net.yzl.crm.customer.service.orderWarehouseRule.IOrderWarehouseRuleService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.constraints.NotBlank;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Api(tags = "订单分库规则管理")
//@RestController
//@RequestMapping(value = OrderWarehouseRuleController.PATH)
//public class OrderWarehouseRuleController {
//    public static final String PATH = "order/OrderWarehouseRule";
//
//    @Autowired
//    private IOrderWarehouseRuleService orderWarehouseRuleService;
//
//
//    /**
//     * @Description 订单分仓列表
//     * @Author jingweitao
//     * @Date 15:36 2020/12/8
//     * @Param [pm]
//     **/
//    @ApiOperation(value="订单分仓列表")
//    @GetMapping("list")
//    public GeneralResult<List<OrderWarehouseRuleSelectDTO>> list() {
//        orderWarehouseRuleService.selectById("1");
//        OrderWarehouseRule orderWarehouseRule = orderWarehouseRuleService.selectById("1");
//        OrderWarehouseRuleSelectDTO d = new OrderWarehouseRuleSelectDTO();
//        BeanUtils.copyProperties(orderWarehouseRule, d);
//        return GeneralResult.success(Collections.singletonList(d));
//    }
//
//    /**
//     * @Description 点击设置，根据ruleId，返回覆盖省份list
//     * @Author jingweitao
//     * @Date 15:42 2020/12/8
//     * @Param [pm]
//     * @return java.util.List<cn.net.yzl.order.api.model.OrderWarehouseRule>
//     **/
//    @ApiOperation(value="订单分仓规则，跳转设置；这个接口应该是需要调用公用common服务")
//    @PostMapping("goUpdate")
//    public GeneralResult<List<String>> goUpdate(
//            @RequestParam("ruleId")
//            @NotBlank(message="订单分仓表id不能为空")
//            @ApiParam(name="ruleId",value="订单分仓规则表id",required=true)  String ruleId) {
//        return GeneralResult.success(Arrays.asList("山东","河南","河北"));
//    }
//
//
//    /**
//     * @Description 提交订单分仓规则 覆盖省份的设置 提交
//     * @Author jingweitao
//     * @Date 15:39 2020/12/8
//     * @Param [pm]
//     * @return java.util.List<cn.net.yzl.order.api.model.OrderWarehouseRule>
//     **/
//    @ApiOperation(value="订单分仓规则更新")
//    @RequestMapping(value = "/update", method = { RequestMethod.POST })
//    public GeneralResult<Boolean> update(@RequestBody @ApiParam(name="ruleId",value="订单分仓规则表id",required=true) OrderWarehourceRuleUpdateDTO update) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//
//
//
//
//}
