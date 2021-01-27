package cn.net.yzl.crm.customer.dto.member;

import cn.net.yzl.crm.customer.dto.PageDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value="MemberSerchConditionDTO",description="会员基础信息查询条件输入参数类" )
@Data
public class MemberSerchConditionDTO extends PageDTO {
    @ApiModelProperty(value = "顾客名称/会员卡号")
    private String memberName;
    @ApiModelProperty(value = "媒体id")
    private Integer mediaId;
    @ApiModelProperty(value = "广告id")
    private Integer adverCode;

    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Integer memberStatus;
    @ApiModelProperty(value="会员级别id")
    private Integer mGradeId;
    @ApiModelProperty(value = "最后一次下单时间起始时间 格式: yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastOrderTimeStart;

    @ApiModelProperty(value = "最后一次下单时间结束时间 格式: yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastOrderTimeEnd;


}
