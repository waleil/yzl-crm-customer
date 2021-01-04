package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("顾客服务")
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    MemberService memberService;

    @ApiOperation("获取顾客基本信息")
    @GetMapping("v1/getCustomerInfo")
    public GeneralResult getCustomerInfo() {
        return GeneralResult.success(memberService.getMemberGrad());
    }
    @ApiOperation("获取顾客级别")
    @GetMapping("v1/getMemberGrad")
    public GeneralResult getMemberGrad(){
        return GeneralResult.success(memberService.getMemberGrad());
    }
}
