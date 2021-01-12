package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("顾客圈选实体类")
@Data
public class Member_Crowd_Group {
    @ApiModelProperty("群组id")
    private String crowd_id;
    @ApiModelProperty("群组名称")
    private String crowd_name;
    @ApiModelProperty("群组描述")
    private String description;
    @ApiModelProperty("是否启用:0=否，1=是")
    private  int enable;
    @ApiModelProperty("生效时间")
    private String effective_date;
    @ApiModelProperty("失效时间")
    private String expire_date;
    @ApiModelProperty("是否删除，true删除，false未删除")
    private boolean del;
    @ApiModelProperty("创建时间")
    private  String create_time;
    @ApiModelProperty("创建人编码")
    private String create_code;
    @ApiModelProperty("创建人姓名")
    private String create_name;
    @ApiModelProperty("修改时间")
    private String update_time;
    @ApiModelProperty("修改人")
    private String update_code;
    private String label_condition;
    @ApiModelProperty("群组人数")
    private int person_count;
    @ApiModelProperty("性别")
    private List<String> sex;
    @ApiModelProperty("顾客年龄段")
    private List<Member_Age> age;
    @ApiModelProperty("顾客圈选地区")
    private List<Member_Area> areas;
    @ApiModelProperty("圈选顾客级别")
    private List<String> member_grade;
    @ApiModelProperty("活跃度")
    private List<String> actives;
    @ApiModelProperty("首次下单时间距离今天多少天")
    private int first_order_to_days;
    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;
    @ApiModelProperty("最后一次下单时间距离今天多少天")
    private int last_order_to_days;
    @ApiModelProperty("生日月份")
    private int member_month;
    @ApiModelProperty("是否有微信，1有，0没有")
    private int wechat;
    @ApiModelProperty("是否有qq，1有，0没有")
    private int qq;
    @ApiModelProperty("是否有邮箱，1有，0没有")
    private int email;
    @ApiModelProperty("获客媒体")
    private List<String> mideaList;
    @ApiModelProperty("是否为会员，1是，0不是")
    private int vip;
    @ApiModelProperty("是否拥有红包，1是，0不是，-1不统计")
    private int red_bag;
    @ApiModelProperty("是否拥有积分，1是，0不是，-1不统计")
    private int integral;
    @ApiModelProperty("是否拥有优惠券，1是，0不是，-1不统计")
    private int ticket;
    @ApiModelProperty("是否拥有储值金额，1是，0不是，-1不统计")
    private int recharge;
    @ApiModelProperty("方便接电话时间")
    private List<Integer> phone_time;
    @ApiModelProperty("性格偏好")
    private List<Integer> nature;
    @ApiModelProperty("响应时间")
    private List<Integer> response_time;
    @ApiModelProperty("坐席性格偏好：0男，1女")
    private List<Integer> staff_sex;
    @ApiModelProperty("支付方式：0=货到付款，1=款到发货 ,支付形式：1=银行卡 2=微信支付 3=支付宝支付 4=现金")
    private List<Integer> pay_type;
    @ApiModelProperty("支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款")
    private List<Integer> pay_form;
    @ApiModelProperty("签收时间截止今日，-1表示不做条件统计")
    private int sign_date_to_days;
    @ApiModelProperty("订单状态：0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4..物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成 9.拒收")
    private List<String> order_state;
    @ApiModelProperty("是否活动订单，1是，0否，-1不做统计查询")
    private int active_order;
    @ApiModelProperty("活动id")
    private List<String> active_ids;
    @ApiModelProperty("活动类型")
    private List<String> active_type;
    @ApiModelProperty("订单来源: 0=电销事业中心，1=OTC ，2=淘宝 ，3=京东 ，4=自建app")
    private List<Integer> order_source;
    @ApiModelProperty("商品Id")
    private List<Integer> product_nos;
    @ApiModelProperty("支付状态：1已支付，0未支付，-1表示不做统计查询")
    private int pay_state;
    @ApiModelProperty("物流状态，-1不做统计查询")
    private int logistics_state;
    @ApiModelProperty("物流公司")
    private int logistics_company_id;
    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;
    @ApiModelProperty("订单总金额")
    private int order_total_amount;
    @ApiModelProperty("订单应收金额")
    private int order_rec_amount;
    @ApiModelProperty("订单最高金额")
    private int order_high_am;
    @ApiModelProperty("订单最低金额")
    private int order_low_am;
    @ApiModelProperty("圈选病症")
    private List<Integer> disease_ids;


}
