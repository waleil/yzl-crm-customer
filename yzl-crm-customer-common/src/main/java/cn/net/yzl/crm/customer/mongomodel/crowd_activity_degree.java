package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("活跃度实体")
@Data
public class crowd_activity_degree {

    @ApiModelProperty("活跃度 1 活跃 2 一般 3 冷淡")
    private int id;

    @ApiModelProperty("活跃度名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;

}
