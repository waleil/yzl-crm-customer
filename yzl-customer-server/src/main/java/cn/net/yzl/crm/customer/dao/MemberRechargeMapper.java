package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberRecharge;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRechargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberRecharge record);

    int insertSelective(MemberRecharge record);

    MemberRecharge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberRecharge record);

    int updateByPrimaryKey(MemberRecharge record);
}