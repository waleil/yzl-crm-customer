package cn.net.yzl.crm.customer.dto.crowdgroup;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author lichanghong
 * @version 1.0
 * @title: GroupRefMember
 * @description 顾客圈选结果
 * @date: 2021/1/28 12:30 上午
 */
@Data
@Document(collection="group_ref_member")
@ApiModel(value="圈选顾客返回结果",description="圈选顾客返回结果" )
public class GroupRefMember extends BaseObject {
    @ApiModelProperty(name = "groupId",value = "群组编号")
    private String groupId;
    @ApiModelProperty(name = "memberCard",value = "顾客编号")
    private String memberCard;
    @ApiModelProperty(name = "memberName",value = "顾客名称")
    private String memberName;

    @ApiModelProperty(name = "version",value = "版本号")
    private String version;
}
