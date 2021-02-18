package cn.net.yzl.crm.customer.collector.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: LogisticsStatus
 * @description 会员关联的物流状态
 * @date: 2021/1/25 2:35 下午
 */
@Data
public class LogisticsStatus {
    //会员卡号
    private String memberCard;
    //订单关联的物流状态
    private Integer status;
}
