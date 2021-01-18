package cn.net.yzl.crm.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel("顾客圈选列表查询条件")
@Data
public class CrowdGroupDTO extends PageDTO {

    @ApiModelProperty("圈选名称")
    private String name;

    @ApiModelProperty("查询圈选状态，-1表示全部，1表示启用，0表示未启用")
    private int enable;

    //@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
  //  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建开始日期")
    private String start_date;

    //@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
   // @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建截止日期")
    private String end_date;




}
