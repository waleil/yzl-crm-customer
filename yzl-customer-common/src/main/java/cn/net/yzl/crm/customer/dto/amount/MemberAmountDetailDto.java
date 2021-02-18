package cn.net.yzl.crm.customer.dto.amount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberAmountDetailDto",description = "顾客账户信息记录实体")
public class MemberAmountDetailDto implements Serializable {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;

    @ApiModelProperty(value = "创建时间",name = "createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "金额",name = "discountMoney")
    private Integer discountMoney;
    @ApiModelProperty(value = "1 退回 2 消费,3:充值",name = "obtainType")
    private Byte obtainType;
    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;
}