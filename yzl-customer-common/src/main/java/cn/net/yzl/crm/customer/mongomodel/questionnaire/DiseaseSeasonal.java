package cn.net.yzl.crm.customer.mongomodel.questionnaire;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="DiseaseSeasonal",description="症状是否随季节性变化" )
public class DiseaseSeasonal {

    @ApiModelProperty(value = "是否秋冬加重")
    DiseaseItem periodAllergyDisease;
}
