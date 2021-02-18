package cn.net.yzl.crm.customer.dto.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 顾客收货地址实体
 * wangzhe
 * 2021-01-29
 */
@Data
@ApiModel(value = "ReveiverAddressMessageDTO",description = "顾客收货地址实体")
public class ReveiverAddressMsgDTO implements Serializable {

    @ApiModelProperty(value = "主键id",name = "id")
    private Integer id;

    @ApiModelProperty(value = "收货人姓名",name = "consignee")
    private String consignee;
    @ApiModelProperty(value = "收货人电话，多个用逗号分隔",name = "recieverPhone")
    private String recieverPhone;

    @ApiModelProperty(value = "顾客省表唯一标识",name = "provinceNo")
    private Integer provinceNo;
    @ApiModelProperty(value = "顾客省name",name = "provinceName")
    private String provinceName;
    @ApiModelProperty(value = "顾客市表唯一标识",name = "cityNo")
    private Integer cityNo;
    @ApiModelProperty(value = "顾客市name",name = "cityName")
    private String cityName;
    @ApiModelProperty(value = "顾客区县表唯一标识",name = "countyNo")
    private Integer countyNo;
    @ApiModelProperty(value = "顾客区县name",name = "countyName")
    private String countyName;
    @ApiModelProperty(value = "街道表唯一标识",name = "streetNo")
    private Integer streetNo;
    @ApiModelProperty(value = "街道name",name = "streetName")
    private String streetName;
    @ApiModelProperty(value = "详细地址，街道门牌号",name = "detailedReceivingAddress")
    private String detailedReceivingAddress;

}