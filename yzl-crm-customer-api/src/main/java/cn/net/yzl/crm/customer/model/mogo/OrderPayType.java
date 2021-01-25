package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderPayType
 * @description 会员订单支付方式
 * @date: 2021/1/25 1:40 下午
 */
@Data
public class OrderPayType {
    //会员编号
    private String memberCard;
    //支付方式：0=货到付款，1=款到发货
    private Integer payType;

}
