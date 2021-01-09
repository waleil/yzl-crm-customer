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

    /**
     * 新增收获地址
     * @param reveiverAddress
     */
    void saveReveiverAddress(ReveiverAddress reveiverAddress);

    /**
     * 修改收获地址
     * @param reveiverAddress
     */
    void updateReveiverAddress(ReveiverAddress reveiverAddress);

    /**
     * 获取收获地址
     * @param member_card
     * @return
     */
    List<ReveiverAddress> getReveiverAddress(String member_card);

    /**
     * 获取购买能力
     * @param member_card
     * @return
     */
    MemberOrderStat getMemberOrderStat(String member_card);

    /**
     * 保存购买能力
     * @param memberOrderStat
     */
    void addMemberOrderStat(MemberOrderStat memberOrderStat);

    /**
     * 修改购买能力
     * @param memberOrderStat
     */
    void updateMemberOrderStat(MemberOrderStat memberOrderStat);

    /**
     * 获取顾客行为偏好
     * @param member_card
     * @return
     */
    MemberAction getMemberAction(String member_card);

    /**
     * 新增顾客行为偏好
     * @param memberAction
     */
    void saveMemberAction(MemberAction memberAction);

    void updateMemberAction(MemberAction memberAction);
}