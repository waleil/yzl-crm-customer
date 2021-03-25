package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseUserInfo {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "家庭住址")
    private String address;
    @ApiModelProperty(value = "身高")
    private Double height;
    @ApiModelProperty(value = "联系方式")
    private String contact;

    @ApiModelProperty(value = "医院诊断证明")
    private CertificateDiagnosis certificateDiagnosis;

    @ApiModelProperty(value = "其他病症")
    private OtherDisease otherDisease;





}
