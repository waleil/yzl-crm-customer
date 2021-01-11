package cn.net.yzl.crm.customer.mongomodel;

import cn.net.yzl.crm.customer.model.MemberBaseAttr;
import cn.net.yzl.crm.customer.model.MemberPhone;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class MemberMongo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "会员卡号")
    @NotEmpty
    private String member_card;
    @ApiModelProperty(value = "顾客名称")
    private String member_name;
    @ApiModelProperty(value = "称谓")
    private String nick_name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别：0代表男，1代表女")
    private int sex;
    @ApiModelProperty(value = "广告id")
    private Integer adver_code;
    @ApiModelProperty(value = "顾客级别  WK 无卡，PK 普卡，TK 铜卡，YK 银卡，JK 金卡，ZS钻卡，VIP VIP，CVIP 超级VIP")
    private String m_grade_code;
    @ApiModelProperty(value = "0未发卡1已发卡未激活2已激活")
    private Short is_active;
    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Byte member_status;
    @ApiModelProperty(value = "活跃度 1 活跃 2 冷淡 3 一般")
    private int activity;
    @ApiModelProperty(value = "所属区")
    private String region_code;
    @ApiModelProperty(value = "所属省份")
    private Integer province_code;
    @ApiModelProperty(value = "所属城市id")
    private Integer city_code;
    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;
    @ApiModelProperty(value = "qq")
    private String qq;
    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "获客来源渠道id")
    private Integer source;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "会员生日")
    private String birthday;
    @ApiModelProperty(value = "所属行业")
    private String job_code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "首单下单时间")
    private String first_order_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后一次下单时间")
    private String last_order_time;
    @ApiModelProperty(value = "主客户会员卡号")
    private Integer master_card;
    @ApiModelProperty(value = "首单下单员工")
    private Integer first_order_staff_no;
    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;
    @ApiModelProperty(value = "订购次数")
    private Integer order_num;
    @ApiModelProperty(value = "媒体名称（冗余，暂定第一次进线）")
    private String media_name;
    @ApiModelProperty(value = "媒体类型id")
    private Integer media_type_code;
    @ApiModelProperty(value = "媒体类型名称（冗余，暂定第一次进线）")
    private String media_type_name;

    @ApiModelProperty(value = "是否vip")
    private boolean vip_flag;
    @ApiModelProperty(value = "0表示系统自动创建，1 坐席添加")
    private int source_type;
    @ApiModelProperty(value = "介绍是member_card,如果是员工介绍就是staff_no")
    private String intro_no;
    @ApiModelProperty(value = "介绍人姓名")
    private String intro_name;
    @ApiModelProperty(value = "介绍人类型，1员工，2顾客")
    private int intro_type;

    @ApiModelProperty("所属区")
    private String region_name;

    @ApiModelProperty("广告名称")
    private String adver_name;

    private List<MemberPhone> memberPhoneList;
    @ApiModelProperty("建档时间")
    private  String create_time;
    @ApiModelProperty("顾客类型")
    private String member_type;

    @ApiModelProperty("累计消费金额")
    private int total_counsum_amount;

    @ApiModelProperty("累计充值金额")
    private int total_invest_amount;


    @ApiModelProperty("最后一次购买商品")
    private String last_buy_product_code;



    @ApiModelProperty("首单订单编号")
    private String first_order_no;

    @ApiModelProperty("首次购买商品")
    private String first_buy_product_code;


    @ApiModelProperty("订单最高金额")
    private int order_high_am;
    @ApiModelProperty("订单最低金额")
    private int order_low_am;
    @ApiModelProperty("订单平均金额")
    private int order_avg_am;

    @ApiModelProperty("购买产品种类个数")
    private int product_type_cnt;

    @ApiModelProperty("累计购买次数")
    private int buy_count;
    @ApiModelProperty("总平均购买天数")
    private int day_avg_count;
    @ApiModelProperty("年度平均购买天数")
    private int year_avg_count;
    @ApiModelProperty("退货率")
    private String return_goods_rate;

    @ApiModelProperty("顾客基本属性")
    private MemberBaseAttr memberBaseAttr;



}
