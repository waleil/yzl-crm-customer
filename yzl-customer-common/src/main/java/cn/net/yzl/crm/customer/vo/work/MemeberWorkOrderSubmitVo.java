package cn.net.yzl.crm.customer.vo.work;

import cn.net.yzl.crm.customer.model.MemberDisease;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;
import cn.net.yzl.crm.customer.vo.ProductConsultationInsertVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value="处理工单提交类",description="处理工单提交类" )
public class MemeberWorkOrderSubmitVo {
    /**
     * 顾客信息
     */
    @NotBlank(message = "会员卡号不能为空")
    @ApiModelProperty(value = "会员卡号",required = true)
    private String memberCard;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名",required = true)
    private String memberName;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty(value = "性别：0代表男，1代表女，2代表未知",required = true)
    private Integer sex;

    @NotNull(message = "年龄不能为空")
    @ApiModelProperty(value = "年龄",required = true)
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号",required = true)
    private String memberPhone;

    @ApiModelProperty(value = "座机号",required = false)
    private String fixedPhoneNum;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "qq")
    private Integer qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "所属区")
    private String regionCode;
    @ApiModelProperty(value = "大区名称")
    private String regionName;

    @ApiModelProperty(value = "所属省份")
    private Integer provinceCode;
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

//    @ApiModelProperty(value = "所属城市id")
//    private Integer cityCode;
//    @ApiModelProperty(value = "所属城市名称")
//    private String cityName;
//
//    @ApiModelProperty(value = "所属区域编号")
//    private Integer areaCode;
//    @ApiModelProperty(value = "所属区域")
//    private String areaName;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "活跃度 1：高，2：中，3：底")
    private Integer activity;

//    @ApiModelProperty(value = "广告id")
//    private Integer advId;
//
//    @ApiModelProperty(value = "广告名称")
//    private String advName;

//    @ApiModelProperty(value = "分配状态：0:未分配，1：自动分配，2：人工分配")
//    private Integer allocateStatus;

//    private Integer mediaId;
//    @ApiModelProperty(value = "媒体名称")
//    private String mediaName;

    @ApiModelProperty("购买意向  1 无意向 2 低意向 3中意向 4高意向")
    private Integer buyIntention;

    @ApiModelProperty(value = "员工号", name = "staffNo", required = false)
    private String staffNo;

    @ApiModelProperty(value = "员工名称", name = "staffName", required = false)
    private String staffName;

    @ApiModelProperty(value = "顾客综合病症集合", name = "memberDiseaseList", required = false)
    private List<MemberWorkOrderDiseaseVo> memberDiseaseList;

    @ApiModelProperty(value = "商品服用效果集合", name = "productEffectList", required = false)
    private List<MemberProductEffectUpdateVO> productEffectList;

    @ApiModelProperty(value = "顾客咨询商品", name = "productConsultationInsertVOList", required = false)
    private List<ProductConsultationInsertVO> productConsultationInsertVOList;

    @ApiModelProperty(value = "顾客综合行为偏好二级id", name = "memberActionDIdList", required = false)
    private List<Integer> memberActionDIdList;

}
