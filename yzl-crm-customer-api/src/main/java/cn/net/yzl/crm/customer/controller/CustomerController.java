package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.service.MemberService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Api(value="CustomerController",tags = {"顾客服务"})
@RestController
@RequestMapping("member")
public class CustomerController {

    @Autowired
    MemberService memberService;

    @ApiOperation("获取顾客列表")
    @PostMapping("v1/getMemberListByPage")
    public GeneralResult<Page<Member>> getMemberListByPage(@RequestBody MemberSerchConditionDTO dto) {
        Page<Member> memberPage = memberService.findPageByCondition(dto);
        return GeneralResult.success(memberPage);

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

    @ApiOperation(value = "更新会员基本信息")
    @PostMapping("v1/updateByMemberCard")
    public GeneralResult<Boolean> updateByMemberCard(@RequestBody Member dto) {

        int result = memberService.updateByMemberCardSelective(dto);
        if (result == 1) {
            return GeneralResult.success(Boolean.TRUE);
        } else {
            return GeneralResult.success(Boolean.FALSE);
        }
    }

    @ApiOperation(value = "根据顾客号查询顾客基本信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String memberCard) {
        Member memberEntity = memberService.selectMemberByCard(memberCard);
        List<MemberPhone> memberPhoneList = memberService.getMemberPhoneList(memberCard);
        memberEntity.setMemberPhoneList(memberPhoneList);
        return GeneralResult.success(memberEntity);
    }

    @ApiOperation("获取顾客级别")
    @GetMapping("v1/getMemberGrad")
    public GeneralResult getMemberGrad() {
        return GeneralResult.success(memberService.getMemberGrad());
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

    /**
     * 获取顾客购买商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客购买商品")
    @GetMapping("v1/getMemberProductEffectList")
    public GeneralResult getMemberProductEffectList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<MemberProductEffect> memberProductEffectList = memberService.getMemberProductEffectList(member_card);
        return GeneralResult.success(memberProductEffectList);
    }

    /**
     * 获取顾客咨询商品
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客咨询商品")
    @GetMapping("v1/getProductConsultationList")
    public GeneralResult getProductConsultationList(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<ProductConsultation> memberServiceProductConsultationList = memberService.getProductConsultationList(member_card);
        return GeneralResult.success(memberServiceProductConsultationList);
    }

    /**
     * 获取顾客病症
     *
     * @param member_card
     * @return
     */
    @ApiOperation("获取顾客病症")
    @GetMapping("v1/getMemberDisease")
    public GeneralResult getMemberDisease(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card) {
        List<MemberDisease> memberDiseaseList = memberService.getMemberDisease(member_card);
        return GeneralResult.success(memberDiseaseList);
    }

    @ApiOperation("保存收货地址")
    @PostMapping("/v1/saveReveiverAddress")
    public GeneralResult saveReveiverAddress(@RequestBody ReveiverAddress reveiverAddress) {
        if (reveiverAddress == null) return GeneralResult.errorWithMessage(101, "参数空");

        // member_card为空表示是新增
        if (StringUtil.isNullOrEmpty(reveiverAddress.getMember_card())) {

            //todo 生成 Reveiver_address_code

            //   reveiverAddress.setReveiver_address_code();
            memberService.saveReveiverAddress(reveiverAddress);
        } else {
            memberService.updateReveiverAddress(reveiverAddress);
        }
        return GeneralResult.success();
    }

    @ApiOperation("获取顾客收货地址")
    @GetMapping("/v1/getReveiverAddress")
    public GeneralResult getReveiverAddress(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card
    ) {
        List<ReveiverAddress> reveiverAddressList = memberService.getReveiverAddress(member_card);
        return GeneralResult.success(reveiverAddressList);
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
    @PostMapping("/v1/saveMemberOrderStat")
    public GeneralResult saveMemberOrderStat(@RequestBody MemberOrderStat memberOrderStat) {
        if (memberOrderStat == null) return GeneralResult.errorWithMessage(101, "参数空");
        if (StringUtil.isNullOrEmpty(memberOrderStat.getMember_card())) {
            //todo 生成code码
            memberService.addMemberOrderStat(memberOrderStat);
        } else {
            memberService.updateMemberOrderStat(memberOrderStat);
        }
        return GeneralResult.success();
    }

    @ApiOperation("保存顾客行为偏好")
    @PostMapping("/v1/saveMemberAction")
    public GeneralResult saveMemberAction(@RequestBody MemberAction memberAction) {
        if (memberAction == null) return GeneralResult.errorWithMessage(101, "参数空");
        if (StringUtil.isNullOrEmpty(memberAction.getMember_card())) {
            //todo 生成code码
            memberService.saveMemberAction(memberAction);
        } else {
            memberService.updateMemberAction(memberAction);
        }
        return GeneralResult.success();
    }


    @ApiOperation("获取顾客行为偏好")
    @PostMapping("/v1/getMemberAction")
    public GeneralResult getMemberAction(
            @RequestParam("member_card")
            @NotBlank(message = "member_card不能为空")
            @ApiParam(name = "member_card", value = "会员卡号", required = true)
                    String member_card

    ) {
        MemberAction memberAction = memberService.getMemberAction(member_card);
        return GeneralResult.success(memberAction);
    }

}
