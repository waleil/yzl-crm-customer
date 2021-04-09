package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberproductMinNumDTO",description = "顾客正在服用商品最小余量")
public class MemberproductMinNumDTO {

    @ApiModelProperty(value ="顾客卡号",name="memberCard")
    private String memberCard;

    @ApiModelProperty(value ="最小余量",name="minNum")
    private Integer minNum;
}
