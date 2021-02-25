package cn.net.yzl.crm.customer.config.mongodb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @Description mongo事务配置
 * @Author jingweitao
 * @Date 14:15 2020/12/10
 **/
@Configuration
public class MongoTransactionConfig {

    @Bean(name = "mongoTransactionManager")
    @ConditionalOnProperty(name="spring.data.mongodb.transactionEnabled",havingValue = "true")
    MongoTransactionManager transactionManager(MongoDatabaseFactory factory){
        return new MongoTransactionManager(factory);
    }
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext mongoMappingContext, MongoDatabaseFactory factory) {
        mongoMappingContext.setAutoIndexCreation(true);
        mongoMappingContext.afterPropertiesSet();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        // 此处是去除插入数据库的 _class 字段
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}