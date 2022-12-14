package cn.net.yzl.crm.customer.dto.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_grade_record
 * @author 
 */
@ApiModel(value = "MemberGradeRecordDto", description = "会员级别实体")
@Data
public class MemberGradeRecordDto implements Serializable {


    @ApiModelProperty(value ="主键id",name="id",hidden = true)
    private Integer id;
    @ApiModelProperty(value ="顾客卡号",name="memberCard")
    private String memberCard;

    @ApiModelProperty(value ="会员级别id",name="mGradeId")
    private Integer mGradeId;
    @ApiModelProperty(value ="会员级别名称",name="mGradeName")
    private String mGradeName;
    @ApiModelProperty(value ="创建时间",name="createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}