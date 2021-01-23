package cn.net.yzl.crm.customer.dao.memberDictRelation;

import cn.net.yzl.crm.customer.dto.member.MemberCharacterRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberCharacterRelation;

public interface MemberCharacterRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberCharacterRelationDto record);

    int insertSelective(MemberCharacterRelationDto record);

    MemberCharacterRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberCharacterRelationDto record);

    int updateByPrimaryKey(MemberCharacterRelationDto record);
}