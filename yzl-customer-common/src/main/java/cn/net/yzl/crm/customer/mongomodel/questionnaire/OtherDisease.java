package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModelProperty;

public class OtherDisease {


    @ApiModelProperty(value = "是否有其他病症")
    private Boolean hasOtherDisease;

    @ApiModelProperty(value = "医院诊断结果")
    private String specificDisease;

}
