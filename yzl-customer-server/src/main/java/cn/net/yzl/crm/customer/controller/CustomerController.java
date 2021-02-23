package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.member.*;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.mongomodel.member_wide;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.BeanUtil;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import cn.net.yzl.crm.customer.vo.MemberAndAddWorkOrderVO;
import cn.net.yzl.crm.customer.vo.MemberDiseaseIdUpdateVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import cn.net.yzl.crm.customer.vo.label.MemberCoilInVO;
import cn.net.yzl.crm.customer.vo.order.OrderCreateInfoVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import cn.net.yzl.crm.customer.vo.work.MemberWorkOrderInfoVO;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(value="顾客信息",tags = {"顾客信息"})
@RestController
@RequestMapping("member")
public class CustomerController {

    @Autowired
    MemberService memberService;

    @ApiOperation("顾客列表-查询")
    @PostMapping("v1/getMemberListByPage")
    public ComResponse<Page<Member>> getMemberListByPage(@RequestBody MemberSerchConditionDTO dto) throws IllegalAccessException {
        dto = BeanUtil.setNullValue(dto);
        return memberService.findPageByCondition(dto);


    }

    @ApiOperation(value = "保存会员基本信息")
    @PostMapping("v1/save")
    public GeneralResult<Boolean> save(@RequestBody Member dto) {
        int result = memberService.insert(dto);
        if (result == 1) {
            return GeneralResult.success(Boolean.TRUE);
        } else {
            return GeneralResult.success(Boolean.FALSE);
        }

    }

    @ApiOperation(value = "保存转介绍用户")
    @PostMapping("v1/saveMemberReferral")
    public GeneralResult<Boolean> saveMemberReferral(@RequestBody MemberAndAddWorkOrderVO memberReferralVO) {
        if (memberReferralVO == null || memberReferralVO.getMemberVO() ==null || memberReferralVO.getWorkOrderBeanVO() == null){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        int result = memberService.saveMemberReferral(memberReferralVO);
        if (result == 1) {
            return GeneralResult.success(Boolean.TRUE);
        } else {
            return GeneralResult.success(Boolean.FALSE);
        }
    }




    @ApiOperation(value = "更新会员基本信息")
    @PostMapping("v1/updateByMemberCard")
    public GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto) {
        if (StringUtils.isEmpty(dto.getMember_card())) {
            return GeneralResult.errorWithMessage(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "memberCard不能为空");
        }
        int result = memberService.updateByMemberCardSelective(dto);
        if (result != 1) {
            return GeneralResult.success(Boolean.FALSE);
        }
        return GeneralResult.success(Boolean.TRUE);
    }

    @ApiOperation(value = "根据顾客号查询顾客基本信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard) {
        Member memberEntity = memberService.selectMemberByCard(memberCard);
        List<MemberPhone> memberPhoneList = memberService.getMemberPhoneList(memberCard); //获取联系方式
        List<ReveiverAddress> addressList = memberService.getReveiverAddress(memberCard); //获取收获地址
        List<String> member_cards = new ArrayList<>();
        member_cards.add(memberCard);
        List<MemberAmount> memberAmountList = memberService.getMemberAmount(member_cards); //获取顾客账户信息
        if (memberAmountList != null && memberAmountList.size() > 0) {
            memberEntity.setMember_amount(memberAmountList.get(0));
        }
        if(addressList!=null && addressList.size()>0){
            memberEntity.setReceive_address_list(addressList);
        }
        if(memberPhoneList!=null && memberPhoneList.size()>0){
            memberEntity.setMemberPhoneList(memberPhoneList);
        }
        return GeneralResult.success(memberEntity);
    }


    @ApiOperation("获取顾客联系方式信息，包括手机号，座机号")
    @GetMapping("v1/getMemberPhoneList")
    public GeneralResult getMemberPhoneList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<MemberPhone> memberPhoneList = memberService.getMemberPhoneList(member_card);
        return GeneralResult.success(memberPhoneList);
    }

    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     *
     * @param phone
     * @return
     */
    @ApiOperation("根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）")
    @GetMapping("v1/getMemberByPhone")
    public GeneralResult getMemberByPhone(
            @RequestParam("phone")
            @NotBlank(message = "phone不能为空")
            @ApiParam(name = "phone", value = "手机号", required = true)
                    String phone) {
        Member member = memberService.getMemberByPhone(phone);
        return GeneralResult.success(member);
    }

//    @GetMapping("getMemberByPHone1")
//    public GeneralResult getMemberByPHone1(){
//        GeneralResult<Member> result= this.getMemberByPhone("13832955330");
//        return result;
//    }

    /**
     * 设置顾客为会员
     *
     * @param member_card
     * @return
     */
    @ApiOperation("设置顾客为会员")
    @GetMapping("v1/setMemberToVip")
    public GeneralResult setMemberToVip(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        memberService.setMemberToVip(member_card);
        return GeneralResult.success();
    }



    @ApiOperation("获取顾客购买商品")
    @GetMapping("v1/getMemberProductEffectList")
    public GeneralResult<List<MemberProductEffect>> getMemberProductEffectList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<MemberProductEffect> memberProductEffectList = memberService.getMemberProductEffectList(member_card);
        return GeneralResult.success(memberProductEffectList);
    }

    @ApiOperation("获取顾客咨询商品")
    @GetMapping("v1/getProductConsultationList")
    public GeneralResult<List<ProductConsultation>> getProductConsultationList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<ProductConsultation> memberServiceProductConsultationList = memberService.getProductConsultationList(member_card);
        return GeneralResult.success(memberServiceProductConsultationList);
    }

    @ApiOperation("顾客画像-添加顾客咨询商品")
    @PostMapping("v1/addProductConsultation")
    public ComResponse<String> addProductConsultation(@RequestBody @Validated List<ProductConsultationInsertVO> productConsultationInsertVOList) {
        return memberService.addProductConsultation(productConsultationInsertVOList);
    }

    @ApiOperation("顾客画像-获取顾客病症")
    @GetMapping("v1/getMemberDisease")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<MemberDiseaseCustomerDto>> getMemberDisease(@NotBlank String memberCard) {
        return  memberService.getMemberDisease(memberCard);

    }

    @ApiOperation("顾客画像-添加顾客病症")
    @PostMapping("v1/insertMemberDisease")
    public ComResponse insertMemberDisease(@RequestBody @Validated MemberDiseaseDto diseaseDto) {
        ComResponse<Integer> integer = memberService.insertMemberDisease(diseaseDto);
        return integer;
    }


    @ApiOperation("获取顾客购买能力")
    @GetMapping("/v1/getMemberOrderStat")
    public GeneralResult getMemberOrderStat(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        MemberOrderStat memberOrderStat = memberService.getMemberOrderStat(member_card);
        return GeneralResult.success(memberOrderStat);
    }

    @ApiOperation("保存顾客购买能力")
    @PostMapping("/v1/addMemberOrderStat")
    public GeneralResult addMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat) {
        if (memberOrderStat == null) return GeneralResult.errorWithMessage(101, "参数空");
        memberService.addMemberOrderStat(memberOrderStat);
        return GeneralResult.success();
    }

    @ApiOperation("修改顾客购买能力")
    @PostMapping("/v1/updateMemberOrderStat")
    public GeneralResult updateMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat) {
        if (memberOrderStat == null || StringUtil.isNullOrEmpty(memberOrderStat.getMember_card()))
            return GeneralResult.errorWithMessage(101, "参数空");

        memberService.updateMemberOrderStat(memberOrderStat);

        return GeneralResult.success();
    }


    @ApiOperation("根据一批会员卡号获取会员信息，会员卡号用英文逗号分隔")
    @GetMapping("/v1/getMemberList")
    public ComResponse getMemberList(
            @RequestParam("member_cards")
            @NotBlank(message = "member_cards不能为空")
            @ApiParam(name = "member_cards", value = "会员卡号", required = true)
                    String member_cards
    ) {
        List<String> list = Arrays.asList(member_cards.split(","));
        List<MemberOrderStatViewModel> memberOrderStatViewModels = memberService.getMemberList(list);
        return ComResponse.success(memberOrderStatViewModels);
    }


    @ApiOperation("获取顾客行为偏好字典数据")
    @GetMapping("/v1/getMemberActions")
    public ComResponse getMemberActions() {
        List<MemberBaseAttr> list = memberService.getmemberActions();
        return ComResponse.success(list);
    }

    @ApiOperation("删除顾客圈选")
    @GetMapping("/v1/delMemberCrowdGroup")
    public ComResponse delMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId) {
        //   memberService.delMemberCrowdGroup(crowdId);
        memberService.delCrowdGroupById(Integer.parseInt(crowdId));
        return ComResponse.success();
    }

    @ApiOperation("顾客一批顾客卡号获取顾客病症(卡号用英文逗号分隔)")
    @GetMapping("/v1/getMemberDiseaseByMemberCards")
    public ComResponse getMemberDiseaseByMemberCards(
            @RequestParam("member_cards")
            @NotBlank(message = "member_cards不能为空")
            @ApiParam(name = "member_cards", value = "一批顾客卡号", required = true)
                    String member_cards) {

        if (StringUtil.isNullOrEmpty(member_cards)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        List<String> memberCardList = Arrays.asList(member_cards.split(","));
        List<MemberDisease> list = memberService.getMemberDiseaseByMemberCards(memberCardList);
        return ComResponse.success(list);
    }

    @ApiOperation("syncMemberToMongo")
    @GetMapping("syncMemberToMongo")
    public void syncMemberToMongo() throws Exception {
        for (int i = 0; i <= 100; i++) {
            List<member_wide> member_wideList = memberService.selectFullMemberByPage(i, 1000);
            for (member_wide member : member_wideList
            ) {
                member_wide mongoMember = memberService.getMemberFromMongo(member.getMember_card());
                if (mongoMember == null) {
                    memberService.saveMemberToMongo(member);
                    continue;
                }
                memberService.updateMemberToMongo(member);
            }
        }
    }

    @ApiOperation("顾客一批顾客卡号获取顾客收货地址、余额、会员等级)")
    @GetMapping("/v1/getMembereAddressAndLevelByMemberCards")
    public ComResponse getMembereAddressAndLevelByMemberCards(
            @RequestParam("member_cards")
            @NotBlank(message = "member_cards不能为空")
            @ApiParam(name = "member_cards", value = "一批顾客卡号", required = true)
                    String member_cards) {

        if (StringUtil.isNullOrEmpty(member_cards)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        List<String> memberCardList = Arrays.asList(member_cards.split(","));
        List<MemberAddressAndLevelDTO> list = memberService.getMembereAddressAndLevelByMemberCards(memberCardList);
        return ComResponse.success(list);
    }

    @ApiOperation("获取会员级别记录")
    @GetMapping("v1/getMemberGradeRecordList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberCard", value = "会员卡号", required = true, dataType = "string", paramType = "query")
    })
    public ComResponse<List<MemberGradeRecordDto>> getMemberGradeRecordList(@NotBlank String memberCard) {
        return  memberService.getMemberGradeRecordList(memberCard);

    }


    @ApiOperation("顾客病症-根据病症id更新顾客病症")
    @PostMapping("/v1/updateMemberDiseaseByDiseaseId")
    public ComResponse<Integer> updateMemberDiseaseByDiseaseId(@RequestBody MemberDiseaseIdUpdateVO memberDiseaseIdUpdateVO) {
        if (memberDiseaseIdUpdateVO == null || memberDiseaseIdUpdateVO.getNewDiseaseId() == null || memberDiseaseIdUpdateVO.getOldDiseaseId() == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        Integer integer = memberService.updateMemberDiseaseByDiseaseId(memberDiseaseIdUpdateVO);
        return  ComResponse.success(integer);

    }


    /**
     * 处理实时进线时，保存顾客信息
     * @param coilInVo
     * @return
     */
    @ApiOperation("顾客管理-实时进线时，处理顾客信息")
    @PostMapping("/v1/coilInDealMemberData")
    public ComResponse<MemberGroupCodeDTO> coilInDealMemberData(@RequestBody MemberCoilInVO coilInVo) {
        if (coilInVo == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        ComResponse<MemberGroupCodeDTO> response = memberService.coilInDealMemberData(coilInVo);
        return response;

    }

    @ApiOperation("顾客管理-处理工单时更新顾客信息")
    @PostMapping("/v1/dealWorkOrderUpdateMemberData")
    public ComResponse<Boolean> dealWorkOrderUpdateMemberData(@RequestBody MemberWorkOrderInfoVO workOrderInfoVO) {
        if (workOrderInfoVO == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        ComResponse<Boolean> response = memberService.dealWorkOrderUpdateMemberData(workOrderInfoVO);
        return response;

    }

    @ApiOperation("顾客管理-处理下单时更新顾客信息")
    @PostMapping("/v1/dealOrderCreateUpdateMemberData")
    public ComResponse<Boolean> dealOrderCreateUpdateMemberData(@RequestBody OrderCreateInfoVO orderCreateInfoVO) {
        if (orderCreateInfoVO == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        ComResponse<Boolean> response = memberService.dealOrderCreateUpdateMemberData(orderCreateInfoVO);
        return response;

    }

    @ApiOperation("顾客管理-订单签收后更新顾客信息")
    @PostMapping("/v1/orderSignUpdateMemberData")
    public ComResponse<Boolean> orderSignUpdateMemberData(@RequestBody OrderSignInfo4MqVO orderInfo4MqVo) {
        if (orderInfo4MqVo == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        ComResponse<Boolean> response = memberService.orderSignUpdateMemberData(orderInfo4MqVo);
        return response;

    }

    /*@ApiOperation("顾客管理-结束通话后更新顾客信息")
    @PostMapping("/v1/hangUpUpdateMemberData")
    public ComResponse<Boolean> hangUpUpdateMemberData(@RequestBody MemberHangUpVO memberHangUpVO) {
        if (memberHangUpVO == null) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空");
        }
        ComResponse<Boolean> response = memberService.hangUpUpdateMemberData(memberHangUpVO);
        return response;

    }*/
    @ApiOperation("同步顾客标签数据")
    @PostMapping("/v1/updateMemberLabelTimedTask")
    public ComResponse<Boolean> updateMemberLabel(){
        boolean b = memberService.updateMemberLabel();
        return ComResponse.success(b);
    }

    @ApiOperation("根据DMC规则初始化会员级别")
    @PostMapping("/v1/updateMemberGrandValidityInitTimedTask")
    public ComResponse<Boolean> updateMemberGrandValidityInit() throws IOException {
        boolean b = memberService.updateMemberGrandValidityInit();
        return ComResponse.success(b);
    }




}
