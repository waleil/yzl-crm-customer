package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客病症")
@Data
public class MemberDiseaseDto {

    @ApiModelProperty("顾客卡号")
    private String memberCard;

    @ApiModelProperty("病症id")
    private int diseaseId;

    @ApiModelProperty("病症名称")
    private String diseaseName;

    @ApiModelProperty("父病症id")
    private int parDiseaseName;

    @ApiModelProperty("操作人")
    private String createNo;
}
