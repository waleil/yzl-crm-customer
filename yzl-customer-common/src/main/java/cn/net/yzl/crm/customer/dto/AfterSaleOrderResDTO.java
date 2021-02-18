package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="售后订单返回值类",description="返回值类" )
@Data
public class AfterSaleOrderResDTO {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "退换货类型：0=退货，1=换货")
    private Byte type;

    @ApiModelProperty(value = "售后单号")
    private Integer afterSaleOrderNo;

    @ApiModelProperty(value = "订单表唯一标识")
    private String orderId;

    @ApiModelProperty(value = "订单号")
    private Integer orderNo;

    @ApiModelProperty(value = "返货方式，0=快递，1=自送")
    private Byte returnGoodsType;

    @ApiModelProperty(value = "快递公司id")
    private String expId;

    @ApiModelProperty(value = "快递单号")
    private String waybill;

    @ApiModelProperty(value = "退款方式，0=快递代办，1=微信转账，2=支付宝转账，3=银行卡转账，4=退回账户余款")
    private Byte refundType;

    @ApiModelProperty(value = "收款人id")
    private String refundPerson;

    @ApiModelProperty(value = "收款人姓名")
    private String refundPersonName;

    @ApiModelProperty(value = "收款人账户")
    private String refundAccount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人编号")
    private String updateCode;
}