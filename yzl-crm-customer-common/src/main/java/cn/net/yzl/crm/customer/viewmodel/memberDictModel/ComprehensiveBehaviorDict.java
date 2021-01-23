package cn.net.yzl.crm.customer.viewmodel.memberDictModel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * comprehensive_behavior_dict
 * @author 
 */
@Data
public class ComprehensiveBehaviorDict implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 删除标志
     */
    private Byte isDel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更改时间
     */
    private Date updateTime;

    /**
     * 更改人
     */
    private String updator;

    private static final long serialVersionUID = 1L;


}