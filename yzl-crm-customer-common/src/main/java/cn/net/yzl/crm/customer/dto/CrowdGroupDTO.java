package cn.net.yzl.crm.customer.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("顾客圈选查询条件")
@Data
public class CrowdGroupDTO extends PageDTO {

    @ApiModelProperty("圈选名称")
    private String name;

    @ApiModelProperty("查询圈选状态，-1表示全部，1表示启用，0表示未启用")
    private int enable;

    @ApiModelProperty("启用开始日期")
    private String start_date;

    @ApiModelProperty("有效截止日期")
    private String end_date;


}
