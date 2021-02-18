package cn.net.yzl.crm.customer.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="订单商品信息",description="mq结构体" )
@Data
public class OrderProductVO {


    @ApiModelProperty(value = "商品编码")
    private String productCode;

    @ApiModelProperty(value = "数量")
    private Integer productCount;

}