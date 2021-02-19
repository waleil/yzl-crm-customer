package cn.net.yzl.crm.customer.collector.model;

import java.io.Serializable;
import lombok.Data;

/**
 * member_amount_redbag_integral
 * @author 
 */
@Data
public class MemberAmountRedbagIntegral implements Serializable {
    private Integer id;

    /**
     * 会员卡号
     */
    private String memberCard;

    /**
     * 余积分
     */
    private Integer lastIntegral;

    /**
     * 余红包
     */
    private Integer lastRedBag;

    /**
     * 是否历史数据 0：否 1：是
     */
    private Boolean isHistory;

    private static final long serialVersionUID = 1L;
}