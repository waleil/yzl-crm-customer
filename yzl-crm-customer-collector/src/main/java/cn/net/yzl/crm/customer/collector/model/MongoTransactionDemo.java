package cn.net.yzl.crm.customer.collector.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MongoTransactionDemo
 * @description
 * @date: 2021/1/30 3:28 下午
 */
@Document("mongoTransactionDemo")
@Data
public class MongoTransactionDemo {
    private String id;
    private String name;
}
