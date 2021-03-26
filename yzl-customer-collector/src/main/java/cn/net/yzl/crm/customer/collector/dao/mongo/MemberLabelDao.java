package cn.net.yzl.crm.customer.collector.dao.mongo;

import cn.net.yzl.crm.customer.collector.model.mogo.MemberLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelDao
 * @description 会员宽表数据
 * @date: 2021/1/25 5:36 下午
 */
@Component
public class MemberLabelDao  {

    @Autowired
    private MongoTemplate mongoTemplate;
    public void save(MemberLabel t,String collect) {
        mongoTemplate.save(t,collect);
    }
    public void batchInsert(List<MemberLabel> list, String collect) {
        BulkOperations bulkOperations= mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,collect);
        bulkOperations.insert(list).execute();
    }
}
