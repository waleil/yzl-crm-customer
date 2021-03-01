package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;

public interface MemberOrderSignHandleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberOrderSignHandle record);

    int insertSelective(MemberOrderSignHandle record);

    MemberOrderSignHandle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberOrderSignHandle record);

    int updateByPrimaryKeyWithBLOBs(MemberOrderSignHandle record);

    int updateByPrimaryKey(MemberOrderSignHandle record);
}