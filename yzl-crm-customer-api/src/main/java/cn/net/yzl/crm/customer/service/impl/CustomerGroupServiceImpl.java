package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    @Autowired
    private RedisUtil redisUtil;

    private final static Integer SAVE_LINE = 10_000;

    //缓存圈选群组id，顾客编号
    private static Map<String, HashSet<String>> cacheMap = new ConcurrentHashMap<>();
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
        if (old == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录不存在!");
        }
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


    @Override
    public String queryGroupIdByMemberCard(String memberCard) {
        return memberLabelDao.queryGroupIdByMemberCard(memberCard);
    }

    @Override
    public List<GroupRefMember> queryMembersByGroupId(String groupId) {
        return memberLabelDao.queryMembersByGroupId(groupId);
    }

    //@Transactional (transactionManager = "mongoTransactionManager")
    //@Transactional(transactionManager = "mongoTransactionManager",rollbackFor = Throwable.class)
    @Override
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup) {
        List<MemberLabel> labels = memberLabelDao.memberCrowdGroupRun(memberCrowdGroup);
      /*  return generateGroupRun(memberCrowdGroup.get_id(), labels);
    }
    @Transactional (transactionManager = "mongoTransactionManager")
    public Integer generateGroupRun(String groupId,List<MemberLabel> labels){*/
        String groupId = memberCrowdGroup.get_id();

        List<GroupRefMember> list = new ArrayList<>(labels.size());
        String memberCard;
        MemberLabel label;
        //删除mongo里面的当前groupId对应的历史数据
        boolean result = deleteMongoGroupRefMemberByGroupId(groupId);
        if (!result) {
            return 0;
        }
        //生成当前groupId对应的数据，并保存至mongo
        for (int i = 0,lengh = labels.size(); i < lengh; i++) {
            label = labels.get(i);
            memberCard = label.getMemberCard();
            //判读是否存在
            if (getAndSetNxInGroupCache(groupId,memberCard)) {
                continue;
            }
            GroupRefMember member = new GroupRefMember();
            member.setGroupId(groupId);
            member.setMemberCard(label.getMemberCard());
            member.setMemberName(label.getMemberName());
            list.add(member);
            if (isSaveLine(i)) {
                memberLabelDao.insertAll(list);
                list.clear();
            }
        }
        //保存小于10000条对象的集合
        if (CollectionUtil.isNotEmpty(list)) {
            memberLabelDao.insertAll(list);
        }
        //删除redis中的对应的缓存
        String cacheKey = CacheKeyUtil.groupRunCacheKey(groupId);
        redisUtil.del(cacheKey);

        //返回本次同步数据的条数
        return labels.size();
    }

    /**
     * 根据groupId删除数据
     * wangzhe
     * 2021-01-29
     * @param groupId 圈选分组id
     * @return
     */
    public boolean deleteMongoGroupRefMemberByGroupId(String groupId){
        if (StringUtils.isEmpty(groupId)) {
            return false;
        }
        //查询出符合条件的第一个结果，并将符合条件的数据删除
        Query query = Query.query(Criteria.where("groupId").is(groupId));
        /*DeleteResult remove = */memberLabelDao.remove(query, GroupRefMember.class);
        //删除redis中的对应的缓存
        String cacheKey = CacheKeyUtil.groupRunCacheKey(groupId);
        redisUtil.del(cacheKey);
        return true;
    }

    /**
     * 判读是否存在，不存在则设置
     * wangzhe
     * 2021-01-29
     * @param groupId 群组id
     * @param memberCard 会员卡号
     * @return 当前是否存在缓存
     */
    private Boolean getAndSetNxInGroupCache(String groupId,String memberCard){
        boolean isFind = false;
        //是否包含群组id
        /*if (cacheMap.containsKey(groupId)) {
            //群组下是否包含该客户编号
            if (cacheMap.get(groupId).contains(memberCard)) {
                isFind = true;
            }else{
                //不存在则设置
                cacheMap.get(groupId).add(memberCard);
            }
        }else{
            HashSet<String> set = new HashSet<>();
            set.add(memberCard);
            cacheMap.put(groupId, set);
        }*/
        //cacheMap中不存在，则去redis中去寻找
        String cacheKey = CacheKeyUtil.groupRunCacheKey(groupId);
        if (redisUtil.sHasKey(cacheKey,memberCard)) {
            isFind = true;
        }else{
            //不存在则设置
            redisUtil.sSet(cacheKey, memberCard);
        }
        return isFind;
    }

    /**
     * 判断是否需要保存一次
     * wangzhe
     * 2021-01-29
     * @param index 当先的下标
     * @return
     */
    private Boolean isSaveLine(Integer index){
        return index > 0 && index % SAVE_LINE == 0;
    }

}
