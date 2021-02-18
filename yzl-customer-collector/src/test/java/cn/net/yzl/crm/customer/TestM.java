package cn.net.yzl.crm.customer;

import cn.net.yzl.crm.customer.collector.CollectorApplication;
import cn.net.yzl.crm.customer.collector.service.MongoTransactionManagerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lichanghong
 * @version 1.0
 * @title: Test
 * @description todo
 * @date: 2021/1/30 4:14 下午
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CollectorApplication.class)
public class TestM {
    @Autowired
    private MongoTransactionManagerServer mongoTransactionManagerServer;
    @Test
    public void tt() throws Throwable {
        mongoTransactionManagerServer.testTransaction("1","2");
    }

}
