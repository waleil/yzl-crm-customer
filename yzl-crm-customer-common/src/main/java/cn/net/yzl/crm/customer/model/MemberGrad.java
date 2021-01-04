package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 顾客级别
 */
@Data
public class MemberGrad {
    private  int id;

    @ApiModelProperty("级别编号")
    private String no;

    @ApiModelProperty("级别名称")
    private String name;
}
