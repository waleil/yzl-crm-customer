package cn.net.yzl.crm.customer.vo.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "memberGrandSelectVo",description = "顾客会员级别记录")
public class MemberGrandSelectVo {

    @ApiModelProperty(value = "会员卡号",name = "memberCard")
    private String memberCard;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private Date endDate;
}
