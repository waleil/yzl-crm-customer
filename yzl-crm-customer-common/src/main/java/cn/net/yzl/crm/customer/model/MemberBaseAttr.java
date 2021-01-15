package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客基本属性")
@Data
public class MemberBaseAttr {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型 ： 1 行为偏好")
    private int ltype;
}
