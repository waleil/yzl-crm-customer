package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.ReveiverAddress;
import cn.net.yzl.crm.customer.service.MemberAddressService;
import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.utils.BeanUtil;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/memberAddress")
@Api(value = "顾客收货地址信息", tags = {"顾客收货地址"})
@Validated
public class MemberAddressController {

    @Autowired
    private MemberAddressService memberAddressService;

    @ApiOperation(value = "顾客收货地址-添加顾客收货地址", notes = "顾客收货地址-添加顾客收货地址")
    @RequestMapping(value = "v1/addReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> addReveiverAddress(@RequestBody  @Validated ReveiverAddressInsertVO reveiverAddressInsertVO) throws IllegalAccessException {
        reveiverAddressInsertVO = BeanUtil.setNullValue(reveiverAddressInsertVO);
        return memberAddressService.addReveiverAddress(reveiverAddressInsertVO);
    }

    @ApiOperation(value = "顾客收货地址-更新收货地址", notes = "顾客收货地址-更新收货地址")
    @RequestMapping(value = "v1/updateReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> updateReveiverAddress(@RequestBody @Validated ReveiverAddressUpdateVO reveiverAddressUpdateVO) throws IllegalAccessException {
        reveiverAddressUpdateVO = BeanUtil.setNullValue(reveiverAddressUpdateVO);
        return memberAddressService.updateReveiverAddress(reveiverAddressUpdateVO);
    }

    @ApiOperation(value = "顾客收货地址-获取顾客收货地址", notes = "顾客收货地址-获取顾客收货地址")
    @RequestMapping(value = "v1/getReveiverAddress", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard) {
        return memberAddressService.getReveiverAddress(memberCard);
    }
}
