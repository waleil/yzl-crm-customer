package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="省份参数类",description="请求参数类" )
@Data
public class ProvenceDTO implements Serializable {

    @ApiModelProperty(value = "省id")
    private String id;

    @ApiModelProperty(value = "省编码")
    private String code;

    @ApiModelProperty(value = "省所属大区名称")
    private String zname; //大区名称

    @ApiModelProperty(value = "省简称")
    private String aname; //简称


}
