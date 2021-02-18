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
public class MemberDiseaseIdUpdateVO extends BaseObject {
    @ApiModelProperty(value = "之前的病症id",name = "oldDiseaseId")
    private Integer oldDiseaseId;
    @ApiModelProperty(value = "修改后的病症id",name = "newDiseaseId")
    private Integer newDiseaseId;
}
