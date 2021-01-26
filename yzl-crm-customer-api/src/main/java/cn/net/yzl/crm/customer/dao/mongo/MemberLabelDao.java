package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.mongomodel.Member_Age;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelDao
 * @description 会员宽表数据
 * @date: 2021/1/25 5:36 下午
 */
@Component
public class MemberLabelDao extends MongoBaseDao<MemberLabel> {
    private static String COLLECTION_NAME = "member_label";
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    protected Class<MemberLabel> getEntityClass() {
        return MemberLabel.class;
    }
    /**
     * @Author: lichanghong
     * @Description: 规则试算
     * @Date: 2021/1/26 11:42 上午
     * @param memberCrowdGroup
     * @Return: java.lang.Integer
     */
    public Integer memberCrowdGroupTrial(member_crowd_group memberCrowdGroup){
        Query query = new Query();
        Criteria criteria = new Criteria();
        //判断性别
        if(memberCrowdGroup.getSex()!=null){
            if(memberCrowdGroup.getSex()==1){
                criteria.and("sex").is(1);
            }else{
                criteria.and("sex").is(0);
            }
        }
        //邮箱
        if(memberCrowdGroup.getEmail()!=null){
            if(memberCrowdGroup.getEmail()==1){
                criteria.and("hasEmail").is(true);
            }else{
                criteria.and("hasEmail").is(false);
            }
        }
        //QQ
        if(memberCrowdGroup.getQq()!=null){
            if(memberCrowdGroup.getQq()==1){
                criteria.and("hasQQ").is(true);
            }else{
                criteria.and("hasQQ").is(false);
            }
        }
        //微信
        if(memberCrowdGroup.getWechat()!=null){
            if(memberCrowdGroup.getWechat()==1){
                criteria.and("hasWechat").is(true);
            }else{
                criteria.and("hasWechat").is(false);
            }
        }
        //微信
        if(memberCrowdGroup.getRecharge()!=null){
            if(memberCrowdGroup.getRecharge()==1){
                criteria.and("hasMoney").is(true);
            }else{
                criteria.and("hasMoney").is(false);
            }
        }
        if(!CollectionUtils.isEmpty(memberCrowdGroup.getAge())){
            List<Member_Age> list = memberCrowdGroup.getAge();
            for(Member_Age age:list){
                //包含
                if(age.getInclude()==1){
//                    criteria.and("age").gte(age.getStart_age()).and("age").lt(age.getEnd_age());
                    criteria.andOperator(Criteria.where("age"));
                }else if(age.getInclude()==0){
                    criteria.and("age").lte(age.getStart_age()).and("age").gte(age.getEnd_age());
                }
            }
        }

        query.addCriteria(criteria);
        return (int)mongoTemplate.count(query,MemberLabel.class,COLLECTION_NAME);
    }
}
