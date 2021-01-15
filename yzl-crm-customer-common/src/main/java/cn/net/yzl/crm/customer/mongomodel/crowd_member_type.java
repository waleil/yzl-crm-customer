package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("圈选会员级别")
public class crowd_member_type {

    @ApiModelProperty("顾客级别")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
