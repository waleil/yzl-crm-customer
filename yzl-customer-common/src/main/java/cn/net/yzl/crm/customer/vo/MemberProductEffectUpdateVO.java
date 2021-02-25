package cn.net.yzl.crm.customer.vo;

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
@ApiModel(value = "MemberProductEffectUpdateVO",description = "顾客服用效果实体")
public class MemberProductEffectUpdateVO implements Serializable {
    @ApiModelProperty(value = "id",name = "id",required = true)
    private Integer id;

    @ApiModelProperty(value = "顾客卡号",name = "memberCard",required = true)
    @NotBlank
    private String memberCard;

    @ApiModelProperty(value = "服用效果",name="takingEffect",required = true)
    private Integer takingEffect;

    @ApiModelProperty(value = "服用状态",name="takingState",required = true)
    private Integer takingState;

    @ApiModelProperty(value = "每天吃几次",name="oneToTimes",required = true)
    private Integer oneToTimes;

    @ApiModelProperty(value = "每次吃多少",name="oneUseNum",required = true)
    private Integer oneUseNum;

    @ApiModelProperty(value = "商品编号",name="productCode",required = false)
    private String productCode;

    @ApiModelProperty(value = "商品名称",name="productName",required = false)
    private String productName;

    @ApiModelProperty(value = "商品剩余量",name="productLastNum",required = false,hidden = true)
    private Integer productLastNum;

    @ApiModelProperty(value = "修改人编号",name="updator",required = false)
    private String updator;

//    @ApiModelProperty(value = "修改时间",name="upateTime")
//    private Date upateTime;

    @ApiModelProperty(value = "商品关联的最后一次签收订单编号",name="orderNo",required = false,hidden = true)
    private String orderNo;

    @ApiModelProperty(value = "购买商品数量",name="productCount",required = false,hidden = true)
    private Integer productCount;




}