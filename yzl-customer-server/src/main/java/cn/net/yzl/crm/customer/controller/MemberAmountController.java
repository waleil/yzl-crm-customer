package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;

import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/member/customerAmount")
@Api(value = "顾客账户信息", tags = {"顾客账户信息"})
@Validated
public class MemberAmountController {

    @Autowired
    private MemberAmountService memberAmountService;

    @ApiOperation(value = "顾客账户-获取顾客账户信息", notes = "顾客账户-获取顾客账户信息")
    @RequestMapping(value = "/getMemberAmount", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
    })
    ComResponse<MemberAmountDto> getMemberAmount(@RequestParam("memberCard") String  memberCard) {
        return memberAmountService.getMemberAmount(memberCard);
    }

    @ApiOperation(value = "顾客账户-获取余额明细", notes = "顾客账户-获取余额明细")
    @RequestMapping(value = "/getMemberAmountDetailList", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "timeFlag", value = "时间标识(1:最近三个月,2:三个月以前的)", required = true, dataType = "Int", paramType = "query"),
    })
    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(@RequestParam("memberCard") String  memberCard,@RequestParam("timeFlag") Integer timeFlag) throws ParseException {
        return memberAmountService.getMemberAmountDetailList(memberCard,timeFlag);
    }



    @ApiOperation(value = "顾客账户-账户冻结操作(充值,消费,退回)", notes = "顾客账户-账户冻结操作(充值,消费,退回)")
    @RequestMapping(value = "/operation", method = RequestMethod.POST)
    ComResponse<String> operation(@RequestBody @Validated MemberAmountDetailVO memberAmountDetailVO) throws ParseException {
        return memberAmountService.operation(memberAmountDetailVO);
    }

    @ApiOperation(value = "顾客账户-冻结金额确认(消费/退回)", notes = "顾客账户-冻结金额确认(消费/退回)")
    @RequestMapping(value = "/operationConfirm", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "obtainType", value = "操作类型(1 退回 2 消费,3:充值(目前先不考虑)", required = true, dataType = "string", paramType = "query")
    })
    ComResponse<String> operationConfirm(@RequestParam("obtainType") @Min(1) @Max(2) int obtainType, @RequestParam("orderNo") @NotBlank String orderNo) throws ParseException {
        return memberAmountService.operationConfirm(obtainType,orderNo);
    }
}
