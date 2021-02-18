package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选商品信息")
@Data
public class crowd_product {

    @ApiModelProperty("商品编码")
    private String id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
