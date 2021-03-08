package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客收获地址")
@Data
public class ReveiverAddress {
    private int id;

    @ApiModelProperty("唯一编码")
    private int reveiver_address_code;

    @ApiModelProperty("顾客表唯一标识")
    private String member_card;

    @ApiModelProperty("顾客姓名")
    private String member_name;

    @ApiModelProperty("顾客电话，多个用逗号分隔")
    private String member_mobile;

    @ApiModelProperty("顾客省表唯一标识")
    private int member_province_no;

    @ApiModelProperty("顾客省name")
    private String member_province_name;

    @ApiModelProperty("顾客市表唯一标识")
    private int member_city_no;

    @ApiModelProperty("顾客市name")
    private String member_city_name;

    @ApiModelProperty("顾客区县表唯一标识")
    private int member_country_no;

    @ApiModelProperty("顾客区县name")
    private String member_country_name;

    @ApiModelProperty("街道表唯一标识")
    private int member_street_no;

    @ApiModelProperty("街道name")
    private String member_street_name;

    @ApiModelProperty("详细地址，街道门牌号")
    private String member_address;

    @ApiModelProperty("状态默认0  无效1")
    private int status;

    @ApiModelProperty("创建人编号")
    private String create_code;

    @ApiModelProperty("修改人编号")
    private String update_code;

    @ApiModelProperty(value = "是否是默认收货地址:0=不是,1=是",name = "defaultFlag",required = false)
    private Integer defaultFlag;



}
