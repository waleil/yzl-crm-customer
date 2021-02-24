package cn.net.yzl.crm.customer.collector.model;

import cn.net.yzl.crm.customer.BaseObject;
import cn.net.yzl.crm.customer.collector.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.collector.model.mogo.MemberDisease;
import cn.net.yzl.crm.customer.collector.model.mogo.MemberOrder;
import cn.net.yzl.crm.customer.collector.model.mogo.MemberProduct;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @ApiModelProperty(value = "会员卡号")
    @Indexed
    private String memberCard;
    @ApiModelProperty(value = "顾客名称")
    private String memberName;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别：0代表男，1代表女,2代表未知")
    private int sex;
    @ApiModelProperty(value = "广告id")
    private Integer adverCode;
    @ApiModelProperty(value = "顾客级别  WK 无卡，PK 普卡，TK 铜卡，YK 银卡，JK 金卡，ZS钻卡，VIP VIP，CVIP 超级VIP")
    private String mGradeCode;
    @ApiModelProperty(value = "顾客级别编号，来自DMC")
    private Integer mGradeId;
    @ApiModelProperty(value = "0未发卡1已发卡未激活2已激活")
    private int active;
    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private int memberStatus;
    @ApiModelProperty(value = "活跃度 1高 2 中 3 低")
    private int activity;
    @ApiModelProperty(value = "所属区")
    private String regionCode;
    private Integer dbId;
    @ApiModelProperty("所属区")
    private String regionName;

    @ApiModelProperty(value = "所属省份id")
    private Integer provinceCode;

    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    @ApiModelProperty(value = "所属城市id")
    private Integer cityCode;

    @ApiModelProperty("所属城市名称")
    private String cityName;

    @ApiModelProperty("区县id")
    private Integer areaCode;

    @ApiModelProperty("区县名称")
    private String areaName;

    @ApiModelProperty(value = "累计消费金额")
    private Integer totalAmount;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "是否有qq")
    private Boolean hasQQ;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty("总剩余金额")
    private int totalMoney;
    //是否有余额
    private Boolean hasMoney;

    @ApiModelProperty(value = "是否有微信")
    private Boolean hasWechat;
    //是否有账户余额
    private Boolean hasRecharge;
    //是否拥有优惠券
    private Boolean hasTicket;

    @ApiModelProperty("是否拥有红包")
    private Boolean hasTedBag;

    @ApiModelProperty("是否拥有积分")
    private Boolean hasIntegral;
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否有邮箱")
    private Boolean hasEmail;

    @ApiModelProperty(value = "获客来源渠道id")
    private Integer source;
    @ApiModelProperty(value = "会员生日")
    private String birthday;

    @ApiModelProperty("生日月份")
    private int memberMonth;

    @ApiModelProperty(value = "所属行业")
    private String jobCode;


    @ApiModelProperty(value = "主客户会员卡号")
    private String masterCard;


    @ApiModelProperty(value = "真正首单金额")
    private Integer firstOrderAm;

    @ApiModelProperty(value = "订购次数")
    private Integer orderNum;

    @ApiModelProperty("媒体id")
    private int mediaId;
    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty(value = "首单下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstOrderTime;

    private long firstOrderTimeL;
    @ApiModelProperty(value = "最后一次下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOrderTime;

    private long lastOrderTimeL;
    @ApiModelProperty(value = "媒体名称（冗余，暂定第一次进线）")
    private String mediaName;

    @ApiModelProperty(value = "媒体类型id")
    private Integer mediaTypeCode;

    @ApiModelProperty(value = "媒体类型名称（冗余，暂定第一次进线）")
    private String mediaTypeName;

    @ApiModelProperty(value = "是否vip")
    private Boolean vipFlag;

    @ApiModelProperty(value = "购买意向  1 无意向 2 低意向 3中意向 4高意向")
    private Integer buyIntention;

    @ApiModelProperty("广告名称")
    private String adverName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("建档时间")
    private Date createTime;
    private long createTimeL;
    //最后签收时间
    private Date lastSignTime;
    private long lastSignTimeL;
    @ApiModelProperty("顾客类型")
    private String memberType;

    @ApiModelProperty("累计消费金额")
    private int totalCounsumAmount;

    @ApiModelProperty("累计充值金额")
    private int totalInvestAmount;
    @ApiModelProperty("最后一次购买商品")
    private List<String> lastBuyProductCodes;
    @JsonIgnore
    private String lastBuyProductCode;
    @ApiModelProperty("首单订单编号")
    private String firstOrderNo;

    @ApiModelProperty("首次购买商品")
    private List<String> firstBuyProductCodes;
    @JsonIgnore
    private String firstBuyProductCod;
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
    private int totalOrderAmount;

    @ApiModelProperty("订单应收总金额")
    private int orderRecAmount;

    @ApiModelProperty("是否下过订单 1是，0否，-1 不做统计查询")
    private int haveOrder;


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
    //活动偏好
    private List<ActionDict> activityBehaviorList;
    //顾客关联的订单
    private List<MemberOrder> memberOrders;
    //最后一次进线广告关联的商品编号
    private List<String> advertProducts;
    //最后一次拨打时间
    private Date lastCallTime;
    private long lastCallTimeL;
    //最后一次进线时间
    private Date lastCallInTime;
    private long lastCallInTimeL;
}
