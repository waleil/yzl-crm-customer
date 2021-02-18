package cn.net.yzl.crm.customer.collector.model;

import cn.net.yzl.crm.customer.BaseObject;
import lombok.Data;

/**
 * customer_distinct
 * @author 
 */
@Data
public class CustomerDistinct extends BaseObject {
    private Integer id;

    /**
     * 顾客编号
     */
    private String memberCard;
}