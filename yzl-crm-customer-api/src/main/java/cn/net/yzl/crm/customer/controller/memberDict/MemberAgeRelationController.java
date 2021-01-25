package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.GeneralResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value="CustomerController",tags = {"顾客条件字典关联关联"})
@RestController
@RequestMapping("/memberAgeRelation")
public class MemberAgeRelationController {

//    @Autowired
//    private MemberRelationService memberRelationService;
//
//    @ApiOperation(value="客户年龄段关联查询")
//    @PostMapping("/getAgeRelationByMemberCard")
//    public GeneralResult getAgeRelationByMemberCard(String cardNo){
//        List<MemberAgeRelation> memberAgeRelations = memberRelationService.selectAgeRelationByMemberCard(cardNo);
//        return GeneralResult.success(memberAgeRelations);
//    }
//
//    @ApiOperation(value="客户年龄段关联保存")
//    @PostMapping("/memberAgeRelationSaveUpdate")
//    public GeneralResult memberAgeRelationSaveUpdate(@RequestBody List<MemberAgeRelationDto> memberAgeRelationDtos){
//        int num= memberRelationService.memberAgeRelationSaveUpdate(memberAgeRelationDtos);
//        return GeneralResult.success(num);
//    }
//
//    @ApiOperation(value="客户联系时间段关联查询")
//    @PostMapping("/getContactTimeRelationByMemberCard")
//    public GeneralResult getContactTimeRelationByMemberCard(String cardNo){
//        List<MemberContactTimeRelation> memberContactTimeRelations = memberRelationService.selectContactTimeRelationByMemberCard(cardNo);
//        return GeneralResult.success(memberContactTimeRelations);
//    }
//
//    @ApiOperation(value="客户联系时间段关联保存")
//    @PostMapping("/memberContactTimeRelationSaveUpdate")
//    public GeneralResult memberContactTimeRelationSaveUpdate(@RequestBody List<MemberContactTimeRelationDto> memberContactTimeRelationDtos){
//        int num= memberRelationService.memberContactTimeRelationSaveUpdate(memberContactTimeRelationDtos);
//        return GeneralResult.success(num);
//    }
//


}
