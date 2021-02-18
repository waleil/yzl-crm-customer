package cn.net.yzl.crm.customer.model.mogo;

import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: LogisticsCompany
 * @description todo
 * @date: 2021/1/25 2:36 下午
 */
@Data
public class LogisticsCompany {
    //会员卡号
    private String memberCard;
    //订单关联物流单所属公司编号
    private String companyCode;
}
