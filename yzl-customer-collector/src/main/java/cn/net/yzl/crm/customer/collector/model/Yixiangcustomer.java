package cn.net.yzl.crm.customer.collector.model;

import java.util.Date;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * yixiangcustomer
 * @author 
 */
@Data
public class Yixiangcustomer extends BaseObject {
    @JsonIgnore
    private Integer id;
    /**
     * member_card
     */
    private String memberCard;

    /**
     * 最后一次拨打时间
     */
    private Date lastCallTime;
    /**
     * 进线咨询商品
     */
    private String productCode;
    /**
     * 最后一次进线时间
     */
    private Date lastCallInTime;

}