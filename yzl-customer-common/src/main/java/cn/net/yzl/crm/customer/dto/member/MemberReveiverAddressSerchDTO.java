package cn.net.yzl.crm.customer.dto.member;

import cn.net.yzl.crm.customer.dto.PageDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="MemberReveiverAddressSerchDTO",description="顾客收货地址查询条件输入参数类" )
@Data
public class MemberReveiverAddressSerchDTO extends PageDTO {
    @ApiModelProperty(value = "会员卡号")
    private String memberCard;
    @ApiModelProperty(value = "是否默认收货地址")
    private Boolean defaultFLag;


}
