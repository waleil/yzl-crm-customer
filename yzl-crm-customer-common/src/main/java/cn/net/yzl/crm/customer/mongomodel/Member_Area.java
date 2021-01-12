package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@ApiModel("圈选省份实体")
@Data
public class Member_Area {
    @ApiModelProperty("省Id")
    private int province_id;
    @ApiModelProperty("省名称")
    private String province_name;
    @ApiModelProperty("要排除的城市")
    private List<String> except_city_ids;
}
