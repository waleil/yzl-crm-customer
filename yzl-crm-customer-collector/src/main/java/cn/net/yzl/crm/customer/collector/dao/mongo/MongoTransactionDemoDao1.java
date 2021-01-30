package cn.net.yzl.crm.customer.collector.dao.mongo;

import cn.net.yzl.crm.customer.collector.model.MongoTransactionDemo1;
import org.springframework.stereotype.Component;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MongoTransactionDemoDao
 * @description todo
 * @date: 2021/1/30 3:29 下午
 */
@Component
public class MongoTransactionDemoDao1 extends MongoBaseDao<MongoTransactionDemo1> {
    @Override
    protected Class<MongoTransactionDemo1> getEntityClass() {
        return MongoTransactionDemo1.class;
    }
}
