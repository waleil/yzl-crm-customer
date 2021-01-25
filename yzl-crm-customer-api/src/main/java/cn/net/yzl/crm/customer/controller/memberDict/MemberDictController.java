package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.GeneralResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="MemberDictController",tags = {"顾客条件字典管理"})
@RestController
@RequestMapping("/memberDict")
public class MemberDictController {
//
//    @Autowired
//    private MemberDictService memberDictService;
//
//    @ApiOperation(value="客户年龄段字典列表查询")
//    @GetMapping("/getAgeDictList")
//    public List<AgeDict> selectAll(){
//       return memberDictService.getAgeDictList();
//    }
//
//    @ApiOperation(value="客户年龄段字典保存")
//    @PostMapping("/memberAgeSaveUpdate")
//    public GeneralResult memberAgeSaveUpdate(@RequestBody  List<AgeDictDto> ageDictDtos){
//       int num= memberDictService.memberAgeSaveUpdate(ageDictDtos);
//        return GeneralResult.success(num);
//    }
//
//    @ApiOperation(value="客户联系时间段列表查询")
//    @GetMapping("/getContactTimeDictList")
//    public List<ContactTimeDict> getContactTimeDictList(){
//        return memberDictService.getContactTimeDictList();
//    }
//
//    @ApiOperation(value="客户联系时间段字典保存")
//    @PostMapping("/memberContactTimeSaveUpdate")
//    public GeneralResult memberContactTimeSaveUpdate(@RequestBody List<ContactTimeDictDto> contactTimeDictDtos){
//        int num= memberDictService.memberContactTimeSaveUpdate(contactTimeDictDtos);
//        return GeneralResult.success(num);
//    }


}
