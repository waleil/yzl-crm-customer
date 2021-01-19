package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款")
@Data
public class crowd_pay_form {
    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
