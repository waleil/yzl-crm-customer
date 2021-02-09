package cn.net.yzl.crm.customer.controller.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.service.memberDict.ActionDictService;
import cn.net.yzl.crm.customer.utils.ValidList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value="ActionDictController",tags = {"客户综合行为"})
@RestController
@RequestMapping("/member/memberAction")
public class ActionDictController {

    @Autowired
    private ActionDictService actionDictService;

    @ApiOperation(value="字典管理-客户综合行为字典列表查询",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("v1/getActionDictList")
    public ComResponse<List<ActionDict>>  getListByType(@RequestParam("type") @NotNull @Min(0) Integer type){
       return actionDictService.getDictListByType(type);
    }

    @ApiOperation(value="字典管理-客户综合行为字典保存",consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("v1/saveUpdateActionDict")
    public ComResponse<Integer> memberAgeSaveUpdate(@RequestBody @Validated ValidList<ActionDictDto> ageDictDtos){
        return  actionDictService.saveUpdateActionDict(ageDictDtos);
    }

    @ApiOperation(value="字典管理-定时器T+1同步行为偏好字典")
    @PostMapping("v1/syncActionDictTimer")
    public ComResponse<Integer> syncActionDictTimer(){
        return actionDictService.updateActionDictWhereStatusIs2();
    }






}
