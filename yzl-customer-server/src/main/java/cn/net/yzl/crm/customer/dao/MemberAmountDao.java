package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.MemberAmount;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAmountDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAmount record);

    int insertSelective(MemberAmount record);

    MemberAmount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAmount record);

    int updateByPrimaryKey(MemberAmount record);

    MemberAmountDto getMemberAmount(@Param("memberCard") String memberCard);

    List<cn.net.yzl.crm.customer.model.mogo.MemberAmount> queryByMemberCodes(List<String> codes);

    int updateConsumeDetailStatusById(Integer id);

    int updateConsumeDetailStatus(@Param("consumeDetail") MemberAmountDetail consumeDetail);

}