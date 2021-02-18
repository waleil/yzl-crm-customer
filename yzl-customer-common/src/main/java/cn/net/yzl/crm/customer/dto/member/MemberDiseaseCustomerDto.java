package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "MemberDiseaseDto",description = "顾客病症")
public class MemberDiseaseCustomerDto {
    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "病症id",name = "diseaseId")
    private int diseaseId;
    @ApiModelProperty(value = "病症名称",name = "diseaseName")
    private String diseaseName;
    @ApiModelProperty(value = "子病症集合",name = "child")
    private List<MemberDiseaseCustomerDto> child;


    @ApiModelProperty(value = "病症pid",name = "diseasePid")
    private int diseasePid;
    @ApiModelProperty(value = "病症父名称",name = "diseasePname")
    private String diseasePname;
}
