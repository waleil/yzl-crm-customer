package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CustomerGroupServiceImpl
 * @description 顾客圈选实现类
 * @date: 2021/1/22 1:58 下午
 */
@Service
public class CustomerGroupServiceImpl implements CustomerGroupService {
    @Autowired
    private MemberCrowdGroupDao memberCrowdGroupDao;
    @Autowired
    private MemberLabelDao memberLabelDao;
    /**
     * @Author: lichanghong
     * @Description: 根据群组编号查询
     * @Date: 2021/1/22 2:01 下午
     * @param groupIds
     * @Return: java.util.List<cn.net.yzl.crm.customer.mongomodel.member_crowd_group>
     */
    @Override
    public List<member_crowd_group> getCrowdGroupByIds(List<String> groupIds) {

        return memberCrowdGroupDao.getMemberCrowdGroupByIds(groupIds);
    }
    /**
     * @Author: lichanghong
     * @Description: 分页查询圈选结果
     * @Date: 2021/1/22 2:00 下午
     * @param crowdGroupDTO
     * @Return: cn.net.yzl.common.entity.Page<cn.net.yzl.crm.customer.mongomodel.member_crowd_group>
     */
    @Override
    public Page<member_crowd_group> getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {
        if (crowdGroupDTO.getCurrentPage() == null || crowdGroupDTO.getCurrentPage() == 0) {
            crowdGroupDTO.setCurrentPage(1);
        }
        if (crowdGroupDTO.getPageSize() == null || crowdGroupDTO.getPageSize() == 0) {
            crowdGroupDTO.setPageSize(10);
        }

        return memberCrowdGroupDao.findCrowdGroupByPage(crowdGroupDTO);
    }

    /**
     * @Author: lichanghong
     * @Description: 保存圈选
     * @Date: 2021/1/22 2:01 下午
     * @param member_crowd_group
     * @Return: void
     */
    @Override
    public ComResponse saveCustomerCrowdGroup(member_crowd_group member_crowd_group) {
        member_crowd_group.setCreate_time(MongoDateHelper.getMongoDate(new Date()));
        memberCrowdGroupDao.saveMemberCrowdGroup(member_crowd_group);
        return ComResponse.success();
    }

    /**
     * @Author: lichanghong
     * @Description: 查询圈选详情
     * @Date: 2021/1/22 2:03 下午
     * @param crowdId
     * @Return: cn.net.yzl.crm.customer.mongomodel.member_crowd_group
     */
    @Override
    public member_crowd_group getMemberCrowdGroup(String crowdId) {

        return memberCrowdGroupDao.getMemberCrowdGroup(crowdId);
    }
    /**
     * @Author: lichanghong
     * @Description: 修改圈选结果
     * @Date: 2021/1/22 2:03 下午
     * @param member_crowd_group
     * @Return: void
     */
    @Override
    public ComResponse updateCustomerCrowdGroup(member_crowd_group member_crowd_group) throws Exception {
        member_crowd_group old = getMemberCrowdGroup(member_crowd_group.get_id());
        if(old.getEnable()!=null && old.getEnable()==1){
            return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(),"已经生效的规则，无法编辑!");
        }
        member_crowd_group.setCreate_time(MongoDateHelper.getMongoDate(old.getCreate_time()));
        member_crowd_group.setCreate_code(old.getCreate_code());
        member_crowd_group.setUpdate_time(MongoDateHelper.getMongoDate(new Date()));
        memberCrowdGroupDao.saveMemberCrowdGroup(member_crowd_group);
       return ComResponse.success();
    }
    /**
     * @Author: lichanghong
     * @Description: 修改圈选状态
     * @Date: 2021/1/22 8:21 下午
     * @param vo
     * @Return: cn.net.yzl.common.entity.ComResponse
     */
    @Override
    public ComResponse updateCustomerCrowdGroupStatus(UpdateCrowdStatusVO vo) {

        return memberCrowdGroupDao.updateCustomerCrowdGroupStatus(vo);
    }
    /**
     * @Author: lichanghong
     * @Description:  查询圈选规则
     * @Date: 2021/1/22 8:20 下午
     * @param
     * @Return: cn.net.yzl.common.entity.ComResponse<cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO>
     */
    @Override
    public ComResponse<List<CustomerCrowdGroupVO>> query4Select() {
        List<CustomerCrowdGroupVO> list = memberCrowdGroupDao.query4Select();
        return ComResponse.success(list);
    }

    @Override
    public int memberCrowdGroupTrial(member_crowd_group memberCrowdGroup) {

        return memberLabelDao.memberCrowdGroupTrial(memberCrowdGroup);
    }
}
