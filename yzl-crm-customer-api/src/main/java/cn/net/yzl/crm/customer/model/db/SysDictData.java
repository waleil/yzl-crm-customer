package cn.net.yzl.crm.customer.model.db;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_dict_data
 * @author 
 */
@Data
public class SysDictData implements Serializable {
    /**
     * 字典编码
     */
    private Integer dictCode;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典类型名称
     */
    private String dictTypeName;

    /**
     * 老系统对应的字段值
     */
    private Byte dictCcode;

    /**
     * 是否默认（1是 0否）
     */
    private Byte isDefault;

    /**
     * 状态（0正常 1停用）
     */
    private Byte status;

    /**
     * 是否删除(0:否,1:是)
     */
    private Byte isDel;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updator;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}