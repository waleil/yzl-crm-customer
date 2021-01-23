package cn.net.yzl.crm.customer.viewmodel.memberDictModel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_order_behavior_relation
 * @author 
 */
@Data
public class MemberOrderBehaviorRelation implements Serializable {
    private Integer id;

    /**
     * 会员卡号
     */
    private String memberCard;

    /**
     * 下单行为字典id
     */
    private Integer did;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    private static final long serialVersionUID = 1L;

  
}