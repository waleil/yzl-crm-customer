package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: PayStatus
 * @description 会员支付状态，从订单的支付状态获取
 * @date: 2021/1/25 2:36 下午
 */
@Data
public class PayStatus {
    //会员卡号
    private String memberCard;
    //物流状态
    private Integer status;
}
