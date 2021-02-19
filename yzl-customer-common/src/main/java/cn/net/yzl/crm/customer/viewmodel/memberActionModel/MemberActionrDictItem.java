package cn.net.yzl.crm.customer.viewmodel.memberActionModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberActionrDictItem implements Serializable {

        @ApiModelProperty("编号")
        private Integer id;

        @ApiModelProperty("综合行为类型：1方便接电话时间 2性格偏好 3响应时间 4 坐席偏好 5 综合行为 6 下单行为 7活动偏好 8 代表年龄段")
        private Integer type;

        @ApiModelProperty("字典值名")
        private String name;

        @ApiModelProperty("值1")
        private String value;

        @ApiModelProperty("值2")
        private String value2;


        private static final long serialVersionUID = 1L;
    }
