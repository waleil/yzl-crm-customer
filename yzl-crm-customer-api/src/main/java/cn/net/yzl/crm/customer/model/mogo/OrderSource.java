package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderSource
 * @description 会员管理的订单来源
 * @date: 2021/1/25 1:37 下午
 */
@Data
public class OrderSource {
    //会员卡号
    private String memberCard;
    //订单来源
    private String source;
}
