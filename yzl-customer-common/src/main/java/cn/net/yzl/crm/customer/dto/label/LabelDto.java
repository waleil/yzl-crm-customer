package cn.net.yzl.crm.customer.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="顾客标签实体",description="入参" )
@Data
public class LabelDto {

    @ApiModelProperty(value = "唯一标识id")
    private Integer id;

    @ApiModelProperty(value = "父标签id")
    private Integer pid;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "映射宽表字段")
    private String fieldName;

    @ApiModelProperty(value = "映射宽表值")
    private Integer code;

    @ApiModelProperty(value = "值域1")
    private String value1;

    @ApiModelProperty(value = "值域2")
    private String value2;

    @ApiModelProperty(value = "标签类型")
    private Short labelType;

    @ApiModelProperty(value = "下限值")
    private Float limitDn;

    @ApiModelProperty(value = "上限值(-1代表无限)")
    private Float limitUp;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "接口路径")
    private String url;

    @ApiModelProperty(value = "是否是复选框(0代表否,1代表是)")
    private Boolean checkBox;

    @ApiModelProperty(value = "标签分组id")
    private Integer groupId;

    @ApiModelProperty(value = "标签层级路线")
    private String route;
}
