package cn.net.yzl.crm.customer.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "坐席编号")
    private String staffNo;

    @ApiModelProperty(value = "顾客卡号")
    private String memberCardNo;
    @ApiModelProperty(value = "状态：1=签收，2=取消签收")
    private Integer status;

    @ApiModelProperty(value = "订单总金额")
    private Integer totalAll;

    @ApiModelProperty(value = "应收金额(=订单总额+邮费-优惠) 分为单位")
    private Integer cash;

    @ApiModelProperty(value = "预存金额 分为单位")
    private Integer cash1;

    @ApiModelProperty(value = "消费金额(订单总额-优惠) 分为单位")
    private Integer spend;

    @ApiModelProperty(value = "商品列表")
    private List<OrderProductVO> productList;


//    @ApiModelProperty(value = "下单时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date createTime;


}
