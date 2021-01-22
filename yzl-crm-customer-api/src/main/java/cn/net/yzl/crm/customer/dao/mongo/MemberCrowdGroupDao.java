package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.customer.mongomodel.crowd.CustomerCrowdGroupVO;
import cn.net.yzl.crm.customer.mongomodel.crowd.UpdateCrowdStatusVO;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.crm.customer.utils.MongoQueryUtil;
import cn.net.yzl.crm.customer.utils.QueryUpdate;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class MemberCrowdGroupDao extends MongoBaseDao<member_crowd_group> {
    private String COLLECTION_NAME = "member_crowd_group";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    protected Class<member_crowd_group> getEntityClass() {
        return member_crowd_group.class;
    }


    public void saveMemberCrowdGroup(member_crowd_group member_crowd_group) {
        if (member_crowd_group == null )
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member_crowd_group);
    }

    public member_crowd_group getMemberCrowdGroup(String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(crowdId).and("del").is(false));
        return mongoTemplate.findOne(query, member_crowd_group.class);
    }

    /**
     * 修改顾客群圈选
     *
     * @param member_crowd_group
     * @throws Exception
     */
    public void updateMemberCrowdGroup(member_crowd_group member_crowd_group) throws Exception {
        if (member_crowd_group == null )
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        QueryUpdate queryUpdate = MongoQueryUtil.getMongoQueryForUpdate(member_crowd_group);
        mongoTemplate.updateFirst(queryUpdate.getQuery(), queryUpdate.getUpdate(), this.getEntityClass());
    }

    /**
     * 删除 顾客圈选，只是更改删除状态
     *
     * @param crowdId
     */
    public void delMemberCrowdGroup(String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(crowdId));
        update.set("del", true);
        mongoTemplate.updateFirst(query, update, member_crowd_group.class);
    }

    /**
     * 批量获取圈选
     *
     * @param crowdIds
     * @return
     */
    public List<member_crowd_group> getMemberCrowdGroupByIds(List<String> crowdIds) {
        // Query query = MongoQueryUtil.getBatchQuery(crowdIds,CrowdGroup.class);
        List<member_crowd_group> crowdGroupList = new ArrayList<>();

        for (String crowdId : crowdIds
        ) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(crowdId).and("del").is(false));
            member_crowd_group group = mongoTemplate.findOne(query, member_crowd_group.class, COLLECTION_NAME);
            if (group != null) crowdGroupList.add(group);
        }

        //  mongoTemplate.find(query,CrowdGroup.class,"member_crowd_group");
        return crowdGroupList;
    }

    public Page<member_crowd_group> findCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {

        Criteria criatira = new Criteria();

        criatira.where("del").is(false);
        if (!StringUtil.isNullOrEmpty(crowdGroupDTO.getName())) {
            criatira.and("crowd_name").is(crowdGroupDTO.getName());
        }
        if (crowdGroupDTO.getEnable() != null) {
            criatira.and("enable").is(crowdGroupDTO.getEnable());
        }
        Date startTime = crowdGroupDTO.getStart_date();
        Date endTime =crowdGroupDTO.getEnd_date();
        if (startTime !=null && endTime==null) {
            criatira.and("create_time").gte(startTime);
        } else if (startTime == null && endTime != null) {
            criatira.and("create_time").lte(endTime);
        } else if (startTime != null && endTime != null) {
            criatira.andOperator(
                    Criteria.where("create_time").gte(startTime),
                    Criteria.where("create_time").lte(endTime));
        }



        Query query = new Query();
        query.addCriteria(criatira);

        int total = (int) mongoTemplate.count(query, member_crowd_group.class, COLLECTION_NAME); //先求总数

        int skip = (crowdGroupDTO.getCurrentPage() - 1) * crowdGroupDTO.getPageSize();
        query.skip(skip).limit(crowdGroupDTO.getPageSize());
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
        query.with(sort);
        query.fields().include("_id")
                .include("crowd_name").include("description")
                .include("enable").include("effective_date")
                .include("expire_date").include("create_name")
                .include("create_time");
        List<member_crowd_group> crowdGroupList = mongoTemplate.find(query, member_crowd_group.class);

        //mongoTemplate.count计算总数

        Page page = new Page();

        PageParam pageParam = new PageParam();
        pageParam.setTotalCount(total);
        pageParam.setPageTotal(total % crowdGroupDTO.getPageSize() == 0 ? total / crowdGroupDTO.getPageSize() : total / crowdGroupDTO.getPageSize() + 1);
        pageParam.setPageNo(crowdGroupDTO.getCurrentPage());

        page.setPageParam(pageParam);
        page.setItems(crowdGroupList);

        return page;
    }


    /**
     * 根据member_card 从mongo获取顾客信息
     *
     * @param member_card
     * @return
     */
    public member_wide getMemberFromMongo(String member_card) {
        Query query = new Query();
        query.addCriteria(Criteria.where("member_card").is(member_card));
        return mongoTemplate.findOne(query, member_wide.class);
    }
    /**
     * @Author: lichanghong
     * @Description:  修改状态
     * @Date: 2021/1/22 6:41 下午
     * @param vo
     * @Return: boolean
     */
    public ComResponse updateCustomerCrowdGroupStatus(UpdateCrowdStatusVO vo){
        member_crowd_group group= getMemberCrowdGroup(vo.get_id());
        if(Objects.isNull(group)){
            return ComResponse.nodata("群组不存在");
        }
        if(vo.getEnable()!=null && vo.getEnable()==1){
            if(group.getExpire_date().getTime()<System.currentTimeMillis()){
                return ComResponse.fail(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(),"当前群组已经过期!");
            }
        }

        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(vo.get_id()));
        update.set("enable", vo.getEnable())
                .set("update_code",vo.getUpdate_code())
                .set("update_name",vo.getUpdate_name())
                .set("update_time", MongoDateHelper.getMongoDate(new Date()));

        mongoTemplate.updateFirst(query, update, member_crowd_group.class);
        return ComResponse.success();
    }
    /**
     * 保存顾客信息到mongo
     *
     * @param member
     */
    public void saveMemberToMongo(member_wide member) {
        if (member == null || StringUtil.isNullOrEmpty(member.getMember_card()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member);
    }

    /**
     * 修改mongo顾客信息
     * @param member
     */
    public void updateMemberToMongo(member_wide member) throws Exception {
        if (member == null || StringUtil.isNullOrEmpty(member.getMember_card()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);

        QueryUpdate queryUpdate = MongoQueryUtil.getMongoQueryForUpdate(member);
        mongoTemplate.updateFirst(queryUpdate.getQuery(), queryUpdate.getUpdate(), this.getEntityClass());

    }
    public List<CustomerCrowdGroupVO> query4Select(){
        Query query = new Query();
        Criteria  criatira =Criteria.where("del").is(false);
        query.addCriteria(criatira);
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
        query.with(sort);
        query.fields().include("_id")
                .include("crowd_name");
        List<CustomerCrowdGroupVO> crowdGroupList = mongoTemplate.find(query, CustomerCrowdGroupVO.class,COLLECTION_NAME);
        return crowdGroupList;
    }

}
