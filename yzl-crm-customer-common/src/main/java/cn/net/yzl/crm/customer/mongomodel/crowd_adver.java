package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选广告实体")
@Data
public class crowd_adver {
    @ApiModelProperty("广告id")
    private Integer id;

    @ApiModelProperty("广告名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;

}
