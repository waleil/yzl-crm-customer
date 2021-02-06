package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.service.MemberAddressService;
import cn.net.yzl.crm.customer.utils.BeanUtil;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member/memberAddress")
@Api(value = "顾客收货地址信息", tags = {"顾客收货地址"})
@Validated
public class MemberAddressController {

    @Autowired
    private MemberAddressService memberAddressService;

    @ApiOperation(value = "顾客收货地址-添加顾客收货地址", notes = "顾客收货地址-添加顾客收货地址")
    @RequestMapping(value = "v1/addReveiverAddress", method = RequestMethod.POST)
    public ComResponse<String> addReveiverAddress(@RequestBody  @Validated ReveiverAddressInsertVO reveiverAddressInsertVO) throws IllegalAccessException {
        if (StringUtils.isEmpty(reveiverAddressInsertVO.getCreateCode()) && StringUtils.isNotEmpty(reveiverAddressInsertVO.getUpdateCode())) {
            reveiverAddressInsertVO.setCreateCode(reveiverAddressInsertVO.getUpdateCode());
        }
        if (StringUtils.isNotEmpty(reveiverAddressInsertVO.getCreateCode()) && StringUtils.isEmpty(reveiverAddressInsertVO.getUpdateCode())) {
            reveiverAddressInsertVO.setUpdateCode(reveiverAddressInsertVO.getCreateCode());
        }
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
