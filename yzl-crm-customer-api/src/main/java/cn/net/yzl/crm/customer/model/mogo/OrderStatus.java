package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderStatus
 * @description 会员的订单状态
 * @date: 2021/1/25 1:38 下午
 */
@Data
public class OrderStatus {
    //会员卡号
    private String memberCard;
    //订单状态，来自于订单
    private Integer status;
}
