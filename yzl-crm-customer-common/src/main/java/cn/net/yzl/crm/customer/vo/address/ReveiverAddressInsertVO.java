package cn.net.yzl.crm.customer.vo.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * reveiver_address
 * @author 
 */
@Data
@ApiModel(value = "ReveiverAddressDto",description = "顾客收货地址实体")
public class ReveiverAddressInsertVO implements Serializable {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard",required = true)
    @NotBlank
    private String memberCard;
    @ApiModelProperty(value = "顾客姓名",name = "memberName",required = true)
    @NotBlank
    private String memberName;
    @ApiModelProperty(value = "顾客电话，多个用逗号分隔",name = "memberMobile",required = true)
    @NotBlank
    private String memberMobile;
    @ApiModelProperty(value = "顾客省表唯一标识",name = "memberProvinceNo",required = true)
    private Integer memberProvinceNo;
    @ApiModelProperty(value = "顾客省name",name = "memberProvinceName",required = true)
    private String memberProvinceName;
    @ApiModelProperty(value = "顾客市表唯一标识",name = "memberCityNo",required = true)
    private Integer memberCityNo;
    @ApiModelProperty(value = "顾客市name",name = "memberCityName",required = true)
    private String memberCityName;
    @ApiModelProperty(value = "顾客区县表唯一标识",name = "memberCountyNo",required = true)
    private Integer memberCountyNo;
    @ApiModelProperty(value = "顾客区县name",name = "memberCountyName",required = true)
    private String memberCountyName;
    @ApiModelProperty(value = "街道表唯一标识",name = "memberStreetNo")
    private Integer memberStreetNo;
    @ApiModelProperty(value = "街道name",name = "memberStreetName")
    private String memberStreetName;
    @ApiModelProperty(value = "详细地址，街道门牌号",name = "memberAddress",required = true)
    @NotBlank
    private String memberAddress;

}