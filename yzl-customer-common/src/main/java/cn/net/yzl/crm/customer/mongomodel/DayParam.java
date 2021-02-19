package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选天类型")
@Data
public class DayParam {

    @ApiModelProperty("符号 >= > = <= <")
    private String symbol;

    @ApiModelProperty("天")
    private Integer day;
}
