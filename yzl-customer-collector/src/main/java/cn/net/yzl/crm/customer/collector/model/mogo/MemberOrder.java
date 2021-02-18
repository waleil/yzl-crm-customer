package cn.net.yzl.crm.customer.collector.model.mogo;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: OrderActivity
 * @description 会员参与的活动
 * @date: 2021/1/25 1:38 下午
 */
@Data
public class MemberOrder extends BaseObject {
    //会员卡号
    @JsonIgnore
    private String memberCard;
    //订单参与的活动
    private String activityCode;
    //订单编号
    @JsonIgnore
    private String orderCode;
    //订单关联的物流状态
    private Integer logisticsStatus;
    //订单关联物流单所属公司编号
    private String companyCode;
    //订单状态，来自于订单
    private Integer status;
    //订单参与的活动类型，来自于订单
    private String activityType;
    //支付方式：0=货到付款，1=款到发货
    private Integer payType;
    //支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款
    private Integer payMode;
    //订单来源
    private String source;
    //支付状态
    private Integer payStatus;
    //是否为活动单
    private Boolean activityFlag;
}
