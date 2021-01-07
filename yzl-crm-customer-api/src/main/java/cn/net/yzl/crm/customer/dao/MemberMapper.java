package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.config.db.DataSourceSelector;
import cn.net.yzl.crm.customer.config.db.DynamicDataSourceEnum;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MemberMapper {
    List<MemberGrad> getMemberGrad();

    @DataSourceSelector(value = DynamicDataSourceEnum.master)
    int insert(Member record);

    List<Member> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member selectMemberByCard(String memberCard);

    List<MemberPhone> getMemberPhoneList(String member_card);

    Member getMemberByPhone(List<String> phoneList);

    void setMemberToVip(String member_card);

    List<MemberProductEffect> getMemberProductEffectList(String member_card);

    List<ProductConsultation> getProductConsultationList(String member_card);

    List<MemberDisease> getMemberDisease(String member_card);
}
