package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="订单查询参数类",description="参数类" )
@Data
public class OrderSelectParamterDTO implements Serializable {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单渠道：0=OTC，1=电媒")
    private Byte orderChannel;


    @ApiModelProperty(value = "订单状态：0=话务待审核，1=话务未通过，2=待发货，3=已发货，4=已签收")
    private Byte orderStatus;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
