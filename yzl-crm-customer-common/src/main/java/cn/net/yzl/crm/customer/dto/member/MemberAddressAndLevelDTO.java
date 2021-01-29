package cn.net.yzl.crm.customer.dto.member;

import cn.net.yzl.crm.customer.dto.address.ReveiverAddressMsgDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value="MemberSerchConditionDTO",description="会员基础信息查询条件输入参数类" )
@Data
public class MemberAddressAndLevelDTO {

    @ApiModelProperty(value = "顾客卡号")
    private String memberCard;

    @ApiModelProperty(value = "顾客名称")
    private String memberName;

    @ApiModelProperty(value = "顾客级别编号")
    private String gradeId;

    @ApiModelProperty(value = "顾客级别")
    private String gradeCode;

    @ApiModelProperty(value = "收获地址信息")
    private List<ReveiverAddressMsgDTO> reveiverAddresss;

    @ApiModelProperty(value = "顾客联系电话")
    private List<String> phoneNumbers;

    @ApiModelProperty(value = "顾客余额")
    private Double totalMoney;


}
