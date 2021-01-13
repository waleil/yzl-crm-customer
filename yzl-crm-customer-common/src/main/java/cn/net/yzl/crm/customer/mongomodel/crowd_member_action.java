package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("行为偏好")
@Data
public class crowd_member_action {

    private int id;
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型:1 方便接电话时间，2性格偏好，3响应时间，4坐席偏好，5综合行为，6下单行为，7活动偏好")
    private int ltype;
}
