package cn.net.yzl.crm.customer.dao.memberDictRelation;

import cn.net.yzl.crm.customer.dto.member.MemberContactTimeRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberContactTimeRelation;

import java.util.List;

public interface MemberContactTimeRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByMemberCard(String cardNo);

    int insert(MemberContactTimeRelationDto record);

    int insertSelective(MemberContactTimeRelationDto record);

    MemberContactTimeRelation selectByPrimaryKey(Integer id);

    List<MemberContactTimeRelation> selectByMemberCard(String cardNo);

    int updateByPrimaryKeySelective(MemberContactTimeRelationDto record);

    int updateByPrimaryKey(MemberContactTimeRelationDto record);
}