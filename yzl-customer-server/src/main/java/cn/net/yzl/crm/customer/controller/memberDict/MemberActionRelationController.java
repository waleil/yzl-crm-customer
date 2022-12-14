package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.service.memberDict.MemberActionRelationService;
import cn.net.yzl.crm.customer.utils.ValidList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelationList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionDictList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value="MemberActionRelationController",tags = {"顾客综合行为"})
@RestController
@RequestMapping("/member/memberAction")
public class MemberActionRelationController {

    @Autowired
    private MemberActionRelationService memberActionRelationService;

    @ApiOperation(value="客户行为关联-客户全综合行为关联查询",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("v1/getRelationByMemberCard")
    public ComResponse<List<MemberActionRelationList>> getRelationByMemberCard(@RequestParam("cardNo") @NotBlank  String cardNo){
        return  memberActionRelationService.selectRelationTreeByMemberCard(cardNo);
    }

    @ApiOperation(value="客户行为关联-根据顾客编号查询行为字典",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("v1/getActionDictByMemberCard")
    public ComResponse<List<MemberActionDictList>> getActionDictByMemberCard(@RequestParam("memberCard") @NotBlank  String memberCard){
        return  memberActionRelationService.getActionDictByMemberCard(memberCard);
    }

    @ApiOperation(value="客户行为关联-客户一类综合行为关联查询",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("v1/getRelationByMemberCardAndType")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCardAndType(@RequestParam("cardNo") @NotBlank String cardNo,@RequestParam("type") @NotNull @Min(0) Integer type){
        return  memberActionRelationService.selectRelationByMemberCardAndType(cardNo,type);
    }

    @ApiOperation(value="客户行为关联-客户全综合行为关联更改提交保存",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("v1/memberAgeRelationSaveUpdate")
    public ComResponse<Integer> saveUpdateRelation(@RequestBody @Validated ValidList<MemberActionRelationDto> memberAgeRelationDtos){
        return memberActionRelationService.saveUpdateRelation(memberAgeRelationDtos);
    }

    @ApiOperation(value="客户行为关联-客户全综合行为关联新增",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("v1/addRelation")
    public ComResponse<Integer> addRelation(@RequestBody @Validated MemberActionRelationDto memberAgeRelationDtos){
        return memberActionRelationService.addRelation(memberAgeRelationDtos);
    }

    @ApiOperation(value="客户行为关联-客户综合行为手动新增录入关联",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("v1/addRelationWithDict")
    public ComResponse<Integer> addRelationWithDict(@RequestBody @Validated MemberActionRelationDto memberActionRelationDto){
        return memberActionRelationService.addRelationWithDict(memberActionRelationDto);
    }


    @ApiOperation(value="客户行为关联-客户全综合行为关联删除",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("v1/deleteRelation")
    public ComResponse<Integer> deleteRelation(@RequestParam("rid") @NotNull @Min(0) Integer rid){
        return memberActionRelationService.deleteRelation(rid);
    }


}
