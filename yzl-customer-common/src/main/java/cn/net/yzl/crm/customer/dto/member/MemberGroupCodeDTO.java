package cn.net.yzl.crm.customer.dto.member;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberGroupCodeDTO",description = "顾客所属的群组编号")
public class MemberGroupCodeDTO {

    @ApiModelProperty(value = "会员卡号",name="memberCard")
    private String memberCard;

    @ApiModelProperty(value = "顾客所属群组编号",name="memberGroupCode")
    private String memberGroupCode;


}
