package cn.net.yzl.crm.customer.dao.memberDictMapper;

import cn.net.yzl.crm.customer.dto.member.OrderBehaviorDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.OrderBehaviorDict;

public interface OrderBehaviorDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderBehaviorDictDto record);

    int insertSelective(OrderBehaviorDictDto record);

    OrderBehaviorDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderBehaviorDictDto record);

    int updateByPrimaryKey(OrderBehaviorDictDto record);
}