package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.label.LabelDto;
import cn.net.yzl.crm.customer.model.LabelType;
import cn.net.yzl.crm.customer.service.LabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "标签管理")
@RestController
@RequestMapping(value = LabelController.PATH)
public class LabelController {
    public static final String PATH = "label";

    @Autowired
    private LabelService labelService;

    @ApiOperation(value="保存客户标签")
    @PostMapping("save")
    public GeneralResult save(@RequestBody LabelDto labelDto) {
        labelService.saveLavbel(labelDto);
        return GeneralResult.success();
    }

    @ApiOperation(value="获取所有客户标签")
    @GetMapping("getCustomerLabels")
    public GeneralResult<List<LabelDto>> getCustomerLabels() {
        List<LabelDto> customerLabels = labelService.getCustomerLabels();
        return GeneralResult.success(customerLabels);
    }

    @ApiOperation(value="获取标签类型接口")
    @GetMapping("getLabelTypes")
    public ComResponse getLabelTypes(){
        List<LabelType> labelTypeList=labelService.getLabelTypes();
        return new ComResponse().setData(labelTypeList).setCode(ComResponse.SUCCESS_STATUS);

    }
}
