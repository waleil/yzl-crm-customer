package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("人群圈选实体类")
@Data
public class CrowdGroup {
    private int id;

    @ApiModelProperty("群组名称")
    private String name;

    @ApiModelProperty("群组描述")
    private String description;

    @ApiModelProperty("是否启用:0=否，1=是")
    private  int enabled;
    @ApiModelProperty("生效时间")
    private String effective_date;

    @ApiModelProperty("失效时间")
    private String expire_date;

    @ApiModelProperty("是否删除，true删除，false未删除")
    private boolean del;

    @ApiModelProperty("创建时间")
    private  String create_time;

    @ApiModelProperty("创建人编码")
    private String create_code;

    @ApiModelProperty("创建人姓名")
    private String create_name;

    @ApiModelProperty("修改时间")
    private String update_time;

    @ApiModelProperty("修改人")
    private String update_code;

    private String label_condition;

    @ApiModelProperty("群组人数")
    private int person_count;

}
