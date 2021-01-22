package cn.net.yzl.crm.customer.mongomodel.crowd;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: CutomerCrowdGroupVO
 * @description todo
 * @date: 2021/1/22 8:17 下午
 */
@Data
public class CustomerCrowdGroupVO extends BaseObject {

    @ApiModelProperty(name = "_id",value = "群组主键")
    private String _id;
    @ApiModelProperty(name = "crowd_name",value = "群组名称")
    private String crowd_name;
}
