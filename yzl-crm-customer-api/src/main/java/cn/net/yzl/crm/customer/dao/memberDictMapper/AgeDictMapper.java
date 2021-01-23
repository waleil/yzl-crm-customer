package cn.net.yzl.crm.customer.dao.memberDictMapper;


import cn.net.yzl.crm.customer.dto.member.AgeDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.AgeDict;

import java.util.List;

public interface AgeDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgeDictDto record);

    int insertSelective(AgeDictDto record);

    List<AgeDict> selectAll();

    int deleteAll();

    AgeDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AgeDictDto record);

    int updateByPrimaryKey(AgeDictDto record);
}