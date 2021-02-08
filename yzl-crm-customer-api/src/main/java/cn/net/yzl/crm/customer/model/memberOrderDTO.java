package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class memberOrderDTO {
        @ApiModelProperty("订单编号")
        private String orderNo;
        @ApiModelProperty("支付方式")
        private Integer payType;
        @ApiModelProperty("支付形式")
        private Integer payMode;
        @ApiModelProperty("订单状态")
        private Integer orderStatus;
        @ApiModelProperty("活动编码")
        private Integer activityNo;
        @ApiModelProperty("活动名称")
        private String activityName;
        @ApiModelProperty("活动类型")
        private String activityType;
        @ApiModelProperty("媒介唯一标识")
        private Integer mediaNo;
        @ApiModelProperty("收款状态")
        private Integer payStatus;
        @ApiModelProperty("物流状态")
        private Integer logisticsStatus;
        @ApiModelProperty("物流公司编码")
        private String expressCompanyCode;
        @ApiModelProperty("订单总金额，单位分")
        private Integer totalOrder;
        @ApiModelProperty("订单总金额，单位元")
        private Double totalOrderD;


}
