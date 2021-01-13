package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.MongoQueryUtil;
import cn.net.yzl.crm.customer.utils.QueryUpdate;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MemberCrowdGroupDao extends MongoBaseDao<member_crowd_group> {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    protected Class<member_crowd_group> getEntityClass() {
        return member_crowd_group.class;
    }


    public void saveMemberCrowdGroup(member_crowd_group member_crowd_group){
        if(member_crowd_group==null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member_crowd_group);
    }

    public member_crowd_group getMemberCrowdGroup(String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Query query = new Query();
        query.addCriteria(Criteria.where("crowd_id").is(crowdId));
        return mongoTemplate.findOne(query, member_crowd_group.class);
    }

    public void updateMemberCrowdGroup(member_crowd_group member_crowd_group) throws Exception {
        if(member_crowd_group==null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        QueryUpdate queryUpdate= MongoQueryUtil.getMongoQueryForUpdate(member_crowd_group);

//        query.addCriteria(Criteria.where("crowd_id").is(member_crowd_group.getCrowd_id()));
//        if(StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_name())){
//            update.set("crowd_name", member_crowd_group.getCrowd_name());
//        }


        mongoTemplate.updateFirst(queryUpdate.getQuery(),queryUpdate.getUpdate(),this.getEntityClass());
    }


}
