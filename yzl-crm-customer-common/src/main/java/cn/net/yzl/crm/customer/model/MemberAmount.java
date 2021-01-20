package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客账户实体")
@Data
public class MemberAmount {

    @ApiModelProperty("顾客卡号")
    private String member_card;

    @ApiModelProperty("余积分")
    private int last_integral;

    @ApiModelProperty("余红包")
    private int last_red_bag;

    @ApiModelProperty("余券")
    private int last_coupon;

    @ApiModelProperty("总剩余金额")
    private int total_money;

    @ApiModelProperty("占用预存款")
    private int frozen_amount;

    @ApiModelProperty("占用返券")
    private int frozen_ticket;

    @ApiModelProperty("占用红包")
    private int frozen_red_bag;

    @ApiModelProperty("占用积分")
    private int frozen_integral;
}
