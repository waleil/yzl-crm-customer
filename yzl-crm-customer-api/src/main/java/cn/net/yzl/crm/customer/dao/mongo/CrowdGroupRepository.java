package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrowdGroupRepository extends MongoRepository<member_crowd_group,String> {
}
