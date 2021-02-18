package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活动")
@Data
public class crowd_active {

    @ApiModelProperty("活动id")
    private int active_id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动类型")
    private int active_type;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
