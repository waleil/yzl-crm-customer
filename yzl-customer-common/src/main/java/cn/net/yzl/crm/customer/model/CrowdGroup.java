package cn.net.yzl.crm.customer.model;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@ApiModel("人群圈选实体类")
@Data
public class CrowdGroup extends BaseObject {
    @ApiModelProperty("群组id")
    private String _id;
    @ApiModelProperty("群组名称")
    @Field("crowd_name")
    private String crowd_name;
    @ApiModelProperty("群组描述")
    private String description;
    @ApiModelProperty("是否启用:0=否，1=是")
    private  Integer enable;
    @ApiModelProperty("生效时间")
    private String effective_date;
    @ApiModelProperty("失效时间")
    private String expire_date;
    @ApiModelProperty("是否删除，true删除，false未删除")
    private boolean del;
    @ApiModelProperty("创建时间")
    private String create_time;
    @ApiModelProperty("创建人编码")
    private String create_code;
    @ApiModelProperty("创建人姓名")
    private String create_name;
    @ApiModelProperty("修改时间")
    private String update_time;
    @ApiModelProperty("修改人")
    private String update_code;

    @ApiModelProperty("修改人姓名")
    private String update_name;

    @ApiModelProperty("群组人数")
    private Integer person_count;





}
