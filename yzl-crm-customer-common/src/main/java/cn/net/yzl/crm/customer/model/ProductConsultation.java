package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("顾客商品咨询")
public class ProductConsultation {

    private int id;

    @ApiModelProperty("顾客卡号")
    private String member_card;

    @ApiModelProperty("商品编号")
    private String product_code;
}
