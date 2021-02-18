package cn.net.yzl.crm.customer.viewmodel.memberActionModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * member_action_relation
 * @author
 */
@Data
public class MemberActionRelationList implements Serializable {

    @ApiModelProperty("综合行为类型：1方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
    private Integer type;

    @ApiModelProperty("综合行为类型名")
    private String typeName;

    @ApiModelProperty("顾客卡号")
    private String memberCard;

    private List<MemberActionrRelationItem> items;



    private static final long serialVersionUID = 1L;

}