package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class crowd_base_value {

    @ApiModelProperty("编号")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;

}
