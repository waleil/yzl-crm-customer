package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value="OtherProductsUseStatus",description="其他产品使用情况(药品,保健食品)" )
@Data
public class OtherProductsUseStatus {

    @ApiModelProperty(value = "病症类型: 0.其他 1.骨关节 2.肠胃 3.心血管 4.呼吸 5.神经系统 6.皮肤 7.滋补 8.泌尿系统 9.肿瘤")
    private Integer diseaseType;

    @ApiModelProperty(value = "病症名称")
    private String diseaseName;

//    @ApiModelProperty(value = "产品名称01")
//    private Boolean productName1;
//    @ApiModelProperty(value = "产品名称02")
//    private Boolean productName2;
    @ApiModelProperty(value = "产品名称集合")
    List<String> productNames;
    @ApiModelProperty(value = "使用方法(1.内服 2.外敷)")
    private Integer useModel;
    @ApiModelProperty(value = "服用量")
    private String dosage;
    @ApiModelProperty(value = "是否有不良反应")
    private Boolean hasAahsAdverseReaction;
    @ApiModelProperty(value = "不良反-具体情况")
    private String adverseDescription;
    @ApiModelProperty(value = "是否与本品有合作")
    private Boolean hasCooperation;
    @ApiModelProperty(value = "与本品合作-具体情况")
    private String cooperationDescription;
}
