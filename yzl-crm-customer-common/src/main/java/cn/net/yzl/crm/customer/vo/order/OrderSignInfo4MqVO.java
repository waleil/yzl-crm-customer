package cn.net.yzl.crm.customer.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@ApiModel(value="订单信息",description="mq结构体" )
@Data
public class OrderSignInfo4MqVO {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "签收时间")
    private Date signTime;

    @ApiModelProperty(value = "坐席编号")
    private String staffNo;

    @ApiModelProperty(value = "顾客卡号")
    private String memberCardNo;
    @ApiModelProperty(value = "状态：1=签收，2=取消签收")
    private Integer status;

    @ApiModelProperty(value = "商品列表")
    private List<OrderProductVO> productList;

}
