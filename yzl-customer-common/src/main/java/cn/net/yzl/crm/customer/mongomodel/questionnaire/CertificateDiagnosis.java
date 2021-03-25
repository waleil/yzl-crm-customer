package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModelProperty;

public class CertificateDiagnosis {


    @ApiModelProperty(value = "是否有医院诊断证明")
    private Boolean hasProve;

    @ApiModelProperty(value = "医院诊断结果")
    private String diagnosisResult;

}
