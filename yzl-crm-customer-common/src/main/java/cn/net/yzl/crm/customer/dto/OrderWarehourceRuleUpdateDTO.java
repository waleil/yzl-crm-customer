package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value="物流规则分仓设置提交参数类",description="请求参数类" )
@Data
public class OrderWarehourceRuleUpdateDTO implements Serializable {
    @ApiModelProperty(value = "提交的覆盖区域列表")
    List<ProvenceDTO> provenceList;
}
