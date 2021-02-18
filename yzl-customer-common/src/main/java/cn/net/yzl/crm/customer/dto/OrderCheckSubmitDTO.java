package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="订单审核提交参数类",description="参数类" )
@Data
public class OrderCheckSubmitDTO implements Serializable {


    @ApiModelProperty(value = "订单id")
    private String id;

    @ApiModelProperty(value = "审核意见")
    private String checkOpinion;

    @ApiModelProperty(value = "沟通一键")
    private String telContext;




}
