package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="RelatedIndex",description="是否检测相关指标" )
public class RelatedIndex {

    @ApiModelProperty(value = "服用前体重(kg)")
    private Integer weight;
    @ApiModelProperty(value = "服用前BMI(kg/m²)")
    private Integer BMI;
    @ApiModelProperty(value = "总胆固醇TC(mmol/L)")
    private Integer TC;

    @ApiModelProperty(value = "甘油三酯TG(mmol/L)")
    private Integer TG;

    @ApiModelProperty(value = "高密度脂蛋白胆固醇HDC-C(mmol/L)")
    private Integer HDCC;
    @ApiModelProperty(value = "低密度脂蛋白胆固醇LDC-C(mmol/L)")
    private Integer LDCC;

}
