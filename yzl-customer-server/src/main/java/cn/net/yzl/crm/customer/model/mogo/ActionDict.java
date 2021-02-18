package cn.net.yzl.crm.customer.model.mogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ActionDict
 * @description 会员综合行为
 * @date: 2021/1/25 2:22 下午
 */
@Data
public class ActionDict {
    @JsonIgnore
    @ApiModelProperty("会员卡号")
    private String memberCard;

    @ApiModelProperty("综合行为主键")
    private Integer id;

    @ApiModelProperty("综合行为名称")
    private String name;
    //综合行为类型 1、方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好
    @JsonIgnore
    private Integer type;
    //值域1
    private String value;
    //值域2
    private String value2;
}
