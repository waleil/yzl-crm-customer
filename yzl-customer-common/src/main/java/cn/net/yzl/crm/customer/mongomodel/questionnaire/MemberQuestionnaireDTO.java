package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 顾客调查问卷记录
 * wangzhe
 * 2021-03-25
 */
@Data
@ApiModel(value="MemberQuestionnaire",description="顾客调查问卷记录" )
@Document(collection="member_questionnaire")
public class MemberQuestionnaireDTO extends BaseObject {
    @ApiModelProperty(value = "主键")
    private String _id;

    @ApiModelProperty(value = "问卷序列号",name = "seqNo",required = true)
    @NotBlank(message = "问卷序列号不能为空!")
    private String seqNo;

    @ApiModelProperty(value = "问卷名称",name = "formName",required = true)
    @NotBlank(message = "问卷序列号不能为空!")
    private String formName;
    @ApiModelProperty(value = "性别：0代表男，1代表女,2代表未知",name = "sex")
    private Integer sex;
    @ApiModelProperty(value = "性别",name = "sex")
    private String sexName;
    @ApiModelProperty(value = "会员级别id",name = "gradeId",required = true)
    private Integer gradeId;
    @ApiModelProperty(value = "会员级别名称",name = "gradeName",required = true)
    private String gradeName;
    @ApiModelProperty(value = "顾客姓名",name = "memberName")
    private String  memberName;

    @ApiModelProperty(value = "顾客卡号",name = "memberCard",required = true)
    @NotBlank(message = "顾客卡号不能为空!")
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

    @ApiModelProperty(value = "问卷内容",name = "questionnaire",required = true)
    private Object questionnaire;

    public <T> T getQuestionnaire() {
        return (T)questionnaire;
    }

    public <T> void setQuestionnaire(T questionnaire) {
        this.questionnaire = questionnaire;
    }




    @Deprecated
    @ApiModelProperty(value = "优韧集调查问卷内容",name = "questionnaireURJ")
    private QuestionnaireURJ questionnaireURJ;
    @Deprecated
    @ApiModelProperty(value = "臻通集调查问卷内容",name = "questionnaireZTJ")
    private QuestionnaireZTJ questionnaireZTJ;


}
