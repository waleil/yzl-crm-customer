package cn.net.yzl.crm.customer.dto.member;

import cn.net.yzl.crm.customer.dto.address.ReveiverAddressMsgDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value="MemberSerchConditionDTO",description="会员基础信息查询条件输入参数类" )
@Data
public class MemberAddressAndLevelDTO {

    @ApiModelProperty(value = "顾客卡号" , name = "memberCard")
    private String memberCard;

    @ApiModelProperty(value = "顾客名称" , name = "memberName")
    private String memberName;

    @ApiModelProperty(value = "顾客级别编号" , name = "gradeId")
    private String gradeId;

    @ApiModelProperty(value = "顾客级别" , name = "gradeCode")
    private String gradeCode;

    @ApiModelProperty(value = "顾客类型" , name = "memberType")
    private String memberType;

    @ApiModelProperty(value = "收获地址信息" , name = "reveiverInformations")
    private List<ReveiverAddressMsgDTO> reveiverInformations;

    @ApiModelProperty(value = "顾客联系电话" , name = "phoneNumbers")
    private List<String> phoneNumbers;

    @ApiModelProperty(value = "顾客余额" , name = "totalMoney")
    private Double totalMoney;


}
