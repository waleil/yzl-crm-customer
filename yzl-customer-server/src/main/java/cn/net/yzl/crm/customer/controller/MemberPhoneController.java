package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.service.MemberPhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memberPhone")
@Api(value = "顾客手机号", tags = {"顾客手机号服务"})
@Validated
public class MemberPhoneController {

    @Autowired
    private MemberPhoneService memberPhoneService;

    @ApiOperation(value = "顾客手机号-获取顾客会员号", notes = "顾客账户-获取顾客会员号")
    @RequestMapping(value = "/v1/getMemberCardByphoneNumber", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "电话号码", required = true, dataType = "string"),
    })
    ComResponse<String> getMemberCardByphoneNumber(@RequestParam("phoneNumber") String  phoneNumber) {
        return memberPhoneService.getMemberCardByphoneNumber(phoneNumber);
    }

    @ApiOperation(value = "顾客手机号-获取顾客会员基本信息")
    @GetMapping("v1/getMemberByphoneNumber")
    public ComResponse<Member> getMemberByphoneNumber(@RequestParam("phoneNumber") String  phoneNumber) {
        return memberPhoneService.getMemberByphoneNumber(phoneNumber);
    }

    @ApiOperation(value = "顾客手机号-根据多个电话号获取顾客会员卡号集合")
    @GetMapping("v1/getMemberCardByphoneNumbers")
    public ComResponse<List<String>> getMemberCardByphoneNumbers(@RequestParam("phoneNumbers") List<String>  phoneNumbers) {
        List<String> memCards = memberPhoneService.getMemberCardByphoneNumbers(phoneNumbers);
        return ComResponse.success(memCards);
    }

}
