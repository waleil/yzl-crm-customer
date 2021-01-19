package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("圈选会员类型")
public class crowd_member_type {

    @ApiModelProperty("A,A1,B")
    private String id;

    @ApiModelProperty("顾客类型")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
