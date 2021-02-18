package cn.net.yzl.crm.customer.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
//应收：receivable、应付：payable、实收：receipts、实付：payment

@ApiModel(value="下单vo类",description="下单vo类" )
@Data
public class OrderCreateInfoVO {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "顾客卡号")
    private String memberCard;

    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "下单坐席编号")
    private String staffNo;

//    @ApiModelProperty(value = "订单商品列表")
//    private List<OrderProductVO> productList;

}
