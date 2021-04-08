package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;

import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import io.swagger.annotations.*;
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

//    @ApiOperation(value = "顾客账户-获取余额明细", notes = "顾客账户-获取余额明细")
//    @RequestMapping(value = "/getMemberAmountDetailList", method = RequestMethod.GET)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
//            @ApiImplicitParam(name = "timeFlag", value = "时间标识(1:最近三个月,2:三个月以前的)", required = true, dataType = "Int", paramType = "query"),
//    })
//    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(@RequestParam("memberCard") String  memberCard,@RequestParam("timeFlag") Integer timeFlag) throws ParseException {
//        return memberAmountService.getMemberAmountDetailList(memberCard,timeFlag);
//    }

    @ApiOperation(value = "顾客账户-获取余额明细(分页)", notes = "顾客账户-获取余额明细(分页)")
    @RequestMapping(value = "v1/getMemberAmountDetailListByPage", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "顾客卡号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "当前页", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示的条数", required = true, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "timeFlag", value = "时间标识(1:最近三个月,2:三个月以前的)", required = true, dataType = "Int", paramType = "query"),
    })
    ComResponse<Page<MemberAmountDetailDto>> getMemberAmountDetailListByPage(@RequestParam("memberCard") String  memberCard,
                                                                             @RequestParam("pageNo") Integer  pageNo,
                                                                             @RequestParam("pageSize") Integer  pageSize,
                                                                             @RequestParam("timeFlag") Integer timeFlag) throws ParseException {

        if (timeFlag == null || (timeFlag != 1 && timeFlag != 2)){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "timeFlag 参数错误!");
        }
        Page<MemberAmountDetailDto> page = memberAmountService.getMemberAmountDetailListByPage(memberCard, pageNo, pageSize, timeFlag);

        return ComResponse.success(page);
    }


    @ApiOperation(value = "顾客账户-通过顾客卡号和订单号集合获取余额明细", notes = "顾客账户-通过顾客卡号和订单号集合获取余额明细")
    @RequestMapping(value = "/getMemberAmountDetailsBymemberCardAndOrderList", method = RequestMethod.POST)
    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailsBymemberCardAndOrderList(@RequestParam("memberCard") String  memberCard,@RequestBody List<String> orderList) throws ParseException {
        List<MemberAmountDetailDto> list = memberAmountService.getMemberAmountDetailsBymemberCardAndOrderList(memberCard, orderList);
        return ComResponse.success(list);
    }



    @ApiOperation(value = "顾客账户-账户操作(1.退回,2.消费,3.充值)", notes = "顾客账户-账户操作(1.退回,2.消费,3.充值)")
    @RequestMapping(value = "/operation", method = RequestMethod.POST)
    ComResponse<String> operation(@RequestBody @Validated MemberAmountDetailVO memberAmountDetailVO) throws ParseException {
        return memberAmountService.operation(memberAmountDetailVO);
    }

    @ApiOperation(value = "顾客账户-消费金额确认", notes = "顾客账户-消费金额确认")
    @RequestMapping(value = "/operationConfirm", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "obtainType", value = "操作类型(1 退回(目前不支持) 2 消费,3:充值(目前不支持)", required = true, dataType = "string", paramType = "query")
    })
    ComResponse<String> operationConfirm(@RequestParam("obtainType") @Min(1) @Max(2) int obtainType, @RequestParam("orderNo") @NotBlank String orderNo) throws ParseException {
        return memberAmountService.operationConfirm(obtainType,orderNo);
    }

}
