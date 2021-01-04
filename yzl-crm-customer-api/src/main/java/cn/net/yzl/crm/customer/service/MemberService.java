package cn.net.yzl.crm.customer.service;

import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberGrad;

import java.util.List;

public interface MemberService {
    List<MemberGrad> getMemberGrad();

    int insert(Member record);

    List<Member> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member  selectMemberByCard(String  memberCard);
}
