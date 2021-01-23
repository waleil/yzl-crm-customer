package cn.net.yzl.crm.customer.dao.memberDictRelation;

import cn.net.yzl.crm.customer.dto.member.MemberAgeRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberAgeRelation;

import java.util.List;

public interface MemberAgeRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByMemberCard(String card);

    int insert(MemberAgeRelationDto record);

    int insertSelective(MemberAgeRelationDto record);

    MemberAgeRelation selectByPrimaryKey(Integer id);

    List<MemberAgeRelation> selectByMemberCard(String card);

    int updateByPrimaryKeySelective(MemberAgeRelationDto record);

    int updateByPrimaryKey(MemberAgeRelationDto record);
}