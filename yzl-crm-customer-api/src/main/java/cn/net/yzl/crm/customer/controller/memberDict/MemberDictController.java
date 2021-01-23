package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.member.AgeDictDto;
import cn.net.yzl.crm.customer.dto.member.ContactTimeDictDto;
import cn.net.yzl.crm.customer.service.memberDict.MemberDictService;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.MemberContactTimeRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value="MemberDictController",tags = {"顾客条件字典管理"})
@RestController
@RequestMapping("/memberDict")
public class MemberDictController {

    @Autowired
    private MemberDictService memberDictService;

    @ApiOperation(value="客户年龄段字典保存")
    @PostMapping("/memberAgeSaveUpdate")
    public GeneralResult memberAgeSaveUpdate(@RequestBody  List<AgeDictDto> ageDictDtos){
       int num= memberDictService.memberAgeSaveUpdate(ageDictDtos);
        return GeneralResult.success(num);
    }


    @ApiOperation(value="客户联系时间段字典保存")
    @PostMapping("/memberContactTimeSaveUpdate")
    public GeneralResult memberContactTimeSaveUpdate(@RequestBody List<ContactTimeDictDto> contactTimeDictDtos){
        int num= memberDictService.memberContactTimeSaveUpdate(contactTimeDictDtos);
        return GeneralResult.success(num);
    }


}
