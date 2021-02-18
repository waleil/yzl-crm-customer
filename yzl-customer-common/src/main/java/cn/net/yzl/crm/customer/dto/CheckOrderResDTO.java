package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="拒收订单返回值类",description="返回值类" )
@Data
public class CheckOrderResDTO implements Serializable {

    @ApiModelProperty(value = "唯一标识id")
    private String id;

    @ApiModelProperty(value = "拒收单编号")
    private String rejectOrderNo;

    @ApiModelProperty(value = "订单表id")
    private String orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "物流公司id")
    private String expressCompanyId;

    @ApiModelProperty(value = "运单表id")
    private String expressWaybillId;

    @ApiModelProperty(value = "拒收单状态：0=待入库，1=已入库，2=库房取消")
    private Byte status;

    @ApiModelProperty(value = "是否预退：0=否，1=是")
    private Byte preRetirement;

    @ApiModelProperty(value = "退货仓库id")
    private Byte orderWarehouseRuleId;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人手机")
    private String receiverMobile;

    @ApiModelProperty(value = "收货人地址")
    private String receiverAddr;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人编号")
    private String updateCode;
}
