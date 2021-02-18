package cn.net.yzl.crm.customer.collector.model;

import java.util.Date;

import cn.net.yzl.crm.customer.BaseObject;
import lombok.Data;

/**
 * member_lastcallin
 * @author 
 */
@Data
public class MemberLastcallin extends BaseObject {
    /**
     * 顾客编号
     */
    private String memberCard;

    /**
     * 最后一次进线时间
     */
    private Date lastCallInTime;

    /**
     * 最后一次拨打时间
     */
    private Date lastCallTime;
}