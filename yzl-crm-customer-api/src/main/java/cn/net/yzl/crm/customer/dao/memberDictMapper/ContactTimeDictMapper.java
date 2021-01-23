package cn.net.yzl.crm.customer.dao.memberDictMapper;


import cn.net.yzl.crm.customer.dto.member.ContactTimeDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.ContactTimeDict;

import java.util.List;

public interface ContactTimeDictMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteAll();

    int insert(ContactTimeDictDto record);

    int insertSelective(ContactTimeDictDto record);

    ContactTimeDict selectByPrimaryKey(Integer id);

    List<ContactTimeDict> selectAll();

    int updateByPrimaryKeySelective(ContactTimeDictDto record);

    int updateByPrimaryKey(ContactTimeDictDto record);
}