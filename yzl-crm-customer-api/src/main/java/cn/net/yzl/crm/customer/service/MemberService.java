package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;

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

    List<MemberProductEffect> getMemberProductEffectList(String member_card);

    List<ProductConsultation> getProductConsultationList(String member_card);

    List<MemberDisease> getMemberDisease(String member_card);
}
