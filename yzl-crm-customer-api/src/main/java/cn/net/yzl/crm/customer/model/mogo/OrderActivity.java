package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderActivity
 * @description 会员参与的活动
 * @date: 2021/1/25 1:38 下午
 */
@Data
public class OrderActivity {
    //会员卡号
    private String memberCard;
    //订单参与的活动
    private String activityCode;
}
