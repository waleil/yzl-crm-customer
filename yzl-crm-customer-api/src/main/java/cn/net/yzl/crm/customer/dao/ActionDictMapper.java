package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;

public interface ActionDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActionDict record);

    int insertSelective(ActionDict record);

    ActionDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActionDict record);

    int updateByPrimaryKey(ActionDict record);
}