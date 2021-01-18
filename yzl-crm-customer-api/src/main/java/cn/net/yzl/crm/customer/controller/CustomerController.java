package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.DateHelper;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.*;
import cn.net.yzl.crm.customer.mongomodel.crowd_action;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
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

    @ApiOperation("添加收货地址")
    @PostMapping("/v1/addReveiverAddress")
    public GeneralResult addReveiverAddress(@RequestBody ReveiverAddress reveiverAddress) {
        if (reveiverAddress == null) return GeneralResult.errorWithMessage(101, "参数空");
        memberService.saveReveiverAddress(reveiverAddress);
        return GeneralResult.success();
    }

    @ApiOperation("修改收获地址")
    @PostMapping("/v1/updateReveiverAddress")
    public GeneralResult updateReveiverAddress(@RequestBody ReveiverAddress reveiverAddress) {
        if (reveiverAddress == null || StringUtil.isNullOrEmpty(reveiverAddress.getMember_card()))
            return GeneralResult.errorWithMessage(101, "参数空");
        memberService.updateReveiverAddress(reveiverAddress);

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

//    @ApiOperation("添加顾客行为偏好")
//    @PostMapping("/v1/addMemberAction")
//    public GeneralResult addMemberAction(@RequestBody MemberAction memberAction) {
//        if (memberAction == null) return GeneralResult.errorWithMessage(101, "参数空");
//        memberService.saveMemberAction(memberAction);
//        return GeneralResult.success();
//    }


//    @ApiOperation("修改顾客行为偏好")
//    @PostMapping("/v1/updateMemberAction")
//    public GeneralResult updateMemberAction(@RequestBody MemberAction memberAction) {
//        if (memberAction == null || StringUtil.isNullOrEmpty(memberAction.getMember_card()))
//            return GeneralResult.errorWithMessage(101, "参数空");
//        memberService.updateMemberAction(memberAction);
//        return GeneralResult.success();
//    }
//
//    @ApiOperation("获取顾客行为偏好")
//    @GetMapping("/v1/getMemberAction")
//    public GeneralResult getMemberAction(
//            @RequestParam("member_card")
//            @NotBlank(message = "member_card不能为空")
//            @ApiParam(name = "member_card", value = "会员卡号", required = true)
//                    String member_card
//    ) {
//        MemberAction memberAction = memberService.getMemberAction(member_card);
//        return GeneralResult.success(memberAction);
//    }

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

    @ApiOperation("根据一批顾客群组id获取群组信息,用英文逗号分隔")
    @GetMapping("/v1/getCrowdGroupList")
    public ComResponse getCrowdGroupList(
            @RequestParam("crowdGroupIds")
            @NotBlank(message = "crowdGroupIds不能为空")
            @ApiParam(name = "crowdGroupIds", value = "群组id", required = true)
                    String crowdGroupIds) {

        List<String> groupIds = Arrays.asList(crowdGroupIds.split(","));
        List<CrowdGroup> crowdGroupList = memberService.getCrowdGroupByIds(groupIds);
        return ComResponse.success(crowdGroupList);

    }

    @ApiOperation("添加顾客圈选")
    @PostMapping("/v1/addCrowdGroup")
    public ComResponse addCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup) {
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);

        CrowdGroup crowdGroup = new CrowdGroup();
//        crowdGroup.setCreate_code();
//        crowdGroup.setCreate_name();
        crowdGroup.setName(memberCrowdGroup.getCrowd_name());
        crowdGroup.setDescription(memberCrowdGroup.getDescription());
        crowdGroup.setEffective_date(DateHelper.formateDate(memberCrowdGroup.getEffective_date(), "yyyy-MM-dd HH:mm:ss"));
        crowdGroup.setExpire_date(DateHelper.formatDate(memberCrowdGroup.getExpire_date(),"yyyy-MM-dd HH:mm:ss"));
        crowdGroup.setEnable(memberCrowdGroup.getEnable());
        crowdGroup.setPerson_count(memberCrowdGroup.getPerson_count());
        int insertRows = memberService.addCrowdGroup(crowdGroup);

        memberCrowdGroup.setCreate_time(DateHelper.getCurrentDate());
        memberCrowdGroup.setCrowd_id(crowdGroup.getId()+"");
        memberService.saveMemberCrowdGroup(memberCrowdGroup);
        //todo 圈选人
        return ComResponse.success();
    }

    @ApiOperation("修改顾客圈选")
    @PostMapping("/v1/updateCrowdGroup")
    public ComResponse updateCrowdGroup(@RequestBody member_crowd_group memberCrowdGroup) {
        if (memberCrowdGroup == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        try {

            CrowdGroup crowdGroup = new CrowdGroup();
            crowdGroup.setId(Integer.parseInt(memberCrowdGroup.getCrowd_id()));
            crowdGroup.setUpdate_code(memberCrowdGroup.getUpdate_code());
            crowdGroup.setUpdate_name(memberCrowdGroup.getUpdate_name());
            crowdGroup.setName(memberCrowdGroup.getCrowd_name());
            crowdGroup.setDescription(memberCrowdGroup.getDescription());
            crowdGroup.setEffective_date(DateHelper.formateDate(memberCrowdGroup.getEffective_date(), "yyyy-MM-dd HH:mm:ss"));
            crowdGroup.setExpire_date(DateHelper.formatDate(memberCrowdGroup.getExpire_date(), "yyyy-MM-dd HH:mm:ss"));
            crowdGroup.setEnable(memberCrowdGroup.getEnable());
            crowdGroup.setPerson_count(memberCrowdGroup.getPerson_count());

            memberCrowdGroup.setUpdate_time(DateHelper.getCurrentDate());

            memberService.updateCrowdGroup(crowdGroup);
            memberService.updateMemberCrowdGroup(memberCrowdGroup);
            //todo 圈选人
            return ComResponse.success();
        } catch (Exception exc) {
            return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE);
        }
    }


    @ApiOperation("分页获取圈选列表")
    @PostMapping("/v1/getCrowdGroupByPage")
    public ComResponse getCrowdGroupByPage(@RequestBody CrowdGroupDTO crowdGroupDTO) {
        if (crowdGroupDTO == null) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        Page<CrowdGroup> page = memberService.getCrowdGroupByPage(crowdGroupDTO);
        return ComResponse.success(page);
    }

    @ApiOperation("根据id获取圈选")
    @GetMapping("/v1/getMemberCrowdGroup")
    public ComResponse getMemberCrowdGroup(
            @RequestParam("crowdId")
            @NotBlank(message = "crowdId不能为空")
            @ApiParam(name = "crowdId", value = "圈选id", required = true)
                    String crowdId) {
        if (StringUtil.isNullOrEmpty(crowdId)) throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        member_crowd_group member_crowd_group = memberService.getMemberCrowdGroup(crowdId);
        return ComResponse.success(member_crowd_group);
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
}
