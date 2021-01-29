package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CustomerGroupService
 * @description 顾客圈选接口
 * @date: 2021/1/22 1:55 下午
 */
public interface CustomerGroupService {
    /**
     * //     * 根据圈选id批量获取圈选
     * //     * @return
     * //
     */
    List<member_crowd_group> getCrowdGroupByIds(List<String> groupIds);


    Page<member_crowd_group> getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO);

    ComResponse saveCustomerCrowdGroup(member_crowd_group member_crowd_group);

    /**
     * 根据crowd_id获取一个圈选
     *
     * @param crowdId
     * @return
     */
    member_crowd_group getMemberCrowdGroup(String crowdId);

    ComResponse updateCustomerCrowdGroup(member_crowd_group member_crowd_group) throws Exception;
    /**
     * @Author: lichanghong
     * @Description: 修改圈选规则状态
     * @Date: 2021/1/22 1:57 下午
     * @Return:
     */
    ComResponse updateCustomerCrowdGroupStatus(UpdateCrowdStatusVO vo);

    ComResponse<List<CustomerCrowdGroupVO>> query4Select();

    int memberCrowdGroupTrial(member_crowd_group memberCrowdGroup);

    int memberCrowdGroupRun(member_crowd_group memberCrowdGroup);
    /**
     * @Author: lichanghong
     * @Description: 根据顾客编号查询顾客所属圈选群
     * @Date: 2021/1/28 12:59 上午
     * @param memberCard
     * @Return: java.lang.String
     */
     String queryGroupIdByMemberCard(String memberCard);
    /**
     * @Author: lichanghong
     * @Description: 根据分组编号查询关联的顾客
     * @Date: 2021/1/28 12:54 上午
     * @param groupId
     * @Return: java.util.List<cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember>
     */
     List<GroupRefMember> queryMembersByGroupId(String groupId);

}
