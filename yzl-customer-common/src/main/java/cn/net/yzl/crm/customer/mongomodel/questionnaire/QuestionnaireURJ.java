package cn.net.yzl.crm.customer.mongomodel.questionnaire;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabel
 * @description 用户标签表
 * @date: 2021/1/25 9:25 上午
 */
@Data
@ApiModel(value="QuestionnaireURJ",description="优韧集调查问卷内容" )
public class QuestionnaireURJ extends BaseObject {
//    @ApiModelProperty(value = "主键")
//    private String _id;
    @ApiModelProperty(value = "问卷名称")
    private String formName;
    @ApiModelProperty(value = "问卷序列号")
    private String seqNo;

    @ApiModelProperty(value = "填表人")
    private String  fillFormBy;

    @ApiModelProperty(value = "填表日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date fillFormDate;

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty(value = "是否有医院诊断证明(是否)")
    private Boolean hasCertificateDiagnosis;

    @ApiModelProperty(value = "是否有其他病症(是否)")
    private Boolean hasOtherDisease;

    @ApiModelProperty(value = "服用本品:开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "服用本品:是否持续使用")
    private Boolean isKeepUse;
    @ApiModelProperty(value = "服用本品:持续时间(天)")
    private Integer keepUseDay;
    @ApiModelProperty(value = "服用本品:服用量")
    private String dosage;

    @ApiModelProperty(value = "其他产品使用情况(药品,保健食品)")
    private OtherProductsUseStatus otherProductsUseStatus;

    @ApiModelProperty(value = "服用期间是否有过敏情况(1.皮肤发扬、皮疹、皮肤红肿等症状 2.其他情况)")
    private List<DiseaseItem> periodAllergy;

    @ApiModelProperty(value = "服用期间是否有出现其他异常情况(1.是否出现浮肿等症状 2.心血管是否出现异常(心跳加速)3.胃肠是否出现异常(便秘、腹泻、小便减少) 4.口腔是否出现异常(牙龈肿胀、口干) 5.睡眠是否出现异常(失眠等症状))")
    private List<DiseaseItem> periodOtherAbnormal;

    @ApiModelProperty(value = "病症季节性变化(1.是否秋冬加重 3.是否无规律)")
    private List<DiseaseItem> diseaseSeasonals;

    @ApiModelProperty(value = "服用优韧集前后症状改善情况(症状类型(1.腰背疼痛 2.腰膝酸软 3.下肢疼痛 4.下肢瘘弱 5.步履艰难 6.目眩)")
    private List<DiseaseImprove> diseaseImproves;
    @ApiModelProperty(value = "检测骨密度(T值)")
    private BoneDensity boneDensity;
    @ApiModelProperty(value = "是否绝经(女性顾客)")
    private DiseaseItem menopause;






}
