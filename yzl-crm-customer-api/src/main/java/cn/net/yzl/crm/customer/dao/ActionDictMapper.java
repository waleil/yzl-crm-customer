package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
/**
 * @Author: lichanghong
 * @Description: 顾客行为dao层
 * @Date: 2021/1/25 4:33 下午
 */
public interface ActionDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActionDict record);

    int insertSelective(ActionDict record);

    ActionDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActionDict record);

    int updateByPrimaryKey(ActionDict record);
}