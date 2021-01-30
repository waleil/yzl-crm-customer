package cn.net.yzl.crm.customer.collector.service;

import cn.net.yzl.crm.customer.collector.dao.mongo.MongoTransactionDemoDao;
import cn.net.yzl.crm.customer.collector.dao.mongo.MongoTransactionDemoDao1;
import cn.net.yzl.crm.customer.collector.model.MongoTransactionDemo;
import cn.net.yzl.crm.customer.collector.model.MongoTransactionDemo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MongoTransactionManagerServer
 * @description todo
 * @date: 2021/1/30 4:00 下午
 */
@Service
public class MongoTransactionManagerServer {

    @Autowired
    private MongoTransactionDemoDao1 mongoTransactionDemoDao1;
    @Autowired
    private MongoTransactionDemoDao mongoTransactionDemoDao;
    /**
     * @Author: lichanghong
     * @Description:
     * @Date: 2021/1/30 4:03 下午
     * @param
     * @Return: boolean
     */
    @Transactional(value = "mongoTransactionManager",rollbackFor = Throwable.class)
    public boolean testTransaction(String id,String name) throws Throwable {

        MongoTransactionDemo demo = new MongoTransactionDemo();

        MongoTransactionDemo1 demo1 = new MongoTransactionDemo1();
        demo.setId("222");
        demo.setName(name);
        mongoTransactionDemoDao.save(demo);
        MongoTransactionDemo d=  mongoTransactionDemoDao.queryById(id);
        if(d!=null&&d.getId().equals("1")){
            throw new Throwable("111");
        }
        demo1.setId("222");
        demo1.setName(name);
        mongoTransactionDemoDao1.save(demo1);
        //MongoTransactionDemo1 d1=  mongoTransactionDemoDao1.queryById(id);
        return true;
    }
}
