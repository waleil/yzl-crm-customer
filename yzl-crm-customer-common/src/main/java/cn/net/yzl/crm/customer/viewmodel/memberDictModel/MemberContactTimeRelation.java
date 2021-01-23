package cn.net.yzl.crm.customer.viewmodel.memberDictModel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_contact_time_relation
 * @author 
 */
@Data
public class MemberContactTimeRelation implements Serializable {
    private Integer id;

    /**
     * 会员卡号
     */
    private String memberCard;

    /**
     * 时间段字典id
     */
    private Integer did;


    private static final long serialVersionUID = 1L;


}