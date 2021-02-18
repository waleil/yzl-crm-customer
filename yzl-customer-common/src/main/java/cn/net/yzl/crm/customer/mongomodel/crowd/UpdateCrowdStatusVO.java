package cn.net.yzl.crm.customer.mongomodel.crowd;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * @author lichanghong
 * @version 1.0
 * @title: UpdateCrowdStatusVO
 * @description 修改圈选规则状态
 * @date: 2021/1/22 6:33 下午
 */
@Data
public class UpdateCrowdStatusVO extends BaseObject {
    @ApiModelProperty(name = "_id",value ="群组id")
    private String _id;

    @ApiModelProperty(name = "enable",value ="是否启用:0=否，1=是")
    @DecimalMin(value = "0",message = "非法状态")
    @DecimalMax(value = "1",message = "非法状态")
    private Integer enable;

    @ApiModelProperty("修改人")
    private String update_code;

    @ApiModelProperty("修改人姓名")
    private String update_name;
}
