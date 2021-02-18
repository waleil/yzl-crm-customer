package cn.net.yzl.crm.customer.config.mongodb;

import cn.net.yzl.crm.customer.dao.mongo.MongoBaseDao;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDemo extends MongoBaseDao<MemberOrderStatViewModel> {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 反射获取泛型类型
     */
    @Override
    protected Class<MemberOrderStatViewModel> getEntityClass() {
        return MemberOrderStatViewModel.class;
    }



}
