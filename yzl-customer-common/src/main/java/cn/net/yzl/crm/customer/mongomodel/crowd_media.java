package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("圈选媒体信息")
@Data
public class crowd_media {

    @ApiModelProperty("媒体id")
    private int media_id;

    @ApiModelProperty("媒体名称")
    private String media_name;

    @ApiModelProperty("媒体类型id")
    private int media_type_code;

    @ApiModelProperty("媒体类型名称")
    private String media_type_name;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;
}
