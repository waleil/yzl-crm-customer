package cn.net.yzl.crm.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberProductEffectSelectVO",description = "顾客服用效果实体")
public class MemberProductEffectSelectVO implements Serializable {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard",required = true)
    @NotBlank
    private String memberCard;

    @ApiModelProperty(value = "订单号",name = "orderNo")
    @NotBlank
    private String orderNo;


    @ApiModelProperty(value = "商品编号",name="productCodes",required = true)
    private List<String> productCodes;


}