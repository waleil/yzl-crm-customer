package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.MemberAmount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAmountDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAmount record);

    int insertSelective(MemberAmount record);

    MemberAmount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAmount record);

    int updateByPrimaryKey(MemberAmount record);

    MemberAmountDto getMemberAmount(@Param("memberCard") String memberCard);
}