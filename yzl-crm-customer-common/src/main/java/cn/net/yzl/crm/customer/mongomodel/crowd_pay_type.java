package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("支付方式  0=货到付款，1=款到发货，-1不做条件判断")
@Data
public class crowd_pay_type {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
