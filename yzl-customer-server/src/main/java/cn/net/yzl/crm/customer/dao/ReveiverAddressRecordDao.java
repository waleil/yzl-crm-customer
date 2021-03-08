package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.ReveiverAddressRecordPo;
import org.apache.ibatis.annotations.Param;

public interface ReveiverAddressRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ReveiverAddressRecordPo record);

    int insertSelective(ReveiverAddressRecordPo record);

    ReveiverAddressRecordPo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReveiverAddressRecordPo record);

    int updateByPrimaryKey(ReveiverAddressRecordPo record);

    int updateDefaultFlagByMemberCard(@Param("memberCard") String memberCard,@Param("oldFlag")  int oldFlag,@Param("newFlag")  int newFlag);
}