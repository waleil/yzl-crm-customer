package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.mongomodel.Member_Crowd_Group;
import cn.net.yzl.crm.customer.sys.BizException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MemberCrowdGroupDao extends MongoBaseDao<Member_Crowd_Group> {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    protected Class<Member_Crowd_Group> getEntityClass() {
        return Member_Crowd_Group.class;
    }


    public void saveMemberCrowdGroup(Member_Crowd_Group member_crowd_group){
        if(member_crowd_group==null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        mongoTemplate.save(member_crowd_group);
    }

    public void updateMemberCrowdGroup(Member_Crowd_Group member_crowd_group){
        if(member_crowd_group==null || StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_id()))
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);

        Query query = new Query();
        query.addCriteria(Criteria.where("crowd_id").is(member_crowd_group.getCrowd_id()));
        Update update = new Update();

        if(StringUtil.isNullOrEmpty(member_crowd_group.getCrowd_name())){
            update.set("crowd_name", member_crowd_group.getCrowd_name());
        }


        mongoTemplate.updateFirst(query,update,this.getEntityClass());
    }
}
