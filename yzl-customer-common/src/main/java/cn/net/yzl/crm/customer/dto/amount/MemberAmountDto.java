package cn.net.yzl.crm.customer.dto.amount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberAmountDto",description = "顾客账户信息实体")
public class MemberAmountDto implements Serializable {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "总剩余金额(分)",name = "totalMoney")
    private Integer totalMoney;
    @ApiModelProperty(value = "冻结预存款(分)",name = "frozenAmount")
    private Integer frozenAmount;

    @ApiModelProperty(value = "可用金额(分)",name = "validAmount")
    private Integer validAmount;


    @ApiModelProperty(value = "总剩余金额(元)",name = "totalMoneyD")
    private BigDecimal totalMoneyD;
    @ApiModelProperty(value = "冻结预存款(元)",name = "frozenAmountD")
    private BigDecimal frozenAmountD;
    @ApiModelProperty(value = "可用金额(元)",name = "validAmountD")
    private BigDecimal validAmountD;


}