package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("顾客订单数据实体类")
@Data
public class Member_Order {
    @ApiModelProperty("支付方式")
    private List<String> pay_type;

    @ApiModelProperty("支付形式")
    private List<String> pay_mode;

    @ApiModelProperty("距离今日的签收天数")
    private int to_today_count;

    @ApiModelProperty("订单状态")
    private String order_state;

    @ApiModelProperty("活动订单对应的活动id")
    private List<String> active_order;

    @ApiModelProperty("订单来源")
    private List<String> order_source;

    @ApiModelProperty("购买过的商品id")
    private List<String> product_no;

    @ApiModelProperty("支付状态")
    private List<String> pay_state;

    @ApiModelProperty("物流状态")
    private List<String> logistics_state;

    @ApiModelProperty("物流公司")
    private List<String> logistics_ids;
}
