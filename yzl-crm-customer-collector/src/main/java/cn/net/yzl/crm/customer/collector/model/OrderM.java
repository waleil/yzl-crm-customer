package cn.net.yzl.crm.customer.collector.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * order_m
 * @author 
 */
@Data
public class OrderM implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单坐席所属部门
     */
    private Integer departId;

    /**
     * 工单号唯一标识
     */
    private Long workOrderNo;

    /**
     * 工单流水号（工单号下会有多个流水号，流水号可能对应多个个订单号）
     */
    private String workBatchNo;

    /**
     * 媒介类型 数据来源dmc
     */
    private Integer mediaType;

    /**
     * 媒介唯一标识
     */
    private Long mediaNo;

    /**
     * 媒介名称
     */
    private String mediaName;

    /**
     * 媒介渠道：0=电销事业中心，1=OTC ，2=淘宝 ，3=京东 ，4=自建app
     */
    private Byte mediaChannel;

    /**
     * 订单活动唯一标识
     */
    private Long activityNo;

    /**
     * 订单活动名称
     */
    private String activityName;

    /**
     * 广告id
     */
    private Long advisorNo;

    /**
     * 广告名称
     */
    private String advisorName;

    /**
     * 订单性质：1=非免审订单，0=免审订单
     */
    private Byte orderNature;

    /**
     * 订单状态：1:待审核,2:审核未通过,3:待发货,4:已发货,5:已签收,6:拒收退货中,8:已取消,11:退款完成,12:部分退款完成,13:签收退货中,14:部分签收退货中,17:拒收已入库,18:签收已入库,19:部分签收已入库,20:仅退款
     */
    private Byte orderStatus;

    /**
     * 物流状态：0.待发货 1.部分发货 2.已发货  3.部分揽件 4.已揽件 5.运途中 6.部分签收 7.已签收 8.部分拒签 9.全部拒签 10.问题件
     */
    private Byte logisticsStatus;

    /**
     * 是否是首单产品 1:首单 0:非首单
     */
    private Byte firstOrderFlag;

    /**
     * 实收金额（应收金额+预存 一期预存均为0）
     */
    private Integer total;

    /**
     * 订单总额
     */
    private Integer totalAll;

    /**
     * 邮费 分为单位
     */
    private Integer pfee;

    /**
     * 邮费承担方 0=御芝林 1=顾客
     */
    private Byte pfeeFlag;

    /**
     * 应收金额(=订单总额+邮费-优惠) 分为单位
     */
    private Integer cash;

    /**
     * 预存金额 分为单位
     */
    private Integer cash1;

    /**
     * 消费金额(订单总额-优惠) 分为单位
     */
    private Integer spend;

    /**
     * 使用储值金额 单位分
     */
    private Integer amountStored;

    /**
     * 使用红包金额 单位分
     */
    private Integer amountRedEnvelope;

    /**
     * 使用优惠券 单位分
     */
    private Integer amountCoupon;

    /**
     * 使用积分抵扣 单位分
     */
    private Integer pointsDeduction;

    /**
     * 返回红包金额 单位分
     */
    private Integer returnAmountRedEnvelope;

    /**
     * 返回优惠券 单位分
     */
    private Integer returnAmountCoupon;

    /**
     * 返回积分 单位分
     */
    private Integer returnPointsDeduction;

    /**
     * 顾客姓名
     */
    private String memberName;

    /**
     * 顾客电话
     */
    private String memberTelphoneNo;

    /**
     * 顾客卡号
     */
    private String memberCardNo;

    /**
     * 是否开票：0=否，1=是
     */
    private Byte invoiceFlag;

    /**
     * 支付方式：0=货到付款，1=款到发货 
     */
    private Byte payType;

    /**
     * 支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款
     */
    private Byte payMode;

    /**
     * 收款状态 1：是 0：否
     */
    private Byte payStatus;

    /**
     * 配送方式 0：快递 1：自提
     */
    private Byte distrubutionMode;

    /**
     * 是否预警 1=是 2=否
     */
    private Byte wangingFlag;

    /**
     * 是否指定物流公司 1=是 0=否
     */
    private Byte expressCompanyFlag;

    /**
     * 物流公司code
     */
    private String expressCompanyCode;

    /**
     * 物流公司名称
     */
    private String expressCompanyName;

    /**
     * 快递单号
     */
    private String expressNumber;

    /**
     * 关联采购单号
     */
    private String relationOrder;

    /**
     * 配送地址唯一标识
     */
    private Long reveiverAddressNo;

    /**
     * 省code
     */
    private String reveiverProvince;

    /**
     * 省名称
     */
    private String reveiverProvinceName;

    /**
     * 市code
     */
    private String reveiverCity;

    /**
     * 市名称
     */
    private String reveiverCityName;

    /**
     * 区code
     */
    private String reveiverArea;

    /**
     * 区名称
     */
    private String reveiverAreaName;

    /**
     * 收货人姓名
     */
    private String reveiverName;

    /**
     * 收货人电话
     */
    private String reveiverTelphoneNo;

    /**
     * 收货人地址
     */
    private String reveiverAddress;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 下单人坐席
     */
    private String staffCode;

    /**
     * 下单人姓名
     */
    private String staffName;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 更新人编号
     */
    private String updateCode;

    /**
     * 更新人姓名
     */
    private String updateName;

    /**
     * 发货时间
     */
    private Date shippingTime;

    /**
     * 预计到货时间
     */
    private Date estimateArrivalTime;

    /**
     * 签收时间
     */
    private Date signTime;

    /**
     * 单前顾客级别
     */
    private String memberLevelBefor;

    /**
     * 单前顾客类型
     */
    private String memberTypeBefor;

    /**
     * 单后顾客级别
     */
    private String memberLevelAfter;

    /**
     * 单后顾客类型
     */
    private String memberTypeAfter;

    /**
     * 下单人财务归属
     */
    private Long financialOwner;

    /**
     * 下单人财务归属名称
     */
    private String financialOwnerName;

    /**
     * 关联问题件补发订单编号
     */
    private String relationReissueOrderNo;

    /**
     * 物流赔付金额
     */
    private Integer logisticsClaims;

    /**
     * 物流赔付的商品金额 实际商品金额
     */
    private Integer relationReissueOrderTotal;

    /**
     * 是否历史数据 0否 1是  默认为0
     */
    private Integer isHistory;

    /**
     * 单后可用积分
     */
    private Integer orderAfterIntegral;

    /**
     * 单后可用返券
     */
    private Integer orderAfterRebate;

    /**
     * 单后可用红包
     */
    private Integer orderAfterRed;

    /**
     * 单后可用余额
     */
    private Integer orderAfterSpare;

    private static final long serialVersionUID = 1L;
}