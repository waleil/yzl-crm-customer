package cn.net.yzl.crm.customer.vo.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "MemberHangUpVO",description = "挂断电话")
public class MemberHangUpVO {

    @ApiModelProperty(value = "顾客卡号")
    private String memberCard;



}
