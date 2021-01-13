package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选对应病症实体")
@Data
public class crowd_disease {

    @ApiModelProperty("病症id")
    private int id;

    @ApiModelProperty("病症名称")
    private String name;

    @ApiModelProperty("所属上级id")
    private int pid;
}
