package cn.net.yzl.crm.customer.collector.dao.mongo;

import cn.net.yzl.crm.customer.collector.model.MongoTransactionDemo;
import org.springframework.stereotype.Component;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MongoTransactionDemoDao
 * @description todo
 * @date: 2021/1/30 3:29 下午
 */
@Component
public class MongoTransactionDemoDao extends MongoBaseDao<MongoTransactionDemo> {
    @Override
    protected Class<MongoTransactionDemo> getEntityClass() {
        return MongoTransactionDemo.class;
    }
}
