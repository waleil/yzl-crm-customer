package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;

public interface MemberActionRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberActionRelation record);

    int insertSelective(MemberActionRelation record);

    MemberActionRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberActionRelation record);

    int updateByPrimaryKey(MemberActionRelation record);
}