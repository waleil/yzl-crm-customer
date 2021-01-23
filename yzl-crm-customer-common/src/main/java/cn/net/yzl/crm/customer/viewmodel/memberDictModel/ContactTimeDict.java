package cn.net.yzl.crm.customer.viewmodel.memberDictModel;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * contact_time_dict
 * @author 
 */
@Data
public class ContactTimeDict implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 起始时间（包含）
     */
    private Date startTime;

    /**
     * 结束时间（包含）
     */
    private Date endTime;

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