package cn.net.yzl.crm.customer.model;

import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("会员手机号")
@Data
public class MemberPhone {
    @ApiModelProperty("主键id")
    private int id;

    @ApiModelProperty("会员卡号")
    private String member_card;

    @ApiModelProperty("电话号")
    private String phone_number;

    @ApiModelProperty("服务提供商，1 移动，2联通，3电信")
    private int service_provider;
    @ApiModelProperty("1 移动电话，2座机")
    private int phone_type;

}
