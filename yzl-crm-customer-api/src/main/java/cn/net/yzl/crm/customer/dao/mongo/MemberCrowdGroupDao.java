package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.customer.mongomodel.Member;
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
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        if (member_crowd_group == null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member_crowd_group);
    }

    public member_crowd_group getMemberCrowdGroup(String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Query query = new Query();
        query.addCriteria(Criteria.where("crowd_id").is(crowdId).and("del").is(false));
        return mongoTemplate.findOne(query, member_crowd_group.class);
    }

    /**
     * 修改顾客群圈选
     *
     * @param member_crowd_group
     * @throws Exception
     */
    public void updateMemberCrowdGroup(member_crowd_group member_crowd_group) throws Exception {
        if (member_crowd_group == null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
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
        query.addCriteria(Criteria.where("crowd_id").is(crowdId));
        update.set("del", true);
        mongoTemplate.updateFirst(query, update, member_crowd_group.class);
    }

    /**
     * 批量获取圈选
     *
     * @param crowdIds
     * @return
     */
    public List<CrowdGroup> getMemberCrowdGroupByIds(List<String> crowdIds) {
        // Query query = MongoQueryUtil.getBatchQuery(crowdIds,CrowdGroup.class);
        List<CrowdGroup> crowdGroupList = new ArrayList<>();

        for (String crowdId : crowdIds
        ) {
            Query query = new Query();
            query.addCriteria(Criteria.where("crowd_id").is(crowdId).and("del").is(false));
            CrowdGroup group = mongoTemplate.findOne(query, CrowdGroup.class, COLLECTION_NAME);
            if (group != null) crowdGroupList.add(group);
        }

        //  mongoTemplate.find(query,CrowdGroup.class,"member_crowd_group");
        return crowdGroupList;
    }

    public Page<CrowdGroup> findCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {

        Criteria criatira = new Criteria();

        criatira.where("del").is(false);
        if (!StringUtil.isNullOrEmpty(crowdGroupDTO.getName())) {
            criatira.and("crowd_name").is(crowdGroupDTO.getName());
        }
        if (crowdGroupDTO.getEnable() != -1) {
            criatira.and("enable").is(crowdGroupDTO.getEnable());
        }
//        Date startTime = crowdGroupDTO.getStart_date();
//        Date endTime =crowdGroupDTO.getEnd_date();
//        if (startTime !=null && endTime==null) {
//            criatira.and("create_time").gte(startTime);
//        } else if (startTime == null && endTime != null) {
//            criatira.and("create_time").lte(endTime);
//        } else if (startTime != null && endTime != null) {
//            criatira.andOperator(
//                    Criteria.where("create_time").gte(startTime),
//                    Criteria.where("create_time").lte(endTime));
//        }

        Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
        Pageable pageable = PageRequest.of(crowdGroupDTO.getCurrentPage(), crowdGroupDTO.getPageSize(), sort);

        Query query = new Query();
        query.addCriteria(criatira);

        int total = (int) mongoTemplate.count(query, CrowdGroup.class, COLLECTION_NAME); //先求总数

        int skip = (crowdGroupDTO.getCurrentPage() - 1) * crowdGroupDTO.getPageSize();
        query.skip(skip).limit(crowdGroupDTO.getPageSize());
        List<CrowdGroup> crowdGroupList = mongoTemplate.find(query, CrowdGroup.class);

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
    public Member getMemberFromMongo(String member_card) {
        Query query = new Query();
        query.addCriteria(Criteria.where("member_card").is(member_card));
        return mongoTemplate.findOne(query, Member.class);
    }

    /**
     * 保存顾客信息到mongo
     *
     * @param member
     */
    public void saveMemberToMongo(Member member) {
        if (member == null || StringUtil.isNullOrEmpty(member.getMember_card()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member);
    }

    /**
     * 修改mongo顾客信息
     * @param member
     */
    public void updateMemberToMongo(Member member) throws Exception {
        if (member == null || StringUtil.isNullOrEmpty(member.getMember_card()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);

        QueryUpdate queryUpdate = MongoQueryUtil.getMongoQueryForUpdate(member);
        mongoTemplate.updateFirst(queryUpdate.getQuery(), queryUpdate.getUpdate(), this.getEntityClass());

    }

}
