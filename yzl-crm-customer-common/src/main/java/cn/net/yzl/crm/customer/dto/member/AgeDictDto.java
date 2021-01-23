package cn.net.yzl.crm.customer.dto.member;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * age_dict
 * @author 
 */
@Data
public class AgeDictDto implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 起始年龄（包含）
     */
    private Byte startAge;

    /**
     * 结束年龄（包含）
     */
    private Byte endAge;

    /**
     * 删除标志
     */
    private Byte isDel;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更改人
     */
    private String updator;

    private static final long serialVersionUID = 1L;


}