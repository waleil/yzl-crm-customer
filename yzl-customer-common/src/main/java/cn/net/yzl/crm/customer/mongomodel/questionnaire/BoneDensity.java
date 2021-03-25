package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value="BoneDensity",description="检测骨密度(T值)" )
public class BoneDensity {
    @ApiModelProperty(value = "服用前(1.T≥-0.1 2.-0.1<T<-0.2.5 3.T≤-2.5合并脆性骨折)")
    private Integer before;
    @ApiModelProperty(value = "服用前检测数据")
    private Double beforeData;

    //如T≥-0.1，代表骨质基本正常：T值位于-0.1与-2.5之间为骨量减少；T值≤-2.5可诊断为骨质疏松；T值≤-2.5合并脆性骨折，诊断为严重骨质疏松。
    @ApiModelProperty(value = "服用后(1.T≥-0.1 2.-0.1<T<-0.2.5 3.T≤-2.5合并脆性骨折)")
    private Integer after;
    @ApiModelProperty(value = "服用后检测数据")
    private Double afterData;



}
