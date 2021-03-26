package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@ApiModel(value="TakingProductTimeAndQuantity",description="服用本品时间和量" )
@Data
public class TakingProductTimeAndQuantity {
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "是否持续使用")
    private Boolean isKeepUse;
    @ApiModelProperty(value = "持续时间(天)")
    private Integer keepUseDay;
    @ApiModelProperty(value = "服用量")
    private String dosage;
}
