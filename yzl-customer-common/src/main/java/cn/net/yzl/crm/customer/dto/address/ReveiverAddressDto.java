package cn.net.yzl.crm.customer.dto.address;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * reveiver_address
 * @author 
 */
@Data
@ApiModel(value = "ReveiverAddressDto",description = "顾客收货地址实体")
public class ReveiverAddressDto implements Serializable {

    @ApiModelProperty(value = "id",name = "id")
    private Integer id;
    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "顾客姓名",name = "memberName")
    private String memberName;
    @ApiModelProperty(value = "顾客电话，多个用逗号分隔",name = "memberMobile")
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

    @ApiModelProperty(value = "是否是默认收货地址:0=不是,1=是",name = "defaultFlag",required = false)
    private Integer defaultFlag;

}