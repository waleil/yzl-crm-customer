package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.service.MemberTypeService;
import cn.net.yzl.crm.customer.vo.MemberTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberTypeController
 * @description 顾客类别查询
 * @date: 2021/2/3 5:15 下午
 */
@Api(value = "顾客类型", tags = {"顾客类型"})
@RestController("/memberType")
public class MemberTypeController {
    @Autowired
    private MemberTypeService memberTypeService;
    @ApiOperation(value = "顾客类别查询", notes = "顾客类别查询")
    @GetMapping("/v1/queryMemberType")
    public ComResponse<List<MemberTypeVO>> queryMemberType(){
        List<MemberTypeVO> list = memberTypeService.queryMemberType();
    return ComResponse.success(list);
    }
}
