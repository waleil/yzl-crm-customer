package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客行为偏好")
@Data
public class MemberAction {
    private int id;

    @ApiModelProperty("顾客卡号")
    private String member_card;

    @ApiModelProperty("方便接电话时间")
    private String phone_time;

    @ApiModelProperty("性格偏好")
    private String nature;

    @ApiModelProperty("响应时间")
    private String response_time;

    @ApiModelProperty("综合行为")
    private String action;

    @ApiModelProperty("下单行为")
    private String order_action;

    @ApiModelProperty("活动偏好")
    private String active;


}
