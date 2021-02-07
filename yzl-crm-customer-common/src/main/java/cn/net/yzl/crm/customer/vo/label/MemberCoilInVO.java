package cn.net.yzl.crm.customer.vo.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "MemberCoilInVO",description = "顾客实时进线信息接收")
public class MemberCoilInVO {

    @ApiModelProperty(value = "活动id",name="activityId")
    private Integer activityId;

    @ApiModelProperty(value = "活动名称",name="activityName")
    private String activityName;

    @ApiModelProperty(value = "广告ID",name="advId")
    private Integer advId;

    @ApiModelProperty(value = "广告名称",name="advName")
    private String advName;

    @ApiModelProperty(value = "主叫号码",name="callerPhone",required = true)
    private String callerPhone;

    @ApiModelProperty(value = "媒体号",name="mediaId")
    private Integer mediaId;

    @ApiModelProperty(value = "媒体名称",name="mediaName")
    private String mediaName;

    @ApiModelProperty(value = "进线时间",name="callInTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date callInTime;



}
