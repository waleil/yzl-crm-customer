package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.mongo.MemberCrowdGroupDao;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.dto.label.MemberLabelDto;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.crowd.MemberCrowdGroupOpVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.CustomerGroupService;
import cn.net.yzl.crm.customer.service.thread.IAsyncService;
import cn.net.yzl.crm.customer.utils.CacheKeyUtil;
import cn.net.yzl.crm.customer.utils.CacheUtil;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.RedisUtil;
import cn.net.yzl.logger.annotate.SysAccessLog;
import cn.net.yzl.logger.enums.DefaultDataEnums;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    IAsyncService executeAsync;

    private final static Integer SAVE_LINE = 15_000;

    //5小时
    private final static Integer REDIS_GROUP_RUN_CACHE_TIME = 5 * 60 * 60;

    private final static String COLLECTION_NAME_MEMBER_LABEL = "member_label";
    private final static String COLLECTION_NAME_GROUP_REF_MEMBER= "group_ref_member";
    //日期格式化成年月日
    private final static String DATE_FORMAT_YYYYMMDD= "yyyyMMdd";

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
        Date date = new Date();
        member_crowd_group.setCreate_time(MongoDateHelper.getMongoDate(date));
        member_crowd_group.setCreateTimeLong(date.getTime());
        member_crowd_group.setUpdate_time(member_crowd_group.getCreate_time());
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
        member_crowd_group.setCreateTimeLong(old.getCreateTimeLong());
        member_crowd_group.setCreate_code(old.getCreate_code());
        Date date = new Date();
        member_crowd_group.setUpdate_time(MongoDateHelper.getMongoDate(new Date()));
        member_crowd_group.setUpdateTimeLong(date.getTime());
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
        //进行顾客人群圈选
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        //圈选试算
        return (int) memberLabelDao.memberCrowdGroupTrial(query);
    }

    /**
     * 判断当前客户是否属于给定圈选规则
     * wangzhe
     * 2020-02-07
     * @param memberCrowdGroup 群组对象
     * @param memberCard 会员卡号
     * @return 该会员卡好是否数据当前群组
     */
    @Override
    public boolean isCrowdGroupIncludeMemberCard(member_crowd_group memberCrowdGroup,String memberCard) {
        //进行顾客人群圈选
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        query.addCriteria(Criteria.where("memberCard").is(memberCard));
        //圈选试算
        long result = memberLabelDao.memberCrowdGroupTrial(query);
        return result > 0 ;
    }


    @Override
    @SysAccessLog(logKeyParamName = "memberCrowdGroup",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public Page<MemberLabelDto> groupTrialPullData(member_crowd_group memberCrowdGroup) {
        //进行顾客人群圈选
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        //圈选试算
        Long count = memberLabelDao.memberCrowdGroupTrial(query);
        //query.with(Sort.by(Sort.Order.asc("_id")));
        appendQueryAttr(query,"memberCard,memberName,sex".split(","),"_id".split(","));
        Page<MemberLabelDto> page = memberLabelDao.memberCrowdGroupRunUsePage(1, 50, query,count);
        return page;
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
        List<member_crowd_group> list =memberCrowdGroupDao.query4Task(Boolean.TRUE);
        //多线程执行
        String groupId = "";
        String groupName = "";

        //明天的yyyyMMdd
        Long version = Long.parseLong(DateUtil.format(DateUtil.tomorrow(),DATE_FORMAT_YYYYMMDD));
        try {
            for (member_crowd_group memberCrowdGroup : list) {
                groupId = memberCrowdGroup.get_id();
                groupName = memberCrowdGroup.getCrowd_name();
                //threadPoolExecutor.execute(()->{ memberCrowdGroupRun(memberCrowdGroup);});
                memberCrowdGroupRun(memberCrowdGroup,version);
            }
        } catch (Exception e) {
            log.error("/v1/memberGroupTimedTask:定时任务圈选异常!,当前处理的群组id为:{},名称为:{}",groupId,groupName);
        }
        return true;
    }

    @Override
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup) throws InterruptedException {
        return memberCrowdGroupRun(memberCrowdGroup, null);
    }
    /**
     * 匹配全选规则，生成新的数据
     * @param memberCrowdGroup
     * @return
     */
    @Override
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup,Long version) throws InterruptedException {
        //生成数据的版本号
        Long ver = version == null ? Long.parseLong(DateUtil.format(new Date(), DATE_FORMAT_YYYYMMDD)) : version;
        String groupId = memberCrowdGroup.get_id();
        AtomicInteger matchCount = new AtomicInteger(0);
        long groupRunStartTime = System.currentTimeMillis();

        if (memberCrowdGroup.getEnable() != null && memberCrowdGroup.getEnable() == 1){
            Integer pageNo = 1;
            Integer pageSize = SAVE_LINE;
            Query query = memberLabelDao.initQuery(memberCrowdGroup);

            //获取记录的总条数
            boolean hasNext = true;
            query.with(Sort.by(Sort.Order.asc("_id")));
            query.fields().include("memberCard").include("memberName").exclude("_id");

            List<Future<String>> futures = new ArrayList<>();
            while (hasNext) {
                Page<MemberLabel> memberLabelPage = memberLabelDao.memberCrowdGroupRunUsePage(pageNo, pageSize, query);
                PageParam pageParam = memberLabelPage.getPageParam();
                //分页查询
                List<MemberLabel> labels = memberLabelPage.getItems();
                if(!CollectionUtils.isEmpty(labels)) {
                    Future<String> future = executeAsync.executeAsync2(() -> {
                        log.info("memberCrowdGroupRun-page query:groupId:{},本次分页,pageNo:{},圈选出{}条记录,版本号为:{}", groupId, pageParam.getPageNo(), labels.size(), ver);
                        //处理数据
                        int result = doMemberCrowdGroupRun(groupId, labels, ver);
                        matchCount.addAndGet(result);
                        labels.clear();
                    });
                    futures.add(future);
                    if (pageParam.getNextPage() == 0) {
                        hasNext = false;
                    }
                    pageNo = pageParam.getNextPage();
                }
                //判断是否有下一页
                else{
                    break;
                }
            }

            a:while (true) {
                Iterator<Future<String>> iterator = futures.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().isDone()) {
                        iterator.remove();
                    }
                }
                if (futures.size() == 0) {
                    break a;
                }
                Thread.sleep(3000);
            }
            log.info("群组:{}程执行完毕",groupId);
        }

        //删除mongo里面的当前groupId对应的历史数据(删除非当前版本的数据)
        deleteMongoGroupRefMemberByGroupId(groupId,ver);

        //查询group_ref_member中，当前群组的顾客数量
        Query getMemberCount = new Query();
        getMemberCount.addCriteria(Criteria.where("groupId").is(groupId));
        long count = memberLabelDao.count(getMemberCount, GroupRefMember.class);

        //设置本次匹配到圈选规则的条数
        Query updateCondition = new Query();
        updateCondition.addCriteria(Criteria.where("_id").is(groupId));
        Update update = new Update();
        update.set("person_count",count);
        memberLabelDao.updateFirst(updateCondition, update,member_crowd_group.class);
        long groupRunEndTime = System.currentTimeMillis();
        log.info("memberCrowdGroupRun-end:groupId:{},本次圈选出{}条记录,版本号为:{} member_crowd_group 已更新,更新后为:{},本次圈选总耗时:{}",groupId,matchCount.get(),ver,count,(groupRunEndTime-groupRunStartTime));
        return matchCount.get();
    }


    /**
     * 把一组客户插入群组
     * wangzhe
     * 2021-02-07
     * @param groupId
     * @param labels
     * @return
     */
    public Boolean memberCrowdGroupRunByLabels(String groupId,List<MemberLabel> labels) {
        long groupRunStartTime = System.currentTimeMillis();
        //生成数据的版本号
        Long version = Long.parseLong(DateUtil.format(new Date(),DATE_FORMAT_YYYYMMDD));
        //int matchCount = doMemberCrowdGroupRun(groupId, labels,version);
        List<GroupRefMember> list = new ArrayList<>(labels.size());

        for (MemberLabel label : labels) {
            GroupRefMember member = new GroupRefMember();//新建
            member.setGroupId(groupId);
            member.setMemberCard(label.getMemberCard());
            member.setMemberName(label.getMemberName());
            member.setVersion(version);
            list.add(member);
        }
        //保存集合
        memberLabelDao.insertAll(list,COLLECTION_NAME_GROUP_REF_MEMBER);
        log.info("memberCrowdGroupRun-save:groupId:{},本次保存:{}条记录,版本号为:{}",groupId,list.size(),version);
        list.clear();
        //labels.clear();
        //查询group_ref_member中，当前群组的顾客数量
        Query getMemberCount = new Query();
        getMemberCount.addCriteria(Criteria.where("groupId").is(groupId));
        long count = memberLabelDao.count(getMemberCount, GroupRefMember.class);

        //设置本次匹配到圈选规则的条数
        Query updateCondition = new Query();
        updateCondition.addCriteria(Criteria.where("_id").is(groupId));
        Update update = new Update();
        update.set("person_count",count);
        memberLabelDao.updateFirst(updateCondition, update,member_crowd_group.class);
        long groupRunEndTime = System.currentTimeMillis();
        log.info("memberCrowdGroupRun-end:groupId:{},本次版本号为:{} member_crowd_group 已更新,更新后为:{},本次圈选总耗时:{}",groupId,version,count,(groupRunEndTime-groupRunStartTime));
        return Boolean.TRUE;
    }

    @Override
    public Query convertMongoCondition(member_crowd_group memberCrowdGroup) {
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        return query;
    }

    @Override
    public Query convertIdToMongoCondition(MemberCrowdGroupOpVO crowdGroupOpVO) {
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        return query;
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
     * 通过id查询给规则并进行圈选试算
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

    @Override
    public Page<MemberLabelDto>  groupTrialByIdPullData(MemberCrowdGroupOpVO crowdGroupOpVO){
        //查询规则
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        Page<MemberLabelDto> page = null;
        if (memberCrowdGroup == null) {
            return page;
        }
        return this.groupTrialPullData(memberCrowdGroup);
    }

    /**
     * 通过id查询规则并运行规则
     * wangzhe
     * 2021-01-30
     * @param crowdGroupOpVO
     * @return
     */
    @Override
    public int memberCrowdGroupRunById(MemberCrowdGroupOpVO crowdGroupOpVO) throws InterruptedException {
        //查询规则
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        if (memberCrowdGroup == null) {
            return -1;
        }
        return memberCrowdGroupRun(memberCrowdGroup);
    }

    /**
     * 构造要查询的属性和要过滤的属性
     * wangzhe
     * 2021-02-04
     * @param query 查询对象
     * @param include 包含
     * @param exclude 不包含
     */
    private void appendQueryAttr(Query query,String[] include,String[] exclude){
        Field fields = query.fields();
        if (include == null) {
            for (String p : include) {
                fields.include(p);
            }
        }
        if (exclude == null) {
            for (String p : include) {
                fields.exclude("_id");
            }
        }
    }

    public static void main(String[] args) {


        List<Object> objects = Collections.emptyList();

        objects.clear();

        objects.add("ssss");
        System.out.println(objects);
    }

}
