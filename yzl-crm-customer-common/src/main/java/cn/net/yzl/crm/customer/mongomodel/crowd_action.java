package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("顾客行为偏好")
public class crowd_action {
    @ApiModelProperty("关键主键")
    private int id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型:1 方便接电话时间，2性格偏好，3响应时间，4坐席偏好，5综合行为，6下单行为，7活动偏好")
    private int ltype;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;

}
