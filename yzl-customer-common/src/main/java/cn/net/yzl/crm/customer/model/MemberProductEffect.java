package cn.net.yzl.crm.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("顾客购买商品效果")
@Data
public class MemberProductEffect {

    private Integer id;

    @ApiModelProperty("会员卡号")
    private String member_card;

    @ApiModelProperty("订单号")
    private String order_no;

    @ApiModelProperty("商品编号")
    private String product_code;

    @ApiModelProperty("商品名称")
    private String product_name;

    @ApiModelProperty("每天吃多少")
    private int eating_perday;

    @ApiModelProperty("每次吃多少")
    private int one_use_num;

    @ApiModelProperty("商品剩余量")
    private int product_last_num;

    @ApiModelProperty("商品服用完日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date due_date;
    @ApiModelProperty("距离回访的天数")
    private int dueDateDays;
    @ApiModelProperty("服用状态:1 正常，2非常好，3 已停服")
    private Integer taking_state;

    @ApiModelProperty("服用效果：1 好，2一般，3 没有效果")
    private Integer taking_effect;

    @ApiModelProperty("购买商品数量")
    private Integer product_count;



}
