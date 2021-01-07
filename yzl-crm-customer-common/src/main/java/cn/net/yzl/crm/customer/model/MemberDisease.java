package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("顾客病症")
public class MemberDisease {
    private  int id;

    @ApiModelProperty("顾客卡号")
    private String member_card;

    @ApiModelProperty("病症id")
    private int disease_id;
}
