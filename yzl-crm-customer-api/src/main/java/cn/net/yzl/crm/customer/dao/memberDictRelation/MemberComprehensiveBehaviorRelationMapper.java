package cn.net.yzl.crm.customer.dao.memberDictRelation;

import cn.net.yzl.crm.customer.dto.member.MemberComprehensiveBehaviorRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberComprehensiveBehaviorRelation;

public interface MemberComprehensiveBehaviorRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberComprehensiveBehaviorRelationDto record);

    int insertSelective(MemberComprehensiveBehaviorRelationDto record);

    MemberComprehensiveBehaviorRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberComprehensiveBehaviorRelationDto record);

    int updateByPrimaryKey(MemberComprehensiveBehaviorRelationDto record);
}