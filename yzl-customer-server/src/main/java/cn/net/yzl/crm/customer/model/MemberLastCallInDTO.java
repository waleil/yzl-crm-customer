package cn.net.yzl.crm.customer.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 顾客 最后一次进线，最后一次通话时间 ，
 * 进线咨询的商品
 */
@Data
public class MemberLastCallInDTO implements Serializable {

    /**
     * 最后一次进线时间
     */
    private String lastCallInTime;


    /**
     * 最后一次拨打电话时间
     */
    private String lastDialTime;


    /**
     * 咨询商品
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String pruductName;


}
