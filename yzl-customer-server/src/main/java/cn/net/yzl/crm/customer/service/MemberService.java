package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.MemberAndAddWorkOrderVO;
import cn.net.yzl.crm.customer.vo.MemberDiseaseIdUpdateVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.crm.customer.vo.member.MemberGrandSelectVo;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderDiseaseVo;
import cn.net.yzl.crm.customer.vo.work.MemeberWorkOrderSubmitVo;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    int insert(Member record);

    ComResponse<Page<Member>> findPageByCondition(MemberSerchConditionDTO dto);

    int updateByMemberCardSelective(Member dto);

    Member selectMemberByCard(String memberCard);

    Member getMemberByPhone(String phone);

    void setMemberToVip(String member_card);
    List<MemberProductEffect> getMemberProductEffectList(String member_card);

    List<ProductConsultation> getProductConsultationList(String member_card);

    ComResponse<Integer> insertMemberDisease(MemberDiseaseDto memberDiseaseDto);

    ComResponse<List<MemberDiseaseCustomerDto>> getMemberDisease(String member_card);

//    void saveReveiverAddress(ReveiverAddress reveiverAddress);

//    void updateReveiverAddress(ReveiverAddress reveiverAddress);

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
     * 获取顾客行为偏好字典数据
     *
     * @return
     */
//    List<MemberBaseAttr> getmemberActions();

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

    List<MemberAmount> getMemberAmount(List<String> member_cards);

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

    // 添加顾客咨询商品
    ComResponse<String> addProductConsultation(List<ProductConsultationInsertVO> productConsultationInsertVOList);

    /**
     * 添加顾客咨询商品(批量Intert保存)
     * wangzhe
     * 2021-02-26
     * @param productConsultationInsertVOList
     * @return
     */
    ComResponse<String> batchSaveProductConsultation(List<ProductConsultationInsertVO> productConsultationInsertVOList);

    List<MemberAddressAndLevelDTO> getMembereAddressAndLevelByMemberCards(List<String> memberCardList);

    ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordList(String memberCard);

    ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordListByTimeRange(MemberGrandSelectVo vo);

    public Integer updateMemberDiseaseByDiseaseId(MemberDiseaseIdUpdateVO vo);

    /**
     * 处理实时进线时，保存顾客信息
     *
     * @param coilInVo
     * @return
     */

    public ComResponse<MemberGroupCodeDTO> coilInDealMemberData(MemberCoilInVO coilInVo);

    //public ComResponse<MemberGroupCodeDTO> coilInSaveMemberData(MemberCoilInVO coilInVo);

    /**
     * 订单签收是更新顾客关联的信息
     * wanghe
     * 2021-02-07
     * @param orderInfo4MqVo
     * @return
     */
    ComResponse<Boolean> orderSignUpdateMemberData(OrderSignInfo4MqVO orderInfo4MqVo);

    ComResponse<Boolean> dealOrderCreateUpdateMemberData(OrderCreateInfoVO orderCreateInfoVO);

//    ComResponse<Boolean> hangUpUpdateMemberData(MemberHangUpVO memberHangUpVO);

//    boolean updateMemberLabel();
    boolean updateMemberLabelForTask();

    int saveMemberReferral(MemberAndAddWorkOrderVO memberReferralVO);

    boolean updateMemberGrandValidityInit() throws IOException;

    /**
     * 提交工单时处理业务
     * @param workOrderSubmitVo
     * @return
     */
    ComResponse<Boolean> memeberWorkOrderSubmit(MemeberWorkOrderSubmitVo workOrderSubmitVo);


    public Integer updateMemberDisease(String memberCard,String createNo, List<MemberWorkOrderDiseaseVo> memberDiseaseList);

    boolean addredis(String memberCard);


    boolean updateMemberOrderQuotaTask();

}
