package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.service.memberDict.MemberActionRelationService;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value="MemberActionRelationController",tags = {"客户综合行为"})
@RestController
@RequestMapping("/member/relation")
public class MemberActionRelationController {

    @Autowired
    private MemberActionRelationService memberActionRelationService;

    @ApiOperation(value="客户行为关联-客户全综合行为关联查询")
    @PostMapping("/getRelationByMemberCard")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCard(String cardNo){
        return  memberActionRelationService.selectRelationByMemberCard(cardNo);
    }

    @ApiOperation(value="客户行为关联-客户一类综合行为关联查询")
    @PostMapping("/getRelationByMemberCardAndType")
    public ComResponse<List<MemberActionRelation>> getRelationByMemberCardAndType(String cardNo,Integer type){
        return  memberActionRelationService.selectRelationByMemberCardAndType(cardNo,type);
    }

    @ApiOperation(value="客户行为关联-客户全综合行为关联更改提交保存")
    @PostMapping("/memberAgeRelationSaveUpdate")
    public ComResponse<Integer> saveUpdateRelation(@RequestBody List<MemberActionRelationDto> memberAgeRelationDtos){
        return memberActionRelationService.saveUpdateRelation(memberAgeRelationDtos);
    }

    @ApiOperation(value="客户行为关联-客户全综合行为关联新增")
    @PostMapping("/addRelation")
    public ComResponse<Integer> addRelation(@RequestBody MemberActionRelationDto memberAgeRelationDtos){
        return memberActionRelationService.addRelation(memberAgeRelationDtos);
    }

    @ApiOperation(value="客户行为关联-客户全综合行为关联删除")
    @PostMapping("/deleteRelation")
    public ComResponse<Integer> deleteRelation(Integer rid){
        return memberActionRelationService.deleteRelation(rid);
    }


}
