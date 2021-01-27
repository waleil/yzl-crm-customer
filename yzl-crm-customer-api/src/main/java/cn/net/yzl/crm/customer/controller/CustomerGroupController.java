package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ApiOperation("修改圈选规则状态")
    @PostMapping("/v1/updateStatus")
    public ComResponse updateMemberCrowdGroupStatus(@RequestBody @Valid UpdateCrowdStatusVO vo){
        return customerGroupService.updateCustomerCrowdGroupStatus(vo);
    }
    /**
     * @Author: lichanghong
     * @Description:  查询圈选规则
     * @Date: 2021/1/22 8:20 下午
     * @param
     * @Return: cn.net.yzl.common.entity.ComResponse<cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO>
     */
    @ApiOperation("查询顾客圈选列表")
    @GetMapping("/v1/query4Select")
    public ComResponse<List<CustomerCrowdGroupVO>> query4Select(){
        return customerGroupService.query4Select();
    }

    @ApiOperation("圈选试算")
    @PostMapping("/v1/groupTrial")
    public ComResponse<Integer> memberCrowdGroupTrial(@RequestBody member_crowd_group memberCrowdGroup){
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        int count = customerGroupService.memberCrowdGroupTrial(memberCrowdGroup);
        return ComResponse.success(count);
    }
    @ApiOperation("圈选")
    @PostMapping("/v1/groupRun")
    public ComResponse<Integer> memberCrowdGroupRun(@RequestBody member_crowd_group memberCrowdGroup){
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        int count = customerGroupService.memberCrowdGroupRun(memberCrowdGroup);
        return ComResponse.success(count);
    }

    /**
     * @Author: lichanghong
     * @Description: 根据顾客编号查询顾客所属圈选群
     * @Date: 2021/1/28 12:59 上午
     * @param memberCard
     * @Return: java.lang.String
     */
    @ApiOperation("根据顾客编号查询圈选群组编号")
    @GetMapping("/v1/queryGroupIdByMemberCard")
    public ComResponse<String> queryGroupIdByMemberCard(@RequestParam String memberCard){
       return ComResponse.success(customerGroupService.queryGroupIdByMemberCard(memberCard));
    }
    /**
     * @Author: lichanghong
     * @Description: 根据分组编号查询关联的顾客
     * @Date: 2021/1/28 12:54 上午
     * @param groupId
     * @Return: java.util.List<cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember>
     */
    @ApiOperation("根据圈选编号查询顾客")
    @GetMapping("/v1/queryMemberByGroupId")
    public ComResponse<List<GroupRefMember>> queryMembersByGroupId(@RequestParam String groupId){
        return ComResponse.success(customerGroupService.queryMembersByGroupId(groupId));
    }
}
