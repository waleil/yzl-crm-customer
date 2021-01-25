package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemberActionRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberActionRelation record);

    int insertSelective(MemberActionRelation record);

    MemberActionRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberActionRelation record);

    int updateByPrimaryKey(MemberActionRelation record);

    List<cn.net.yzl.crm.customer.model.mogo.ActionDict> queryByMemberCodes(@Param("list") List<String> codes);
}