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
 * @description ?????????????????????
 * @date: 2021/1/22 1:58 ??????
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

    //5??????
    private final static Integer REDIS_GROUP_RUN_CACHE_TIME = 5 * 60 * 60;

    private final static String COLLECTION_NAME_MEMBER_LABEL = "member_label";
    private final static String COLLECTION_NAME_GROUP_REF_MEMBER= "group_ref_member";
    //???????????????????????????
    private final static String DATE_FORMAT_YYYYMMDD= "yyyyMMdd";

    //??????????????????id???????????????
    private static Map<String, HashSet<String>> cacheMap = new ConcurrentHashMap<>();

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4,128,1000L,
            TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<Runnable>(1024));

    /**
     * @Author: lichanghong
     * @Description: ????????????????????????
     * @Date: 2021/1/22 2:01 ??????
     * @param groupIds
     * @Return: java.util.List<cn.net.yzl.crm.customer.mongomodel.member_crowd_group>
     */
    @Override
    public List<member_crowd_group> getCrowdGroupByIds(List<String> groupIds) {

        return memberCrowdGroupDao.getMemberCrowdGroupByIds(groupIds);
    }
    /**
     * @Author: lichanghong
     * @Description: ????????????????????????
     * @Date: 2021/1/22 2:00 ??????
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
     * @Description: ????????????
     * @Date: 2021/1/22 2:01 ??????
     * @param member_crowd_group
     * @Return: void
     */
    @Override
    public ComResponse saveCustomerCrowdGroup(member_crowd_group member_crowd_group) {
        Date date = new Date();
        member_crowd_group.setCreate_time(date/*MongoDateHelper.getMongoDate(date)*/);
        member_crowd_group.setCreateTimeLong(date.getTime());
        member_crowd_group.setUpdate_time(member_crowd_group.getCreate_time());
        memberCrowdGroupDao.saveMemberCrowdGroup(member_crowd_group);
        return ComResponse.success();
    }

    /**
     * @Author: lichanghong
     * @Description: ??????????????????
     * @Date: 2021/1/22 2:03 ??????
     * @param crowdId
     * @Return: cn.net.yzl.crm.customer.mongomodel.member_crowd_group
     */
    @Override
    public member_crowd_group getMemberCrowdGroup(String crowdId) {

        return memberCrowdGroupDao.getMemberCrowdGroup(crowdId);
    }
    /**
     * @Author: lichanghong
     * @Description: ??????????????????
     * @Date: 2021/1/22 2:03 ??????
     * @param member_crowd_group
     * @Return: void
     */
    @Override
    public ComResponse updateCustomerCrowdGroup(member_crowd_group member_crowd_group) throws Exception {
        member_crowd_group old = getMemberCrowdGroup(member_crowd_group.get_id());
        if (old == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"???????????????!");
        }
        if(old.getEnable()!=null && old.getEnable()==1){
            return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(),"????????????????????????????????????!");
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
     * @Description: ??????????????????
     * @Date: 2021/1/22 8:21 ??????
     * @param vo
     * @Return: cn.net.yzl.common.entity.ComResponse
     */
    @Override
    public ComResponse updateCustomerCrowdGroupStatus(UpdateCrowdStatusVO vo) {

        return memberCrowdGroupDao.updateCustomerCrowdGroupStatus(vo);
    }
    /**
     * @Author: lichanghong
     * @Description:  ??????????????????
     * @Date: 2021/1/22 8:20 ??????
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
        //????????????????????????
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        //????????????
        return (int) memberLabelDao.memberCrowdGroupTrial(query);
    }

    /**
     * ????????????????????????????????????????????????
     * wangzhe
     * 2020-02-07
     * @param memberCrowdGroup ????????????
     * @param memberCard ????????????
     * @return ???????????????????????????????????????
     */
    @Override
    public boolean isCrowdGroupIncludeMemberCard(member_crowd_group memberCrowdGroup,String memberCard) {
        //????????????????????????
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        query.addCriteria(Criteria.where("memberCard").is(memberCard));
        //????????????
        long result = memberLabelDao.memberCrowdGroupTrial(query);
        return result > 0 ;
    }


    @Override
    @SysAccessLog(logKeyParamName = "memberCrowdGroup",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public Page<MemberLabelDto> groupTrialPullData(member_crowd_group memberCrowdGroup) {
        //????????????????????????
        Query query = memberLabelDao.initQuery(memberCrowdGroup);
        //????????????
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
        //???????????????
        String groupId = "";
        String groupName = "";

        //?????????yyyyMMdd
        Long version = Long.parseLong(DateUtil.format(DateUtil.tomorrow(),DATE_FORMAT_YYYYMMDD));
        try {
            for (member_crowd_group memberCrowdGroup : list) {
                groupId = memberCrowdGroup.get_id();
                groupName = memberCrowdGroup.getCrowd_name();
                //threadPoolExecutor.execute(()->{ memberCrowdGroupRun(memberCrowdGroup);});
                memberCrowdGroupRun(memberCrowdGroup,version);
            }
        } catch (Exception e) {
            log.error("/v1/memberGroupTimedTask:????????????????????????!,?????????????????????id???:{},?????????:{}",groupId,groupName);
        }
        return true;
    }

    @Override
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup) throws InterruptedException {
        return memberCrowdGroupRun(memberCrowdGroup, null);
    }
    /**
     * ???????????????????????????????????????
     * @param memberCrowdGroup
     * @return
     */
    @Override
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public int memberCrowdGroupRun(member_crowd_group memberCrowdGroup,Long version) throws InterruptedException {
        //????????????????????????
        Long ver = version == null ? Long.parseLong(DateUtil.format(new Date(), DATE_FORMAT_YYYYMMDD)) : version;
        String groupId = memberCrowdGroup.get_id();
        AtomicInteger matchCount = new AtomicInteger(0);
        long groupRunStartTime = System.currentTimeMillis();

        if (memberCrowdGroup.getEnable() != null && memberCrowdGroup.getEnable() == 1){
            Integer pageNo = 1;
            Integer pageSize = SAVE_LINE;
            Query query = memberLabelDao.initQuery(memberCrowdGroup);

            //????????????????????????
            boolean hasNext = true;
            query.with(Sort.by(Sort.Order.asc("_id")));
            query.fields().include("memberCard").include("memberName").exclude("_id");

            List<Future<String>> futures = new ArrayList<>();
            while (hasNext) {
                Page<MemberLabel> memberLabelPage = memberLabelDao.memberCrowdGroupRunUsePage(pageNo, pageSize, query);
                PageParam pageParam = memberLabelPage.getPageParam();
                //????????????
                List<MemberLabel> labels = memberLabelPage.getItems();
                if(!CollectionUtils.isEmpty(labels)) {
                    Future<String> future = executeAsync.executeAsync2(() -> {
                        log.info("memberCrowdGroupRun-page query:groupId:{},????????????,pageNo:{},?????????{}?????????,????????????:{}", groupId, pageParam.getPageNo(), labels.size(), ver);
                        //????????????
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
                //????????????????????????
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
            log.info("??????:{}???????????????",groupId);
        }

        //??????mongo???????????????groupId?????????????????????(??????????????????????????????)
        deleteMongoGroupRefMemberByGroupId(groupId,ver);

        //??????group_ref_member?????????????????????????????????
        Query getMemberCount = new Query();
        getMemberCount.addCriteria(Criteria.where("groupId").is(groupId));
        long count = memberLabelDao.count(getMemberCount, GroupRefMember.class);

        //??????????????????????????????????????????
        Query updateCondition = new Query();
        updateCondition.addCriteria(Criteria.where("_id").is(groupId));
        Update update = new Update();
        update.set("person_count",count);
        memberLabelDao.updateFirst(updateCondition, update,member_crowd_group.class);
        long groupRunEndTime = System.currentTimeMillis();
        log.info("memberCrowdGroupRun-end:groupId:{},???????????????{}?????????,????????????:{} member_crowd_group ?????????,????????????:{},?????????????????????:{}",groupId,matchCount.get(),ver,count,(groupRunEndTime-groupRunStartTime));
        return matchCount.get();
    }


    /**
     * ???????????????????????????
     * wangzhe
     * 2021-02-07
     * @param groupId
     * @param labels
     * @return
     */
    public Boolean memberCrowdGroupRunByLabels(String groupId,List<MemberLabel> labels) {
        long groupRunStartTime = System.currentTimeMillis();
        //????????????????????????
        Long version = Long.parseLong(DateUtil.format(new Date(),DATE_FORMAT_YYYYMMDD));
        //int matchCount = doMemberCrowdGroupRun(groupId, labels,version);
        List<GroupRefMember> list = new ArrayList<>(labels.size());

        for (MemberLabel label : labels) {
            GroupRefMember member = new GroupRefMember();//??????
            member.setGroupId(groupId);
            member.setMemberCard(label.getMemberCard());
            member.setMemberName(label.getMemberName());
            member.setVersion(version);
            list.add(member);
        }
        //????????????
        memberLabelDao.insertAll(list,COLLECTION_NAME_GROUP_REF_MEMBER);
        log.info("memberCrowdGroupRun-save:groupId:{},????????????:{}?????????,????????????:{}",groupId,list.size(),version);
        list.clear();
        //labels.clear();
        //??????group_ref_member?????????????????????????????????
        Query getMemberCount = new Query();
        getMemberCount.addCriteria(Criteria.where("groupId").is(groupId));
        long count = memberLabelDao.count(getMemberCount, GroupRefMember.class);

        //??????????????????????????????????????????
        Query updateCondition = new Query();
        updateCondition.addCriteria(Criteria.where("_id").is(groupId));
        Update update = new Update();
        update.set("person_count",count);
        memberLabelDao.updateFirst(updateCondition, update,member_crowd_group.class);
        long groupRunEndTime = System.currentTimeMillis();
        log.info("memberCrowdGroupRun-end:groupId:{},??????????????????:{} member_crowd_group ?????????,????????????:{},?????????????????????:{}",groupId,version,count,(groupRunEndTime-groupRunStartTime));
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
        //???????????????
        int repeatFilterCount = 0;
        //????????????groupId??????????????????????????????mongo
        for (int i = 0; i < length; i++) {
            label = labels.get(i);
            memberCard = label.getMemberCard();
            //??????????????????
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
        //????????????
        if (CollectionUtil.isNotEmpty(list)) {
            memberLabelDao.insertAll(list,COLLECTION_NAME_GROUP_REF_MEMBER);
            log.info("memberCrowdGroupRun-save:groupId:{},????????????:{}?????????,????????????:{}",groupId,list.size(),version);
            list.clear();
        }
        int matchCount = length - repeatFilterCount;
        log.info("memberCrowdGroupRun-page deal end:groupId:{},???????????????{}?????????,???????????????:{}?????????,????????????????????????:{}?????????,????????????:{}",groupId,length,matchCount,repeatFilterCount,version);

        //?????????????????????????????????
        return matchCount;
    }

    /**
     * ??????groupId????????????
     * wangzhe
     * 2021-01-29
     * @param groupId ????????????id
     * @param version ?????????
     * @return
     */
    public boolean deleteMongoGroupRefMemberByGroupId(String groupId,Long version){
        if (StringUtils.isEmpty(groupId)) {
            return false;
        }
        //??????????????????????????????????????????
        Query query = Query.query(Criteria.where("groupId").is(groupId))
                .addCriteria(Criteria.where("version").lt(version));
        DeleteResult remove = memberLabelDao.remove(query, GroupRefMember.class);
        log.info("memberCrowdGroupRun-delete:groupId:{},???????????????????????????{},???????????????????????????:{}",groupId,version,remove.getDeletedCount());
        return true;
    }

    /**
     * ???????????????????????????????????????
     * wangzhe
     * 2021-01-29
     * @param memberCard ????????????
     * @return ????????????????????????
     */
    private Boolean getAndSetNxInGroupCache(String memberCard){
        String cacheKey = CacheKeyUtil.groupRunCacheKey("");
        //???????????????????????????
        if (CacheUtil.contain(memberCard)) {
            return true;
        }
        //??????redis???????????????
        if (redisUtil.sHasKey(cacheKey,memberCard)) {
            //????????????????????????redis???????????????
            CacheUtil.setKey(memberCard,"1");
            //redis?????????
            return true;
        }
        //??????redis??????
        redisUtil.sSetAndTime(cacheKey,REDIS_GROUP_RUN_CACHE_TIME, memberCard);
        //??????????????????
        CacheUtil.setKey(memberCard,"1");
        //??????????????????????????????
        return false;
    }

    /**
     * ??????????????????????????????
     * wangzhe
     * 2021-01-29
     * @param index ???????????????
     * @return
     */
    private Boolean isSaveLine(Integer index){
        return index > 0 && index % SAVE_LINE == 0;
    }


    /**
     * ??????id????????????????????????????????????
     * wangzhe
     * 2021-01-21
     * @param crowdGroupOpVO
     * @return
     */
    @Override
    public int memberCrowdGroupTrialById(MemberCrowdGroupOpVO crowdGroupOpVO){
        //????????????
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        if (memberCrowdGroup == null) {
            return -1;
        }
        return memberCrowdGroupTrial(memberCrowdGroup);
    }

    @Override
    public Page<MemberLabelDto>  groupTrialByIdPullData(MemberCrowdGroupOpVO crowdGroupOpVO){
        //????????????
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        Page<MemberLabelDto> page = null;
        if (memberCrowdGroup == null) {
            return page;
        }
        return this.groupTrialPullData(memberCrowdGroup);
    }

    /**
     * ??????id???????????????????????????
     * wangzhe
     * 2021-01-30
     * @param crowdGroupOpVO
     * @return
     */
    @Override
    public int memberCrowdGroupRunById(MemberCrowdGroupOpVO crowdGroupOpVO) throws InterruptedException {
        //????????????
        member_crowd_group memberCrowdGroup = memberCrowdGroupDao.getMemberCrowdGroup(crowdGroupOpVO.get_id());
        if (memberCrowdGroup == null) {
            return -1;
        }
        return memberCrowdGroupRun(memberCrowdGroup);
    }

    /**
     * ?????????????????????????????????????????????
     * wangzhe
     * 2021-02-04
     * @param query ????????????
     * @param include ??????
     * @param exclude ?????????
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
