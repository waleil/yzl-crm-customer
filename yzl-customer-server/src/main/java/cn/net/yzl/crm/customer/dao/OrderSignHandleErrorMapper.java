package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.model.db.OrderSignHandleError;

public interface OrderSignHandleErrorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSignHandleError record);

    int insertSelective(OrderSignHandleError record);

    OrderSignHandleError selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSignHandleError record);

    int updateByPrimaryKeyWithBLOBs(OrderSignHandleError record);

    int updateByPrimaryKey(OrderSignHandleError record);
}