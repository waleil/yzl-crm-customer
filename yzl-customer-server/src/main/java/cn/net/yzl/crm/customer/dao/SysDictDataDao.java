package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.dto.member.MemberTypeDTO;
import cn.net.yzl.crm.customer.model.db.SysDictData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysDictDataDao {
    int deleteByPrimaryKey(Integer dictCode);

    int insert(SysDictData record);

    int insertSelective(SysDictData record);

    SysDictData selectByPrimaryKey(Integer dictCode);

    int updateByPrimaryKeySelective(SysDictData record);

    int updateByPrimaryKey(SysDictData record);

    List<MemberTypeDTO> queryMemberType();
}