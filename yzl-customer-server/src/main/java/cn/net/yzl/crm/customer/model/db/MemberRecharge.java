package cn.net.yzl.crm.customer.model.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(value = "MemberRecharge",description = "顾客充值记录实体")
public class MemberRecharge {
    @ApiModelProperty(value = "主键id")
    private Integer id;
    @ApiModelProperty(value = "顾客卡号")
    private String memberCard;
    @ApiModelProperty(value = "充值金额")
    private Integer inMoney;
    @ApiModelProperty(value = "充值日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    @ApiModelProperty(value = "操作人id")
    private String creatorNo;
    @ApiModelProperty(value = "操作人名称")
    private String creatorName;
    @ApiModelProperty(value = "是否历史数据 0：否 1：是")
    private Boolean isHistory;

}