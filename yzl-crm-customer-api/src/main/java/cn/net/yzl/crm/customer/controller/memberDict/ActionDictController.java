package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.service.memberDict.ActionDictService;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="ActionDictController",tags = {"客户综合行为"})
@RestController
@RequestMapping("/member/actionDict")
public class ActionDictController {

    @Autowired
    private ActionDictService actionDictService;

    @ApiOperation(value="字典管理-客户年龄段字典列表查询")
    @GetMapping("/getAgeDictList")
    public ComResponse<List<ActionDict>>  getListByType(Integer type){
       return actionDictService.getDictListByType(type);
    }

    @ApiOperation(value="字典管理-客户年龄段字典保存")
    @PostMapping("/memberAgeSaveUpdate")
    public ComResponse<Integer> memberAgeSaveUpdate(@RequestBody  List<ActionDictDto> ageDictDtos){
        return  actionDictService.saveUpdateActionDict(ageDictDtos);
    }



}
