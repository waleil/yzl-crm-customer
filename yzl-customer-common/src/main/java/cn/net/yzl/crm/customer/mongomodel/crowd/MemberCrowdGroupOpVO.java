package cn.net.yzl.crm.customer.mongomodel.crowd;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据id操作群选规则
 * wangzhe
 * 2021-01-30
 */
@Data
public class MemberCrowdGroupOpVO extends BaseObject {

    @ApiModelProperty(name = "_id",value = "群组主键")
    private String _id;
}
