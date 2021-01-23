package cn.net.yzl.crm.customer.dao.memberDictRelation;


import cn.net.yzl.crm.customer.dto.member.MemberResponseTimeRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberResponseTimeRelation;

public interface MemberResponseTimeRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberResponseTimeRelationDto record);

    int insertSelective(MemberResponseTimeRelationDto record);

    MemberResponseTimeRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberResponseTimeRelationDto record);

    int updateByPrimaryKey(MemberResponseTimeRelationDto record);
}