package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderPayMode
 * @description 会员订单支付形式
 * @date: 2021/1/25 1:40 下午
 */
@Data
public class OrderPayMode {
    //会员编号
    private String memberCard;
    //支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款
    private Integer payMode;
}
