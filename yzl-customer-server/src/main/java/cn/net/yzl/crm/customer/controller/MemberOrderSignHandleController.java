package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.PageDTO;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;
import cn.net.yzl.crm.customer.service.MemberOrderSignHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/memberAddress")
@Api(value = "MemberOrderSignHandleController", tags = {"订单签收后推送消息处理类"})
@Validated
public class MemberOrderSignHandleController {

    @Autowired
    private MemberOrderSignHandleService memberOrderSignHandleService;

    @ApiOperation(value = "订单签收后推送消息-手动处理失败的消息", notes = "订单签收后推送消息-手动处理失败的消息")
    @RequestMapping(value = "v1/dealFailRecord", method = RequestMethod.POST)
    public ComResponse<Boolean> dealFailRecord(@RequestParam Integer primaryKey) throws IllegalAccessException {
        if (primaryKey == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "参数不能为空");
        }
        Boolean result = memberOrderSignHandleService.dealFailRecord(primaryKey);
        return ComResponse.success(Boolean.TRUE);
    }


    @ApiOperation(value = "订单签收后推送消息-获取处理失败的消息列表", notes = "订单签收后推送消息-获取处理失败的消息列表")
    @RequestMapping(value = "v1/getFailRecordList", method = RequestMethod.POST)
    public ComResponse<Page<MemberOrderSignHandle>>  getFailRecordList(PageDTO page) throws IllegalAccessException {
        if (page == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "参数不能为空");
        }
        Page<MemberOrderSignHandle> pageList = memberOrderSignHandleService.getFailRecordList(page);
        return ComResponse.success(pageList);
    }

    @ApiOperation(value = "订单签收后推送消息-修改订单签收失败消息记录", notes = "订单签收后推送消息-获取处理失败的消息列表")
    @RequestMapping(value = "v1/updateSignHandleRecord", method = RequestMethod.POST)
    public ComResponse<Boolean>  updateByPrimaryKeySelective(@RequestBody  MemberOrderSignHandle signHandle) throws IllegalAccessException {
        memberOrderSignHandleService.updateByPrimaryKeySelective(signHandle);
        return ComResponse.success(Boolean.TRUE);
    }



}
