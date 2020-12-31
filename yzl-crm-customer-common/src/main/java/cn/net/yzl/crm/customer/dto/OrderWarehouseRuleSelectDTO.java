package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="订单分仓规则查询参数类",description="请求参数类" )
@Data
public class OrderWarehouseRuleSelectDTO implements Serializable {

    @ApiModelProperty(value = "订单分仓规则表id")
    private String id;

    @ApiModelProperty(value = "仓库编号")
    private String storeCode;

    @ApiModelProperty(value = "仓库名称")
    private String storeName;

    @ApiModelProperty(value = "仓库类型 0:自建仓库 1:租赁仓库")
    private Byte storeType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人编号")
    private String updateCode;


}