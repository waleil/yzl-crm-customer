package cn.net.yzl.crm.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberAmountDetailVO",description = "顾客账户信息记录实体")
public class MemberAmountDetailVO implements Serializable {

    @ApiModelProperty(value = "顾客卡号", name = "memberCard", required = true)
    @NotBlank
    private String memberCard;
    @ApiModelProperty(value = "金额", name = "discountMoney", required = true)
    @NotNull
    @Min(1)
    private Integer discountMoney;
    @ApiModelProperty(value = "1 退回 2 消费,3:充值", name = "obtainType", required = true)
    @NotNull
    @Min(1)
    @Max(3)
    private int obtainType;
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    @ApiModelProperty(value = "订单号(操作类型为1或者2时,orderNo必传)", name = "orderNo")
    private String orderNo;

//    @ApiModelProperty(value = "操作类型:1:先冻结,需要手动确认  2:一步完成", name = "operateType")
//    private Integer operateType;
}