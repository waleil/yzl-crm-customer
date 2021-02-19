package cn.net.yzl.crm.customer.collector.dao;

import cn.net.yzl.crm.customer.collector.model.MemberAmountRedbagIntegral;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MemberAmountRedbagIntegralDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAmountRedbagIntegral record);

    int insertSelective(MemberAmountRedbagIntegral record);

    MemberAmountRedbagIntegral selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAmountRedbagIntegral record);

    int updateByPrimaryKey(MemberAmountRedbagIntegral record);

    List<MemberAmountRedbagIntegral> queryMemberCards(List<String> codes);
}