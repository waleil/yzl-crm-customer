package cn.net.yzl.crm.customer.dto.amount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberAmountDto",description = "顾客账户信息实体")
public class MemberAmountDto implements Serializable {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "总剩余金额",name = "totalMoney")
    private Integer totalMoney;
    @ApiModelProperty(value = "冻结预存款",name = "frozenAmount")
    private Integer frozenAmount;

    @ApiModelProperty(value = "可用金额",name = "validAmount")
    private Integer validAmount;


}