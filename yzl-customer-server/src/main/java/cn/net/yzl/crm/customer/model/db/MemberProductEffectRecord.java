package cn.net.yzl.crm.customer.model.db;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * reveiver_address_record
 * @author 
 */
@Data
public class MemberProductEffectRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 修改之前的数据 json串
     */
    private String beforeData;

    /**
     * 修改之后的数据 json串
     */
    private String afterData;

    /**
     * 顾客地址id
     */
    private Integer productEffectId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改人编号
     */
    private String modifyNo;

}