package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选城市基本信息")
@Data
public class crowd_city {

    @ApiModelProperty("城市编码")
    private int city_code;

    @ApiModelProperty("城市名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;

}
