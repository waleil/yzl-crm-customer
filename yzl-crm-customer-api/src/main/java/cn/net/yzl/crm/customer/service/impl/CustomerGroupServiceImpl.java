package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.crowd.MemberCrowdGroupOpVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.CacheUtil;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CustomerGroupServiceImpl
 * @description 顾客圈选实现类
 * @date: 2021/1/22 1:58 下午
 */
@Service
@Slf4j
public class CustomerGroupServiceImpl implements CustomerGroupService {
    @Autowired
    private MemberCrowdGroupDao memberCrowdGroupDao;
    @Autowired
    private MemberLabelDao memberLabelDao;

    @Autowired
    private RedisUtil redisUtil;

    private final static Integer SAVE_LINE = 15_000;

    //5小时
    private final static Integer REDIS_GROUP_RUN_CACHE_TIME = 5 * 60 * 60;

    private final static String COLLECTION_NAME_MEMBER_LABEL = "member_label";
    private final static String COLLECTION_NAME_GROUP_REF_MEMBER= "group_ref_member";

    //缓存圈选群组id，顾客编号
    private static Map<String, HashSet<String>> cacheMap = new ConcurrentHashMap<>();

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4,128,1000L,
            TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<Runnable>(1024));

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

    @Override
    public Boolean memberGroupTimedTask() {
        List<member_crowd_group> list =memberCrowdGroupDao.query4Task();
        //多线程执行
        for(member_crowd_group memberCrowdGroup:list){
            threadPoolExecutor.execute(()->{ memberCrowdGroupRun(memberCrowdGroup);});
        }
        return true;
    }

    /**
     * 匹配全选规则，生成新的数据
     * @param memberCrowdGroup
     * @return
     */
    @Override
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup) {
        long groupRunStartTime = System.currentTimeMillis();
        //生成数据的版本号
        Long version = Long.parseLong(DateUtil.format(new Date(),"yyyyMMdd"));
        String groupId = memberCrowdGroup.get_id();
        Integer pageNo = 1;
        Integer pageSize = SAVE_LINE;
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        List<MemberLabel> labels = null;
        Page<MemberLabel> memberLabelPage;
        //获取记录的总条数
        boolean hasNext = true;
        int matchCount = 0;
        while (hasNext) {
            //分页查询
            memberLabelPage = memberLabelDao.memberCrowdGroupRunUsePage(pageNo, pageSize, query);
            labels = memberLabelPage.getItems();
            if(!CollectionUtils.isEmpty(labels)){
            PageParam pageParam = memberLabelPage.getPageParam();
            log.info("memberCrowdGroupRun-page query:groupId:{},本次分页,pageNo:{},圈选出{}条记录,版本号为:{}",groupId,pageParam.getPageNo(),labels.size(),version);
            //处理数据
            matchCount += doMemberCrowdGroupRun(groupId, labels,version);
            labels.clear();
            //判断是否有下一页
            if (pageParam.getNextPage() == 0) {
                hasNext = false;
            }
            pageNo = pageParam.getNextPage();
            }else{
                break;
            }
        }

        //删除mongo里面的当前groupId对应的历史数据(删除非当前版本的数据)
        deleteMongoGroupRefMemberByGroupId(groupId,version);
        //设置本次匹配到圈选规则的条数
        Query updateCondition = new Query();
        updateCondition.addCriteria(Criteria.where("_id").is(groupId));
        Update update = new Update();
        update.set("person_count",matchCount);
        memberLabelDao.updateFirst(updateCondition, update,member_crowd_group.class);
        long groupRunEndTime = System.currentTimeMillis();
        log.info("memberCrowdGroupRun-end:groupId:{},本次圈选出{}条记录,版本号为:{} member_crowd_group 已更新,本次圈选总耗时:{}",groupId,matchCount,version,(groupRunEndTime-groupRunStartTime));
        return matchCount;
    }


    /**
     * wangzhe
     * 2021-01-30
     * @param groupId
     * @param labels
     * @return
     */
    public int doMemberCrowdGroupRun(String groupId,List<MemberLabel> labels,Long version) {
        int length = labels.size();
        List<GroupRefMember> list = new ArrayList<>(labels.size());
        String memberCard;
        MemberLabel label;
        //重复的数量
        int repeatFilterCount = 0;
        //生成当前groupId对应的数据，并保存至mongo
        for (int i = 0; i < length; i++) {
            label = labels.get(i);
            memberCard = label.getMemberCard();
            //判读是否存在
            if (getAndSetNxInGroupCache(memberCard)) {
                repeatFilterCount++;
                continue;
            }
            GroupRefMember member = new GroupRefMember();
            member.setGroupId(groupId);
            member.setMemberCard(label.getMemberCard());
            member.setMemberName(label.getMemberName());
            member.setVersion(version);
            list.add(member);
            /*if (isSaveLine(i)) {
                memberLabelDao.insertAll(list,COLLECTION_NAME_MEMBER_LABEL);
                log.info("memberCrowdGroupRun-save:groupId:{},本次保存{}条记录,因重复圈选当前过滤:{}条记录,版本号为:{}",groupId,length,list.size(),repeatFilterCount,version);
                list.clear();
            }*/
        }
        //保存集合
        if (CollectionUtil.isNotEmpty(list)) {
            memberLabelDao.insertAll(list,COLLECTION_NAME_GROUP_REF_MEMBER);
            log.info("memberCrowdGroupRun-save:groupId:{},本次保存:{}条记录,版本号为:{}",groupId,list.size(),version);
            list.clear();
        }
        int matchCount = length - repeatFilterCount;
        log.info("memberCrowdGroupRun-page deal end:groupId:{},本次圈选出{}条记录,总共保存了:{}条记录,因重复圈选总过滤:{}条记录,版本号为:{}",groupId,length,matchCount,repeatFilterCount,version);

        //返回本次同步数据的条数
        return matchCount;
    }

    /**
     * 根据groupId删除数据
     * wangzhe
     * 2021-01-29
     * @param groupId 圈选分组id
     * @param version 版本号
     * @return
     */
    public boolean deleteMongoGroupRefMemberByGroupId(String groupId,Long version){
        if (StringUtils.isEmpty(groupId)) {
            return false;
        }
        //删除分组下小于当前版本的数据
        Query query = Query.query(Criteria.where("groupId").is(groupId))
                .addCriteria(Criteria.where("version").lt(version));
        DeleteResult remove = memberLabelDao.remove(query, GroupRefMember.class);
        log.info("memberCrowdGroupRun-delete:groupId:{},本次操作的版本号为{},本次删除的记录数为:{}",groupId,version,remove.getDeletedCount());
        return true;
    }

    /**
     * 判读是否存在，不存在则设置
     * wangzhe
     * 2021-01-29
     * @param memberCard 会员卡号
     * @return 当前是否存在缓存
     */
    private Boolean getAndSetNxInGroupCache(String memberCard){
        String cacheKey = CacheKeyUtil.groupRunCacheKey("");
        //如果包含则直接使用
        if (CacheUtil.contain(memberCard)) {
            return true;
        }
        //判断redis中是否存在
        if (redisUtil.sHasKey(cacheKey,memberCard)) {
            //设置本地缓存，和redis中保持一致
            CacheUtil.setKey(memberCard,"1");
            //redis中存在
            return true;
        }
        //设置redis缓存
        redisUtil.sSetAndTime(cacheKey,REDIS_GROUP_RUN_CACHE_TIME, memberCard);
        //设置本地缓存
        CacheUtil.setKey(memberCard,"1");
        //返回本地没有直接找到
        return false;
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


    /**
     * 通过id查询给uize并进行u安萱试算
     * wangzhe
     * 2021-01-21
     * @param crowdGroupOpVO
     * @return
     */
    @Override
    public int memberCrowdGroupTrialById(MemberCrowdGroupOpVO crowdGroupOpVO){
        //查询规则
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        if (memberCrowdGroup == null) {
            return -1;
        }
        return memberCrowdGroupTrial(memberCrowdGroup);
    }

    /**
     * 通过id查询规则并运行规则
     * wangzhe
     * 2021-01-30
     * @param crowdGroupOpVO
     * @return
     */
    @Override
    public int memberCrowdGroupRunById(MemberCrowdGroupOpVO crowdGroupOpVO) {
        //查询规则
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        if (memberCrowdGroup == null) {
            return -1;
        }
        return memberCrowdGroupRun(memberCrowdGroup);
    }

}
