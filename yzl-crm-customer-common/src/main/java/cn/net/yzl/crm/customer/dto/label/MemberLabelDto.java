package cn.net.yzl.crm.customer.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiModel(value="顾客标签实体",description="入参" )
@Data
@Document(collection="member_label")
public class MemberLabelDto {

    @ApiModelProperty(value = "姓名")
    private String memberName;

    @ApiModelProperty(value = "会员卡号")
    private Integer memberCard;

    @ApiModelProperty(value = "性别")
    private Integer sex;

}
