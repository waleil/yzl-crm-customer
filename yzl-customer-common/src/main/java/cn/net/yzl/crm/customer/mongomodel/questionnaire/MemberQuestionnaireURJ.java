package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabel
 * @description 用户标签表
 * @date: 2021/1/25 9:25 上午
 */
@Data
@ApiModel(value="MemberQuestionnaireURJ",description="服用本品时间和量" )
@Document(collection="member_questionnaire")
public class MemberQuestionnaireURJ extends BaseObject {
    @ApiModelProperty(value = "主键")
    private String _id;

    @ApiModelProperty(value = "问卷序列号")
    private String seqNo;

    @ApiModelProperty(value = "顾客卡号")
    private String  memberCard;

    @ApiModelProperty(value = "创建人编号")
    private String createCode;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改人编号")
    private String updateCode;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "问卷内容详情")
    private QuestionnaireURJ questionnaire;


}
