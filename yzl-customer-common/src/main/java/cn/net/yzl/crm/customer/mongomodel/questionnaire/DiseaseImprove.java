package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Improve",description="服用臻通集前后症状改善情况" )
public class DiseaseImprove {

    @ApiModelProperty(value = "症状类型(1.晕眩 2.头重如裹 3.胸闷 4.呕恶痰涎 5.芝麻沉重 6.畏寒肢冷)")
    //对于URJ症状类型(1.腰背疼痛 2.腰膝酸软 3.下肢疼痛 4.下肢瘘弱 5.步履艰难 6.目眩)")
    private Integer diseaseType;

    @ApiModelProperty(value = "病症名称")
    private String diseaseName;

    @ApiModelProperty(value = "服用前(1.加重 2.减轻 3.不变)")
    private Integer userBefore;

    @ApiModelProperty(value = "服用后(1.加重 2.减轻 3.不变)")
    private Integer userAfter;



}
