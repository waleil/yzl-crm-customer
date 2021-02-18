package cn.net.yzl.crm.customer.model;

import java.io.Serializable;
import lombok.Data;

/**
 * member_amount
 * @author 
 */
@Data
public class MemberAmount implements Serializable {
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
     * 余券
     */
    private Integer lastCoupon;

    /**
     * 总剩余金额
     */
    private Integer totalMoney;

    /**
     * 占用预存款
     */
    private Integer frozenAmount;

    /**
     * 占用返券
     */
    private Integer frozenTicket;

    /**
     * 占用积分
     */
    private Integer frozenIntegral;

    /**
     * 占用红包
     */
    private Integer frozenRedBag;

    private static final long serialVersionUID = 1L;
}