package cn.net.yzl.crm.customer.dao.memberDictMapper;

import cn.net.yzl.crm.customer.dto.member.CharacterDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.CharacterDict;

public interface CharacterDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CharacterDictDto record);

    int insertSelective(CharacterDictDto record);

    CharacterDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CharacterDictDto record);

    int updateByPrimaryKey(CharacterDictDto record);
}