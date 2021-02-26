package cn.net.yzl.crm.customer.vo.work;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberWorkOrderDiseaseVo",description = "处理工单时顾客综合病症")
public class MemberWorkOrderDiseaseVo {


    @ApiModelProperty("病症id")
    private int diseaseId;

    @ApiModelProperty("病症名称")
    private String diseaseName;

}
