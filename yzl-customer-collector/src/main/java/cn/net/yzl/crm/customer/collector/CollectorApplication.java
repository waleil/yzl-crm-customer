package cn.net.yzl.crm.customer.collector;

import cn.net.yzl.common.swagger2.EnableSwagger;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CollectorApplication
 * @description todo
 * @date: 2021/1/29 6:31 下午
 */
@Slf4j
@EnableSwagger
@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.net.yzl.crm.customer.collector.client"})
@MapperScan("cn.net.yzl.crm.customer.collector.dao")
//@EnableTransactionManagement
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }
}
