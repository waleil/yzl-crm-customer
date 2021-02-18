package cn.net.yzl.crm.customer.model.db;

import lombok.Data;

import java.util.Date;

@Data
public class SysDictData {

    private Integer dictCode;

    private Integer dictSort;

    private String dictValue;

    private String dictType;

    private String dictTypeName;

    private Byte dictCcode;

    private Byte isDefault;

    private Byte status;

    private Byte isDel;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private String remark;


}
