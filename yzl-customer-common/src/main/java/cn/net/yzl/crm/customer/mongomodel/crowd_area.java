package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选省市区实体")
@Data
public class crowd_area {

    @ApiModelProperty("区域编码")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("1省自治区，2市，3区")
    private int level;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
