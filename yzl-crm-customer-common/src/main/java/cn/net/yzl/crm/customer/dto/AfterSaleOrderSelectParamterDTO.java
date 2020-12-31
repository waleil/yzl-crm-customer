package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="售后订单查询参数类",description="参数类" )
@Data
public class AfterSaleOrderSelectParamterDTO implements Serializable {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;


    @ApiModelProperty(value = "退换货类型：0=退货，1=换货")
    private Byte type;

    @ApiModelProperty(value = "退款方式，0=快递代办，1=微信转账，2=支付宝转账，3=银行卡转账，4=退回账户余款")
    private Byte refundType;

    @ApiModelProperty(value = "顾客姓名")
    private String customerName;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
