package cn.net.yzl.crm.customer.vo.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberJinxianVO",description = "顾客实时进线信息接收")
public class MemberCoilInVO {

    @ApiModelProperty(value = "会员卡号",name="memberCard",required = true)
    private String memberCard;

    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 广告ID
     */
    private Integer advId;
    /**
     * 广告名称
     */
    private String advName;
    /**
     * 呼叫编号
     */
    private String callId;
    /**
     * 被叫号码
     */
    private String calledPhone;
    /**
     * 主叫号码
     */
    private String callerPhone;
    /**
     * 通话时长
     */
    private Integer duration;

    /**
     * 媒体号
     */
    private Integer mediaId;
    /**
     * 媒体名称
     */
    private String mediaName;



}
