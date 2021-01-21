package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;

import java.util.List;

public interface MemberService {
    List<MemberGrad> getMemberGrad();

    int insert(Member record);

    Page<Member> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member selectMemberByCard(String memberCard);

    List<MemberPhone> getMemberPhoneList(String member_card);

    Member getMemberByPhone(String phone);

    void setMemberToVip(String member_card);

    List<MemberProductEffect> getMemberProductEffectList(String member_card);

    List<ProductConsultation> getProductConsultationList(String member_card);

    List<MemberDisease> getMemberDisease(String member_card);

    void saveReveiverAddress(ReveiverAddress reveiverAddress);

    void updateReveiverAddress(ReveiverAddress reveiverAddress);

    List<ReveiverAddress> getReveiverAddress(String member_card);

    /**
     * 获取购买能力
     *
     * @param member_card
     * @return
     */
    MemberOrderStat getMemberOrderStat(String member_card);

    /**
     * 保存购买能力
     *
     * @param memberOrderStat
     */
    void addMemberOrderStat(MemberOrderStat memberOrderStat);

    /**
     * 修改购买能力
     *
     * @param memberOrderStat
     */
    void updateMemberOrderStat(MemberOrderStat memberOrderStat);

//    /**
//     * 获取顾客行为偏好
//     *
//     * @param member_card
//     * @return
//     */
//    MemberAction getMemberAction(String member_card);
//
//    /**
//     * 新增顾客行为偏好
//     *
//     * @param memberAction
//     */
//    void saveMemberAction(MemberAction memberAction);
//
//    void updateMemberAction(MemberAction memberAction);

    List<MemberOrderStatViewModel> getMemberList(List<String> member_cards);

    /**
     * //     * 根据圈选id批量获取圈选
     * //     * @return
     * //
     */
    List<CrowdGroup> getCrowdGroupByIds(List<String> groupIds);


    Page<CrowdGroup> getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO);

    void saveMemberCrowdGroup(member_crowd_group member_crowd_group);

    /**
     * 根据crowd_id获取一个圈选
     *
     * @param crowdId
     * @return
     */
    member_crowd_group getMemberCrowdGroup(String crowdId);

    void updateMemberCrowdGroup(member_crowd_group member_crowd_group) throws Exception;

    /**
     * 获取顾客行为偏好字典数据
     *
     * @return
     */
    List<MemberBaseAttr> getmemberActions();

    /**
     * 删除圈选
     *
     * @param crowdId
     */
    void delMemberCrowdGroup(String crowdId);

    /**
     * 根据一批顾客卡号获取病症信息
     * @param member_cards
     * @return
     */
    List<MemberDisease> getMemberDiseaseByMemberCards(List<String> member_cards);

    /**
     * 添加顾客圈选到mysql
     * @param crowdGroup
     * @return
     */
    int addCrowdGroup(CrowdGroup crowdGroup);

    void updateCrowdGroup(CrowdGroup crowdGroup);

    void delCrowdGroupById(int id);

    List getMemberAmount(List<String> member_cards);

    List<member_wide> selectFullMemberByPage(int currentPage, int pageSize);

    /**
     * 根据卡号从mongo获取顾客信息
     * @param member_card
     * @return
     */
    member_wide getMemberFromMongo(String member_card);

    /**
     * 保存顾客信息到mongo
     * @param member
     */
    void saveMemberToMongo(member_wide member);

    /**
     * 修改mongo顾客信息
     * @param member
     */
    void updateMemberToMongo(member_wide member) throws Exception;
}
