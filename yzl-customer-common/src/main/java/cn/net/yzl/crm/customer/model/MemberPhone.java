package cn.net.yzl.crm.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("会员手机号")
@Data
public class MemberPhone {
    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("会员卡号")
    private String member_card;

    @ApiModelProperty("电话号")
    private String phone_number;

    @ApiModelProperty("手机归属地")
    private String phone_place;

    @ApiModelProperty("服务提供商，1 移动，2联通，3电信")
    private Integer service_provider;
    @ApiModelProperty("1 移动电话，2座机")
    private Integer phone_type;

    @ApiModelProperty("创建人no")
    private String creator_no;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    @ApiModelProperty("创建人no")
    private String updator_no;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    @ApiModelProperty("是否可用，1 是 0 否")
    private Integer enabled;






}
