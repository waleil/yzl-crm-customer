package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.label.MemberLabelDto;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.MemberCrowdGroupOpVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import org.springframework.data.mongodb.core.query.Query;

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

    Page<MemberLabelDto>  groupTrialPullData(member_crowd_group memberCrowdGroup);

    int memberCrowdGroupRun(member_crowd_group memberCrowdGroup) throws InterruptedException;

    int memberCrowdGroupRun(member_crowd_group memberCrowdGroup,Long version) throws InterruptedException;

    int memberCrowdGroupTrialById(MemberCrowdGroupOpVO crowdGroupOpVO);

    Page<MemberLabelDto>  groupTrialByIdPullData(MemberCrowdGroupOpVO crowdGroupOpVO);

    int memberCrowdGroupRunById(MemberCrowdGroupOpVO crowdGroupOpVO) throws InterruptedException;
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
    /**
     * @Author: lichanghong
     * @Description: 顾客人群圈选定时任务
     * @Date: 2021/2/1 3:27 下午
     * @param
     * @Return: java.lang.Boolean
     */
    Boolean memberGroupTimedTask() throws InterruptedException;


    boolean isCrowdGroupIncludeMemberCard(member_crowd_group memberCrowdGroup, String memberCard);

    public Boolean memberCrowdGroupRunByLabels(String groupId, List<MemberLabel> labels);

    Query convertMongoCondition(member_crowd_group memberCrowdGroup);

    Query convertIdToMongoCondition(MemberCrowdGroupOpVO crowdGroupOpVO);
}
