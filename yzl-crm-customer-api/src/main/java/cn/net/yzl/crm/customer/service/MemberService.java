package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberGrad;
import cn.net.yzl.crm.customer.model.MemberPhone;

import java.util.List;

public interface MemberService {
    List<MemberGrad> getMemberGrad();

    int insert(Member record);

    Page<Member> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member  selectMemberByCard(String  memberCard);

    List<MemberPhone> getMemberPhoneList(String member_card);

    Member getMemberByPhone(String phone);

    void setMemberToVip(String member_card);
}
