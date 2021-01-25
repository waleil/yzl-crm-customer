package cn.net.yzl.crm.customer.model.mogo;

import cn.net.yzl.crm.customer.BaseObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

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
@ApiModel("顾客宽表实体类")
@Document(collection="member_label")
public class MemberLabel extends BaseObject {
    @ApiModelProperty(value = "主键")
    private String _id;
    @ApiModelProperty(value = "会员卡号")
    private String member_card;
    @ApiModelProperty(value = "顾客名称")
    private String member_name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别：0代表男，1代表女,2代表未知")
    private int sex;
    @ApiModelProperty(value = "广告id")
    private Integer adver_code;
    @ApiModelProperty(value = "顾客级别  WK 无卡，PK 普卡，TK 铜卡，YK 银卡，JK 金卡，ZS钻卡，VIP VIP，CVIP 超级VIP")
    private String m_grade_code;
    @ApiModelProperty(value = "0未发卡1已发卡未激活2已激活")
    private Short is_active;
    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Byte member_status;
    @ApiModelProperty(value = "活跃度 1高 2 中 3 低")
    private int activity;
    @ApiModelProperty(value = "所属区")
    private String region_code;

    @ApiModelProperty("所属区")
    private String region_name;

    @ApiModelProperty(value = "所属省份id")
    private Integer province_code;

    @ApiModelProperty(value = "所属省份名称")
    private String province_name;

    @ApiModelProperty(value = "所属城市id")
    private Integer city_code;

    @ApiModelProperty("所属城市名称")
    private Integer city_name;

    @ApiModelProperty("区县id")
    private Integer area_code;

    @ApiModelProperty("区县名称")
    private String area_name;

    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "是否有qq")
    private Boolean has_qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "是否有微信")
    private Boolean has_wechat;
    //是否有账户余额
    private Boolean has_recharge;
    //是否拥有优惠券
    private Boolean has_ticket;

    @ApiModelProperty("是否拥有红包")
    private Boolean has_red_bag;

    @ApiModelProperty("是否拥有积分")
    private Boolean has_integral;
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否有邮箱")
    private Boolean has_email;

    @ApiModelProperty(value = "获客来源渠道id")
    private Integer source;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "会员生日")
    private String birthday;

    @ApiModelProperty("生日月份")
    private int member_month;

    @ApiModelProperty(value = "所属行业")
    private String job_code;


    @ApiModelProperty(value = "主客户会员卡号")
    private Integer master_card;


    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;

    @ApiModelProperty(value = "订购次数")
    private Integer order_num;

    @ApiModelProperty("媒体id")
    private int media_id;

    @ApiModelProperty(value = "首单下单时间")
    private Date first_order_time;

    @ApiModelProperty(value = "最后一次下单时间")
    private Date last_order_time;
    @ApiModelProperty(value = "媒体名称（冗余，暂定第一次进线）")
    private String media_name;

    @ApiModelProperty(value = "媒体类型id")
    private Integer media_type_code;

    @ApiModelProperty(value = "媒体类型名称（冗余，暂定第一次进线）")
    private String media_type_name;

    @ApiModelProperty(value = "是否vip")
    private Boolean vip_flag;

    @ApiModelProperty(value = "购买意向  1 无意向 2 低意向 3中意向 4高意向")
    private Integer buy_intention;

    @ApiModelProperty("广告名称")
    private String adver_name;

    @ApiModelProperty("建档时间")
    private String create_time;

    @ApiModelProperty("顾客类型")
    private String member_type;

    @ApiModelProperty("累计消费金额")
    private int totalCounsumAmount;

    @ApiModelProperty("累计充值金额")
    private int totalInvestAmount;

    @ApiModelProperty("最后一次购买商品")
    private List<String> lastBuyProductCodes;

    @ApiModelProperty("首单订单编号")
    private String first_order_no;

    @ApiModelProperty("首次购买商品")
    private List<String> firstBuyProductCodes;

    @ApiModelProperty("订单最高金额")
    private Integer orderHighAm;

    @ApiModelProperty("订单最低金额")
    private Integer orderLowAm;

    @ApiModelProperty("订单平均金额")
    private Integer orderAvgAm;

    @ApiModelProperty("购买产品种类个数")
    private Integer productTypeCnt;

    @ApiModelProperty("累计购买次数")
    private Integer buyCount;

    @ApiModelProperty("总平均购买天数")
    private Integer dayAvgCount;

    @ApiModelProperty("年度平均购买天数")
    private Integer yearAvgCount;

    @ApiModelProperty("退货率")
    private Integer returnGoodsRate;


    @ApiModelProperty("累计订单总金额")
    private int total_order_amount;

    @ApiModelProperty("订单应收总金额")
    private int order_rec_amount;

    @ApiModelProperty("是否下过订单 1是，0否，-1 不做统计查询")
    private int have_order;

    @ApiModelProperty("订单总金额")
    private int order_total_amount;

    @ApiModelProperty("方便接电话时间")
    private List<ActionDict> phoneDictList;
    //会员病症信息
    private List<MemberDisease> memberDiseaseList;
    //顾客购买商品信息
    private List<MemberProduct> memberProductList;
    //响应时间
    private List<ActionDict> memberResponseTimeList;
    //下单行为
    private List<ActionDict> orderBehaviorList;
    //性格偏好
    private List<ActionDict> memberCharacterList;
    //综合行为
    private List<ActionDict> comprehensiveBehaviorList;
    //订单来源
    private List<OrderSource> orderSourceList;
    //活动名称
    private List<OrderActivity> orderActivityList;
    //订单状态
    private List<OrderStatus> orderStatusList;
    //活动类型
    private List<OrderActivityType> activityTypeList;
    //支付形式
    private List<OrderPayType> payTypeList;
    //支付方式
    private List<OrderPayMode> payModeList;
    //物流状态
    private List<LogisticsStatus> logisticsStatusList;
    //物流公司
    private List<LogisticsCompany> logisticsCompanyList;
    //支付状态
    private List<PayStatus> payStatusList;
}
