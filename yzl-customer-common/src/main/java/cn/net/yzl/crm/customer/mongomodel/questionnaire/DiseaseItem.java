package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="DiseaseItem",description="病症情况" )
public class DiseaseItem {

    @ApiModelProperty(value = "1.皮肤发扬、皮疹、皮肤红肿等症状 2.其他情况 3.石佛出现浮肿等症状 4.心血管是否出现异常(心跳加速) " +
            "5.胃肠是否出现异常(便秘、腹泻、小便减少)" +
            "6.口腔是否出现异常(牙龈肿胀、口干) 7.睡眠是否出现异常(失眠等症状)")
    private Boolean diseaseType;
    @ApiModelProperty(value = "病症名称")
    private String diseaseName;

    @ApiModelProperty(value = "是否有对应病症")
    private Boolean hasDisease;

    @ApiModelProperty(value = "1.三七 2.黄芪 3.其他")
    private Integer type;

    @ApiModelProperty(value = "是否有其他病症")
    private Boolean hasOtherDisease;

    @ApiModelProperty(value = "具体情况")
    private String description;

}
