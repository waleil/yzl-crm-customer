package cn.net.yzl.crm.customer.service.orderWarehouseRule;

import cn.net.yzl.crm.customer.model.OrderWarehouseRule;

public interface IOrderWarehouseRuleService {
    int insert(OrderWarehouseRule record);
    OrderWarehouseRule selectById(String id);
}
