package cn.net.yzl.crm.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客圈选列表查询条件")
@Data
public class MemberQuwarionnireDTO extends PageDTO {

    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;

    @ApiModelProperty(value = "顾客姓名",name = "memberName")
    private String memberName;

    @ApiModelProperty(value = "序列号",name = "seqNo")
    private String seqNo;

    @ApiModelProperty(value = "性别",name = "sex")
    private Integer sex;

    @ApiModelProperty(value = "调研开始时间",name = "startTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String startTime;

    @ApiModelProperty(value = "调研截止日期",name = "endTime")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String endTime;

}
