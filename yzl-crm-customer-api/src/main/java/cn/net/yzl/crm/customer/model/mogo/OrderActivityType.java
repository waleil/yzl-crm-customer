package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ActivityType
 * @description 会员下单时参与活动的类型
 * @date: 2021/1/25 1:39 下午
 */
@Data
public class OrderActivityType {
    //会员卡号
    private String memberCard;
    //订单参与的活动类型，来自于订单
    private String activityType;
}
