package cn.net.yzl.crm.customer.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * member_amount_detail
 * @author 
 */
@Data
public class MemberAmountDetail implements Serializable {
    /**
     * 1
     */
    private Integer id;

    /**
     * 顾客卡号
     */
    private String memberCard;

    /**
     * 红包，优惠，积分名称
     */
    private String discountName;

    /**
     * 金额
     */
    private Integer discountMoney;

    /**
     * 红包/优惠券/积分 编码
     */
    private String discountNo;

    /**
     * 有效开始时间
     */
    private Date startDate;

    /**
     * 有效截止时间
     */
    private Date endDate;

    /**
     * 1 红包，2积分，3 优惠券
     */
    private Byte discountType;

    /**
     * 1 转入 2 转出,3:充值
     */
    private Byte obtainType;

    /**
     * 从哪个订单得到的红包积分，优惠券
     */
    private String orderNo;

    /**
     * 关联的商品
     */
    private String productNo;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 1 正常 2 无效
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作类型:1:先冻结,需要手动确认  2:一步完成", name = "operateType")
    private Integer operateType;
}