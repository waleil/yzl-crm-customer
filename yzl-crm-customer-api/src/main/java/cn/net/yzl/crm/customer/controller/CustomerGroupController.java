package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CustomerGroupController
 * @description 顾客圈选接口
 * @date: 2021/1/22 1:53 下午
 */
@Api(value="顾客圈选",tags = {"顾客圈选"})
@RestController
@RequestMapping(value = "customerGroup")
public class CustomerGroupController {
    @Autowired
    private CustomerGroupService customerGroupService;

    @ApiOperation("根据一批顾客群组id获取群组信息,用英文逗号分隔")
    @GetMapping("/v1/getCrowdGroupList")
    public ComResponse getCrowdGroupList(
            @RequestParam("crowdGroupIds")
            @NotBlank(message = "crowdGroupIds不能为空")
            @ApiParam(name = "crowdGroupIds", value = "群组id", required = true)
                    String crowdGroupIds) {

        List<String> groupIds = Arrays.asList(crowdGroupIds.split(","));
        List<member_crowd_group> crowdGroupList = customerGroupService.getCrowdGroupByIds(groupIds);
        return ComResponse.success(crowdGroupList);

    }

    @ApiOperation("添加顾客圈选")
    @PostMapping("/v1/addCrowdGroup")
    public ComResponse addCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup) {
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);

        return customerGroupService.saveCustomerCrowdGroup(memberCrowdGroup);
    }

    @ApiOperation("修改顾客圈选")
    @PostMapping("/v1/updateCrowdGroup")
    public ComResponse updateCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup) {
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        try {
            return customerGroupService.updateCustomerCrowdGroup(memberCrowdGroup);
        } catch (Exception exc) {
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE);
        }
    }


    @ApiOperation("分页获取圈选列表")
    @GetMapping("/v1/getCrowdGroupByPage")
    public ComResponse getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {
        if (crowdGroupDTO == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Page<member_crowd_group> page = customerGroupService.getCrowdGroupByPage(crowdGroupDTO);
        return ComResponse.success(page);
    }

    @ApiOperation("根据id获取圈选")
    @GetMapping("/v1/getMemberCrowdGroup")
    public ComResponse getMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        member_crowd_group member_crowd_group = customerGroupService.getMemberCrowdGroup(crowdId);
        return ComResponse.success(member_crowd_group);
    }

}
