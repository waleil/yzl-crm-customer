package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * member_amount
 * @author 
 */
@Data
@ApiModel(value = "MemberProductEffectVO",description = "顾客服用效果实体")
public class MemberProductEffectDTO implements Serializable {
    @ApiModelProperty(value = "id",name = "id",required = true)
    private Integer id;

    @ApiModelProperty(value = "顾客卡号",name = "memberCard",required = true)
    @NotBlank
    private String memberCard;

    @ApiModelProperty(value = "订单号",name="orderNo",required = true)
    private String orderNo;

    @ApiModelProperty(value = "服用效果",name="takingEffect",required = true)
    private Integer takingEffect;

    @ApiModelProperty(value = "服用状态",name="takingState",required = true)
    private Integer takingState;

    @ApiModelProperty(value = "每天吃多少",name="eatingTime",required = true)
    private Integer eatingTime;

    @ApiModelProperty(value = "每天吃几次",name="oneToTimes",required = true)
    private Integer oneToTimes;

    @ApiModelProperty(value = "每次吃多少",name="oneUseNum",required = true)
    private Integer oneUseNum;

    @ApiModelProperty(value = "商品服用完日期",name="oneUseNum")
    private Date dueDate;

    @ApiModelProperty(value = "商品编号",name="productCode",required = true)
    private String productCode;

    @ApiModelProperty(value = "商品名称",name="productName",required = true)
    private String productName;

    @ApiModelProperty(value = "商品剩余量",name="productLastNum",required = true)
    private Integer productLastNum;

    @ApiModelProperty(value = "商品购买数量",name="productCount")
    private Integer productCount;

//    @ApiModelProperty(value = "修改人编号",name="updator",required = true)
//    private String updator;
//
//    @ApiModelProperty(value = "修改时间",name="upateTime")
//    private Date upateTime;





}