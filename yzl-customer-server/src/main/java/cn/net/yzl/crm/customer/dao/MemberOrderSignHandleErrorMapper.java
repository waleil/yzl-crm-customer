package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandleError;

public interface MemberOrderSignHandleErrorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberOrderSignHandleError record);

    int insertSelective(MemberOrderSignHandleError record);

    MemberOrderSignHandleError selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberOrderSignHandleError record);

    int updateByPrimaryKeyWithBLOBs(MemberOrderSignHandleError record);

    int updateByPrimaryKey(MemberOrderSignHandleError record);
}