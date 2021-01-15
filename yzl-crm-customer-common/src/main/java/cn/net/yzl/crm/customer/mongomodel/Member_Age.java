package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("年龄段实体类")
@Data
public class Member_Age {

    @ApiModelProperty("年龄段名称（幼儿，少年，青年，中年，老年等）")
    private String age_name;

    @ApiModelProperty("开始年龄")
    private int start_age;
    @ApiModelProperty("结束年龄")
    private int end_age;

    @ApiModelProperty("是否包含，1是，0否")
    private int include;


}
