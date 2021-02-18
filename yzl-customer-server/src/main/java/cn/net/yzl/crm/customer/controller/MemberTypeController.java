package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberTypeDTO;
import cn.net.yzl.crm.customer.service.MemberTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberTypeController
 * @description 顾客类别查询
 * @date: 2021/2/3 5:15 下午
 */
@RestController
@RequestMapping("/memberType")
@Api(value = "顾客类型", tags = {"顾客类型"})
@Validated
public class MemberTypeController {

    @Autowired
    private MemberTypeService memberTypeService;


    @ApiOperation(value = "顾客类别查询", notes = "顾客类别查询")
    @RequestMapping(value = "/v1/queryMemberType", method = RequestMethod.GET)
    public ComResponse<List<MemberTypeDTO>> queryMemberType() {
        List<MemberTypeDTO> list = memberTypeService.queryMemberType();
    return ComResponse.success(list);
    }
}
