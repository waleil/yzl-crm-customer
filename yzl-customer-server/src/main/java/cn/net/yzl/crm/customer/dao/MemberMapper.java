package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.member.MemberAddressAndLevelDTO;
import cn.net.yzl.crm.customer.dto.member.MemberDiseaseCustomerDto;
import cn.net.yzl.crm.customer.dto.member.MemberDiseaseDto;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.model.mogo.MemberProduct;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MemberMapper {
    List<MemberGrad> getMemberGrad();

//    @DataSourceSelector(value = DynamicDataSourceEnum.master)
    int insertSelective(Member record);

    List<Member> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member selectMemberByCard(String memberCard);

    List<MemberPhone> getMemberPhoneList(String member_card);

    Member getMemberByPhone(List<String> phoneList);

    void setMemberToVip(String member_card);

    List<MemberProductEffect> getMemberProductEffectList(String member_card);

    List<ProductConsultation> getProductConsultationList(String member_card);

    Integer insertMemberDisease(MemberDiseaseDto memberDiseaseDto);


    List<MemberDisease> getMemberDiseaseByCardAndDiseaseId(@Param("memberCard") String memberCard,@Param("diseaseId") Integer diseaseId);

    List<MemberDisease> getMemberDisease(String member_card);

    List<MemberDisease> getMemberDiseaseByMemberCards(List<String> member_cards);

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

//    /**
//     * 获取顾客行为偏好
//     * @param member_card
//     * @return
//     */
//    MemberAction getMemberAction(String member_card);
//
//    /**
//     * 新增顾客行为偏好
//     * @param memberAction
//     */
//    void saveMemberAction(MemberAction memberAction);
//
//    void updateMemberAction(MemberAction memberAction);

    List<MemberOrderStatViewModel> getMemberList(List<String> member_cards);

    /**
     *  添加顾客圈选
     * @param crowdGroup
     * @return
     */
    int addCrowdGroup(CrowdGroup crowdGroup);

    List<CrowdGroup> getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO);

    /**
     * 根据圈选id批量获取圈选
     * @return
     */
    List<CrowdGroup> getCrowdGroupByIds(List<Integer> groupIds);

    /**
     * 获取顾客行为偏好字典数据
     * @return
     */
    List<MemberBaseAttr> getmemberActions();

    void updateCrowdGroup(CrowdGroup crowdGroup);

    void delCrowdGroupById(int id);

    List<MemberAmount> getMemberAmount(List<String> member_cards);

    List<member_wide> selectFullMemberByPage(int currentPage, int pageSize);

    List<MemberLabel> queryAllMemberByPage(int id, int pageSize);

    String queryMaxMemberCard();

    List<MemberDiseaseCustomerDto> getMemberDiseaseDtoByMemberCard(@Param("memberCard") String memberCard);

    List<MemberAddressAndLevelDTO> getMembereAddressAndLevelByMemberCards(@Param("list")  List<String> memberCards);

    int findPageByConditionCount(MemberSerchConditionDTO dto);
    /**
     * 更新顾客最后下单时间
     * @return
     */
    int updateLastOrderTime(@Param("memberCard") String memberCard,@Param("now") Date now);

    List<MemberLabel> queryMemberLabelByCodes(List<String> memberCodes);

    List<MemberDisease> queryDiseaseByMemberCodes(List<String> memberCodes);

    List<ActionDict> queryActionByMemberCodes(List<String> memberCodes);

    List<MemberProduct> queryProductByMemberCodes(List<String> memberCodes);
}