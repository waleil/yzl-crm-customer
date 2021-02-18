package cn.net.yzl.crm.customer.vo;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberType
 * @description 顾客级别查询
 * @date: 2021/2/3 5:08 下午
 */
@Data
public class MemberTypeVO extends BaseObject {
    @ApiModelProperty(value = "会员类型编号",name = "memberTypeId")
    private Integer memberTypeId;
    @ApiModelProperty(value = "会员类型名称",name = "memberTypeName")
    private String memberTypeName;
}
