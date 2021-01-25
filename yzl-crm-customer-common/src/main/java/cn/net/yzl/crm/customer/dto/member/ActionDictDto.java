package cn.net.yzl.crm.customer.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * action_dict
 * @author 
 */
@Data
public class ActionDictDto implements Serializable {
    @ApiModelProperty("主键,新增时不需填写;一类字典全部删除时,传id为0和type有值的数据")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("综合行为类型：1、方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
    private Byte type;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("值2")
    private String value2;

    @ApiModelProperty("创建人")
    private String creator;


    @ApiModelProperty("修改人")
    private String updator;



}