package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="订单返回值类",description="返回值类" )
@Data
public class OrderResDTO implements Serializable {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单渠道：0=OTC，1=电媒")
    private Byte orderChannel;

    @ApiModelProperty(value = "订单商品关联表id")
    private String orderProductId;

    @ApiModelProperty(value = "订单性质：1=正常订单，0=免审订单")
    private Byte orderNature;

    @ApiModelProperty(value = "订单状态：0=话务待审核，1=话务未通过，2=待发货，3=已发货，4=已签收")
    private Byte orderStatus;

    @ApiModelProperty(value = "商品总金额 单位分")
    private Integer amountProduct;

    @ApiModelProperty(value = "配送费用 单位分")
    private Integer amountDelivery;

    @ApiModelProperty(value = "红包金额 单位分")
    private Integer amountRedEnvelope;

    @ApiModelProperty(value = "应收金额 单位分")
    private Integer amountReceivable;

    @ApiModelProperty(value = "实收金额 单位分")
    private Integer amountActual;

    @ApiModelProperty(value = "优惠券 单位分")
    private Integer amountCoupon;

    @ApiModelProperty(value = "顾客表id")
    private Integer customerId;

    @ApiModelProperty(value = "顾客卡号")
    private Integer customerCardNo;

    @ApiModelProperty(value = "顾客表姓名")
    private Integer customerName;

    @ApiModelProperty(value = "积分抵扣 单位分")
    private Integer pointsDeduction;

    @ApiModelProperty(value = "是否开票：0=否，1=是")
    private Byte invoice;

    @ApiModelProperty(value = "发票抬头")
    private String invoiceTitle;

    @ApiModelProperty(value = "付款方式：1=货到付款")
    private Byte payType;

    @ApiModelProperty(value = "审核意见")
    private String checkOpinion;

    @ApiModelProperty(value = "订单备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人编号")
    private String updateCode;
}
