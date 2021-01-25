package cn.net.yzl.crm.customer.dao.mongo;

import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import org.springframework.stereotype.Component;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelDao
 * @description 会员宽表数据
 * @date: 2021/1/25 5:36 下午
 */
@Component
public class MemberLabelDao extends MongoBaseDao<MemberLabel> {
    @Override
    protected Class<MemberLabel> getEntityClass() {
        return MemberLabel.class;
    }

}
