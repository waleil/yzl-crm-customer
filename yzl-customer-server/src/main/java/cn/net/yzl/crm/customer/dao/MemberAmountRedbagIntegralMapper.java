package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.model.MemberAmountRedbagIntegral;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAmountRedbagIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAmountRedbagIntegral record);

    int insertSelective(MemberAmountRedbagIntegral record);

    MemberAmountRedbagIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAmountRedbagIntegral record);

    int updateByPrimaryKey(MemberAmountRedbagIntegral record);


    MemberAmountRedbagIntegral selectByMemberCard(String memberCard);
}