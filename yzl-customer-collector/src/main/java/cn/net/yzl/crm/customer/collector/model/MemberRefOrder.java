package cn.net.yzl.crm.customer.collector.model;

import cn.net.yzl.crm.customer.BaseObject;
import lombok.Data;

import java.util.Date;

/**
 * @author lichanghong
 * @version 1.0
 * @title: 用户相关订单
 * @description
 * @date: 2021/1/29 8:14 下午
 */
@Data
public class MemberRefOrder extends BaseObject {

    /**
     * 顾客卡号
     */
    private String memberCard;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 媒介类型 数据来源dmc
     */
    private Integer mediaType;
    /**
     * 媒介唯一标识
     */
    private Long mediaNo;

    /**
     * 媒介渠道：0=电销事业中心，1=OTC ，2=淘宝 ，3=京东 ，4=自建app
     */
    private Integer mediaChannel;

    /**
     * 订单活动唯一标识
     */
    private Long activityNo;

    /**
     * 广告id
     */
    private Long advisorNo;

    /**
     * 订单状态：1:待审核,2:审核未通过,3:待发货,4:已发货,5:已签收,6:拒收退货中,8:已取消,11:退款完成,12:部分退款完成,13:签收退货中,14:部分签收退货中,17:拒收已入库,18:签收已入库,19:部分签收已入库,20:仅退款
     */
    private Integer orderStatus;

    /**
     * 物流状态：0.待发货 1.部分发货 2.已发货  3.部分揽件 4.已揽件 5.运途中 6.部分签收 7.已签收 8.部分拒签 9.全部拒签 10.问题件
     */
    private Integer logisticsStatus;

    /**
     * 支付方式：0=货到付款，1=款到发货
     */
    private Integer payType;

    /**
     * 支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款
     */
    private Integer payMode;

    /**
     * 收款状态 1：是 0：否
     */
    private Integer payStatus;

    /**
     * 物流公司code
     */
    private String expressCompanyCode;


}
