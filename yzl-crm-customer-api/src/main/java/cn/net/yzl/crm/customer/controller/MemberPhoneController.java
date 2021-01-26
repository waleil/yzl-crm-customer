package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.service.MemberPhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memberPhone")
@Api(value = "顾客手机号", tags = {"顾客手机号服务"})
@Validated
public class MemberPhoneController {

    @Autowired
    private MemberPhoneService memberPhoneService;

    @ApiOperation(value = "顾客手机号-获取顾客会员号", notes = "顾客账户-获取顾客会员号")
    @RequestMapping(value = "/v1/getMemberCard", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "电话号码", required = true, dataType = "string"),
    })
    ComResponse<String> getMemberCard(@RequestParam("phoneNumber") String  phoneNumber) {
        return memberPhoneService.getMemberCard(phoneNumber);
    }

}
