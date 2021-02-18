package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客订单统计")
@Data
public class MemberOrderStat {

    private  int id;

    @ApiModelProperty("顾客卡号")
    private  String member_card;

    @ApiModelProperty("累计消费金额")
    private int total_counsum_amount;

    @ApiModelProperty("累计充值金额")
    private int total_invest_amount;

    @ApiModelProperty("首单下单时间")
    private String first_order_time;

    @ApiModelProperty("最后一次下单时间")
    private String last_order_time;

    @ApiModelProperty("最后一次购买商品")
    private String last_buy_product_code;
    @ApiModelProperty("最后一次购买商品名称")
    private String lastBuyProductNames;
    @ApiModelProperty("首单下单员工")
    private String first_order_staff_no;

    @ApiModelProperty("首单订单编号")
    private String first_order_no;

    @ApiModelProperty("首次购买商品")
    private String first_buy_product_code;
    @ApiModelProperty("首次购买商品名称")
    private String firstBuyProductNames;
    @ApiModelProperty("首单金额")
    private int first_order_am;

    @ApiModelProperty("订单最高金额")
    private int order_high_am;
    @ApiModelProperty("订单最低金额")
    private int order_low_am;
    @ApiModelProperty("订单平均金额")
    private int order_avg_am;

    @ApiModelProperty("购买产品种类个数")
    private int product_type_cnt;

    @ApiModelProperty("累计购买次数")
    private int buy_count;
    @ApiModelProperty("总平均购买天数")
    private int day_avg_count;
    @ApiModelProperty("年度平均购买天数")
    private int year_avg_count;
    @ApiModelProperty("退货率")
    private String return_goods_rate;
}
