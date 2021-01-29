package cn.net.yzl.crm.customer.dto.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * reveiver_address
 * @author 
 */
@Data
@ApiModel(value = "ReveiverAddressMessageDTO",description = "顾客收货地址实体")
public class ReveiverAddressMsgDTO implements Serializable {

    @ApiModelProperty(value = "收货人姓名",name = "addressee")
    private String addressee;
    @ApiModelProperty(value = "收货人电话，多个用逗号分隔",name = "memberMobile")
    private String memberMobile;

    @ApiModelProperty(value = "顾客省表唯一标识",name = "memberProvinceNo")
    private Integer memberProvinceNo;
    @ApiModelProperty(value = "顾客省name",name = "memberProvinceName")
    private String memberProvinceName;
    @ApiModelProperty(value = "顾客市表唯一标识",name = "memberCityNo")
    private Integer memberCityNo;
    @ApiModelProperty(value = "顾客市name",name = "memberCityName")
    private String memberCityName;
    @ApiModelProperty(value = "顾客区县表唯一标识",name = "memberCountyNo")
    private Integer memberCountyNo;
    @ApiModelProperty(value = "顾客区县name",name = "memberCountyName")
    private String memberCountyName;
    @ApiModelProperty(value = "街道表唯一标识",name = "memberStreetNo")
    private Integer memberStreetNo;
    @ApiModelProperty(value = "街道name",name = "memberStreetName")
    private String memberStreetName;
    @ApiModelProperty(value = "详细地址，街道门牌号",name = "memberAddress")
    private String memberAddress;

}