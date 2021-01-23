package cn.net.yzl.crm.customer.dao.memberDictRelation;

import cn.net.yzl.crm.customer.dto.member.MemberOrderBehaviorRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberOrderBehaviorRelation;

public interface MemberOrderBehaviorRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberOrderBehaviorRelationDto record);

    int insertSelective(MemberOrderBehaviorRelationDto record);

    MemberOrderBehaviorRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberOrderBehaviorRelationDto record);

    int updateByPrimaryKey(MemberOrderBehaviorRelationDto record);
}