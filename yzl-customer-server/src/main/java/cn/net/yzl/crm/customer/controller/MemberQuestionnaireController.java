package cn.net.yzl.crm.customer.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.MemberQuwarionnireDTO;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaire;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaireDTO;
import cn.net.yzl.crm.customer.service.MemberQuestionnaireService;
import cn.net.yzl.crm.customer.sys.BizException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/memberQuestionnaire")
@Api(value = "MemberQuestionnaireController", tags = {"顾客调查问卷"})
@Validated
public class MemberQuestionnaireController {

    @Autowired
    private MemberQuestionnaireService memberQuestionnaireService;

    @ApiOperation(value = "保存调查问卷", notes = "保存调查问卷")
    @RequestMapping(value = "v1/saveQuestionnaire", method = RequestMethod.POST)
    public ComResponse<Boolean> saveQuestionnaire(@RequestBody @Validated MemberQuestionnaire memberQuestionnaire) throws IllegalAccessException {
        if (memberQuestionnaire == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        Boolean result = memberQuestionnaireService.saveQuestionnaire(memberQuestionnaire);

//        if (!result) {
//
//        }
        return ComResponse.success(Boolean.TRUE);
    }

    @ApiOperation(value = "批量保存调查问卷", notes = "批量保存调查问卷")
    @RequestMapping(value = "v1/batchSaveQuestionnaire", method = RequestMethod.POST)
    public ComResponse<Boolean> batchSaveQuestionnaire(@RequestBody @Validated List<MemberQuestionnaire> memberQuestionnaires) throws IllegalAccessException {
        if (CollectionUtil.isEmpty(memberQuestionnaires)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        for (MemberQuestionnaire questionnaire : memberQuestionnaires) {
            if (StringUtils.isEmpty(questionnaire.getMemberCard())) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "顾客卡号不能为空!", false);
            }
            if (StringUtils.isEmpty(questionnaire.getSeqNo())) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "问卷序列号不能为空!", false);
            }
        }
        //保存调查问卷列表
        Boolean result = memberQuestionnaireService.saveQuestionnaireList(memberQuestionnaires);

        if (!result) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        return ComResponse.success(Boolean.TRUE);
    }



    @ApiOperation("分页查询顾客调查问卷列表")
    @PostMapping("/v1/getMemberQuestionnaireByPage")
    public ComResponse<Page<MemberQuestionnaireDTO>> getMemberQuestionnaireByPage(@RequestBody @Validated MemberQuwarionnireDTO searchDTO) {
        if (searchDTO == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        Page<MemberQuestionnaireDTO> page = memberQuestionnaireService.getQuestionnaireByPage(searchDTO);
        return ComResponse.success(page);
    }

    @ApiOperation("根据id查询顾客调查问卷")
    @GetMapping("/v1/getMemberQuestionnaireById")
    public ComResponse<MemberQuestionnaire> getMemberQuestionnaireById(
            @RequestParam("primaryKey")
            @NotBlank(message = "primaryKey不能为空")
            @ApiParam(name = "primaryKey", value = "问卷记录id", required = true) String primaryKey) {
        if (StringUtil.isNullOrEmpty(primaryKey)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        MemberQuestionnaire memberQuestionnaire = memberQuestionnaireService.getMemberQuestionnaireById(primaryKey);
        return ComResponse.success(memberQuestionnaire);
    }

    @ApiOperation("根据顾客卡号查询顾客调查问卷")
    @GetMapping("/v1/getMemberQuestionnaireByMemberCard")
    public ComResponse<List<MemberQuestionnaire>> getMemberQuestionnaireByMemberCard(
            @RequestParam("memberCard")
            @NotBlank(message = "memberCard不能为空")
            @ApiParam(name = "memberCard", value = "顾客卡号", required = true) String memberCard) {
        if (StringUtil.isNullOrEmpty(memberCard)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        List<MemberQuestionnaire> memberQuestionnaireList = memberQuestionnaireService.getMemberQuestionnaireByMemberCard(memberCard);
        return ComResponse.success(memberQuestionnaireList);
    }


}
