package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.ReveiverAddressRecordPo;

public interface ReveiverAddressRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ReveiverAddressRecordPo record);

    int insertSelective(ReveiverAddressRecordPo record);

    ReveiverAddressRecordPo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReveiverAddressRecordPo record);

    int updateByPrimaryKey(ReveiverAddressRecordPo record);
}