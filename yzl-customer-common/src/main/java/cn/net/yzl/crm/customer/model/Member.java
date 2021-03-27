package cn.net.yzl.crm.customer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@ApiModel(value="会员基础信息vo类",description="会员基础信息vo类" )
@Data
public class Member {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "会员卡号")
    @NotEmpty
    private String member_card;
    @ApiModelProperty(value = "顾客名称")
    private String member_name;

    @ApiModelProperty(value = "密码")
    private String passwd;

    @ApiModelProperty(value = "称谓")
    private String nick_name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别：0代表男，1代表女，2代表未知")
    private Integer sex;
    @ApiModelProperty(value = "广告id")
    private Integer adver_code;
    @ApiModelProperty(value = "顾客级别  WK 无卡，PK 普卡，TK 铜卡，YK 银卡，JK 金卡，ZS钻卡，VIP VIP，CVIP 超级VIP")
    private String m_grade_code;
    @ApiModelProperty("会员级别id")
    private Integer mGradeId;
    @ApiModelProperty("会员级别名称")
    private String mGradeName;
    @ApiModelProperty(value = "0未发卡1已发卡未激活2已激活")
    private Integer is_active;
    @ApiModelProperty(value = "会员状态1 正常 ，2 恶意3 拒访 4 无效 5 放弃")
    private Integer member_status;
    @ApiModelProperty(value = "活跃度 1 活跃 2 冷淡 3 一般")
    private Integer activity;

    @ApiModelProperty(value = "所属区")
    private String region_code;
    @ApiModelProperty("所属区")
    private String region_name;

    @ApiModelProperty(value = "所属省份")
    private Integer province_code;
    @ApiModelProperty(value = "所属省份名称")
    private String province_name;

    @ApiModelProperty(value = "所属城市id")
    private Integer city_code;
    @ApiModelProperty(value = "所属城市名称")
    private String city_name;

    @ApiModelProperty(value = "所属区域编号")
    private Integer area_code;
    @ApiModelProperty(value = "所属区域")
    private String area_name;

    @ApiModelProperty(value = "媒体id")
    private Integer media_id;
    @ApiModelProperty(value = "媒体名称")
    private String media_name;
    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;
    @ApiModelProperty(value = "qq")
    private String qq;
    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "邮箱")
    private String email;
    //@ApiModelProperty(value = "获客来源渠道id：1 tv 2 internet 3 paper 4 radio 5 introduce 6 other")
    @ApiModelProperty(value = "获客来源渠道(媒介类型) -1：其他，0:电视媒体, 1:广播电台媒体，2：社区媒体，3：户外媒体，4：印刷媒体，5：互联网媒体，6：电商站内流量媒体",name="source")
    private Integer source;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "会员生日")
    private Date birthday;

    @ApiModelProperty(value = "所属行业")
    private String job_code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "首单下单时间")
    private Date first_order_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后一次下单时间")
    private Date last_order_time;
    @ApiModelProperty(value = "主客户会员卡号")
    private String master_card;
    @ApiModelProperty(value = "首单下单员工")
    private String first_order_staff_no;
    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;
    @ApiModelProperty(value = "订购次数")
    private Integer order_num;
    @ApiModelProperty(value = "媒体类型id")
    private Integer media_type_code;
    @ApiModelProperty(value = "媒体类型名称（冗余，暂定第一次进线）")
    private String media_type_name;

    @ApiModelProperty(value = "是否vip")
    private boolean vip_flag;
    @ApiModelProperty(value = "成为vip的时间(第一次订单签收的时间)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date vip_time;
    @ApiModelProperty(value = "0表示系统自动创建，1 坐席添加")
    private int source_type;
    @ApiModelProperty(value = "介绍是member_card,如果是员工介绍就是staff_no")
    private String intro_no;
    @ApiModelProperty(value = "介绍人姓名")
    private String intro_name;
    @ApiModelProperty(value = "介绍人类型，1员工，2顾客")
    private int intro_type;

    @ApiModelProperty("广告名称")
    private String adver_name;

    private List<MemberPhone> memberPhoneList;
    @ApiModelProperty("建档时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date create_time;
    @ApiModelProperty("顾客类型")
    private String member_type;

    @ApiModelProperty("收货地址")
    private List<ReveiverAddress> receive_address_list;

    @ApiModelProperty("顾客账户")
    private MemberAmount member_amount;

    @ApiModelProperty("是否拒访，1 是0 否")
    private Integer reject_flag;

    @ApiModelProperty("购买意向  1 无意向 2 低意向 3中意向 4高意向")
    private Integer buy_intention;
    @ApiModelProperty("身份证号")
    private String id_card;
    @ApiModelProperty("创建人编号")
    private String creator_no;

    @ApiModelProperty("创建人姓名")
    private String creator_name;
    @ApiModelProperty("编辑人编号")
    private String updator_no;

    @ApiModelProperty("编辑人姓名")
    private String updator_name;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_time;


}