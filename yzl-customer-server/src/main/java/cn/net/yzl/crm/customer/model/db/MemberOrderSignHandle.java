package cn.net.yzl.crm.customer.model.db;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MemberOrderSignHandle implements Serializable {

    @ApiModelProperty(value = "主键id",name = "id")
    private Integer id;
    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "订单号",name = "orderNo")
    private String orderNo;
    @ApiModelProperty(value = "订单推送消息数据",name = "orderData")
    private String orderData;
    @ApiModelProperty(value = "创建人",name = "creatorNo")
    private String creatorNo;
    @ApiModelProperty(value = "创建时间",name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "修改人",name = "updatorNo")
    private String updatorNo;
    @ApiModelProperty(value = "修改时间",name = "updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty(value = "消息处理失败错误码",name = "errorCode")
    private String errorCode;
    @ApiModelProperty(value = "消息处理失败错误信息",name = "errorMsg")
    private String errorMsg;
    @ApiModelProperty(value = "状态 1:处理成功 2:处理失败",name = "status")
    private Integer status;

}