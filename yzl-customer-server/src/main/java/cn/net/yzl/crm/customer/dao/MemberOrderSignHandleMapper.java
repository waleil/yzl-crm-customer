package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.dto.PageDTO;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberOrderSignHandleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberOrderSignHandle record);

    int insertSelective(MemberOrderSignHandle record);

    MemberOrderSignHandle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberOrderSignHandle record);

    int updateByPrimaryKeyWithBLOBs(MemberOrderSignHandle record);

    int updateByPrimaryKey(MemberOrderSignHandle record);

    /**
     * 更新处理成功的消息
     * wangzhe
     * 2021-03-18
     * @param primaryKey
     * @return
     */
    Integer updateSuccessStatusByPrimaryKey(@Param("primaryKey") Integer primaryKey);

    Integer getFailRecordListCount();


    List<MemberOrderSignHandle> getFailRecordList(PageDTO page);
}