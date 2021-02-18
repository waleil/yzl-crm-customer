package cn.net.yzl.crm.customer.vo.work;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="处理工单",description="处理工单类" )
public class MemberWorkOrderInfoVO {

    @ApiModelProperty(value = "顾客卡号")
    private String memberCard;

    @ApiModelProperty(value = "最后一次拨打时间")
    private String lastCallTime;

    @ApiModelProperty(value = "最后一次进线时间")
    private String lastCallInTime;


}
