package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选金额类型")
@Data
public class AmountParam {

    @ApiModelProperty("符号 >= > = <= <")
    private String symbol;

    @ApiModelProperty("金额")
    private Double amount;
}
