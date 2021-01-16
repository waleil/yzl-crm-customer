package cn.net.yzl.crm.customer.mongomodel;

import cn.net.yzl.crm.customer.annotations.FieldForMongo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@ApiModel("顾客圈选实体类")

@Document(collection="member_crowd_group")
public class member_crowd_group {

    @Indexed
    @FieldForMongo(PrimaryKey = "crowd_id")
    @ApiModelProperty("群组id")
    private String crowd_id;

    @ApiModelProperty("群组名称")
    private String crowd_name;

    @ApiModelProperty("群组描述")
    private String description;

    @ApiModelProperty("是否启用:0=否，1=是")
    private  Integer enable;

    @ApiModelProperty("生效时间")
    private Date effective_date;

    @ApiModelProperty("失效时间")
    private Date expire_date;

    @ApiModelProperty("是否删除，true删除，false未删除")
    private boolean del;

    @ApiModelProperty("创建时间")
    private Date create_time;

    @ApiModelProperty("创建人编码")
    private String create_code;


    @ApiModelProperty("创建人姓名")
    private String create_name;

    @ApiModelProperty("修改时间")
    private Date update_time;

    @ApiModelProperty("修改人")
    private String update_code;

    @ApiModelProperty("群组人数")
    private Integer person_count;

    @ApiModelProperty("性别： 0男，1女，-1不做条件判断")
    private Integer sex;

    @ApiModelProperty("顾客年龄段")
    private List<Member_Age> age;

    @ApiModelProperty("顾客圈选地区")
    private List<crowd_area> areas;

    @ApiModelProperty("圈选顾客级别")
    private List<crowd_base_value> member_grade;

    @ApiModelProperty("顾客类型")
    private List<crowd_member_type> member_type;

    @ApiModelProperty("活跃度")
    private List<crowd_activity_degree> active_degree;

    @ApiModelProperty("首次下单时间距离今天多少天")
    private Integer first_order_to_days;

    @ApiModelProperty(value = "真正首单金额")
    private Integer first_order_am;

    @ApiModelProperty("最后一次下单时间距离今天多少天")
    private Integer last_order_to_days;

    @ApiModelProperty("生日月份,1 一月份，2二月份，3三月份")
    private List<crowd_base_value> member_month;

    @ApiModelProperty("是否有微信，1有，0没有")
    private Integer wechat;

    @ApiModelProperty("是否有qq，1有，0没有")
    private Integer qq;

    @ApiModelProperty("是否有邮箱，1有，0没有")
    private Integer email;

    @ApiModelProperty("获客媒体")
    private List<crowd_media> mediaList;

    @ApiModelProperty("广告")
    private List<crowd_adver> advers;


    @ApiModelProperty("是否为会员，1是，0不是")
    private Integer vip;

    @ApiModelProperty("是否拥有红包，1是，0不是，-1不统计")
    private Integer red_bag;

    @ApiModelProperty("是否拥有积分，1是，0不是，-1不统计")
    private Integer integral;

    @ApiModelProperty("是否拥有优惠券，1是，0不是，-1不统计")
    private Integer ticket;

    @ApiModelProperty("是否拥有储值金额，1是，0不是，-1不统计")
    private Integer recharge;

    @ApiModelProperty("方便接电话时间")
    private List<crowd_action> phone_time;

    @ApiModelProperty("性格偏好")
    private List<crowd_action> nature;

    @ApiModelProperty("响应时间")
    private List<crowd_action> response_time;

    @ApiModelProperty("坐席性格偏好")
    private List<crowd_action> staff_sex;

    @ApiModelProperty("综合行为")
    private List<crowd_action> actions;

    @ApiModelProperty("下单行为")
    private List<crowd_action> order_action;

    @ApiModelProperty("活动偏好")
    private List<crowd_action> active_like;

    @ApiModelProperty("支付方式：0=货到付款，1=款到发货，-1不做条件判断")
    private List<crowd_base_value> pay_type;

    @ApiModelProperty("支付形式：0=快递待办，1=微信转账、2=支付宝转账、3=银行卡转账、4=客户账户扣款")
    private List<crowd_base_value> pay_form;

    @ApiModelProperty("签收时间截止今日，-1表示不做条件统计")
    private Integer sign_date_to_days;

    @ApiModelProperty("订单状态：0.话务待审核 1.话务未通过 2. 物流部待审核 3.物流部审核未通过  4..物流已审核 5.已退 6.部分退 7.订单已取消 8.订单已完成 9.拒收")
    private List<crowd_base_value> order_state;

    @ApiModelProperty("是否活动订单，1是，0否，-1不做统计查询")
    private Integer active_order;

    @ApiModelProperty("活动")
    private List<crowd_active> activeList;

    @ApiModelProperty("订单来源: 0=电销事业中心，1=OTC ，2=淘宝 ，3=京东 ，4=自建app")
    private List<crowd_base_value> order_source;

    @ApiModelProperty("商品")
    private List<crowd_product> products;

    @ApiModelProperty("支付状态：1已支付，0未支付，-1表示不做统计查询")
    private Integer pay_state;

    @ApiModelProperty("物流状态，-1不做统计查询")
    private List<crowd_base_value> logistics_state;

    @ApiModelProperty("物流公司")
    private List<crowd_base_value> logistics_company_id;

    @ApiModelProperty(value = "累计消费金额")
    private Integer total_amount;

    @ApiModelProperty("订单总金额")
    private Integer order_total_amount;

    @ApiModelProperty("订单应收金额")
    private Integer order_rec_amount;

    @ApiModelProperty("订单最高金额")
    private Integer order_high_am;

    @ApiModelProperty("订单最低金额")
    private Integer order_low_am;

    @ApiModelProperty("是否下单: 1是，0否，-1不做条件判断")
    private Integer have_order;

    @ApiModelProperty("圈选病症")
    private List<crowd_disease> diseases;

    public List<crowd_base_value> getLogistics_state() {
        return logistics_state;
    }

    public void setLogistics_state(List<crowd_base_value> logistics_state) {
        this.logistics_state = logistics_state;
    }

    public List<crowd_base_value> getLogistics_company_id() {
        return logistics_company_id;
    }

    public void setLogistics_company_id(List<crowd_base_value> logistics_company_id) {
        this.logistics_company_id = logistics_company_id;
    }

    public Date getEffective_date() {
        return effective_date;
    }

    public void setEffective_date(Date effective_date) {
        this.effective_date = effective_date;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public List<crowd_area> getAreas() {
        return areas;
    }

    public void setAreas(List<crowd_area> areas) {
        this.areas = areas;
    }

    public List<crowd_base_value> getOrder_source() {
        return order_source;
    }

    public List<crowd_member_type> getMember_type() {
        return member_type;
    }

    public void setMember_type(List<crowd_member_type> member_type) {
        this.member_type = member_type;
    }

    public List<crowd_activity_degree> getActive_degree() {
        return active_degree;
    }

    public void setActive_degree(List<crowd_activity_degree> active_degree) {
        this.active_degree = active_degree;
    }

    public List<crowd_adver> getAdvers() {
        return advers;
    }

    public void setAdvers(List<crowd_adver> advers) {
        this.advers = advers;
    }

    public List<crowd_base_value> getPay_type() {
        return pay_type;
    }

    public void setPay_type(List<crowd_base_value> pay_type) {
        this.pay_type = pay_type;
    }

    public List<crowd_base_value> getPay_form() {
        return pay_form;
    }

    public void setPay_form(List<crowd_base_value> pay_form) {
        this.pay_form = pay_form;
    }

    public List<crowd_base_value> getOrder_state() {
        return order_state;
    }

    public void setOrder_state(List<crowd_base_value> order_state) {
        this.order_state = order_state;
    }

    public String getCrowd_id() {
        return crowd_id;
    }

    public void setCrowd_id(String crowd_id) {
        this.crowd_id = crowd_id;
    }

    public String getCrowd_name() {
        return crowd_name;
    }

    public void setCrowd_name(String crowd_name) {
        this.crowd_name = crowd_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public String getCreate_code() {
        return create_code;
    }

    public void setCreate_code(String create_code) {
        this.create_code = create_code;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_code() {
        return update_code;
    }

    public void setUpdate_code(String update_code) {
        this.update_code = update_code;
    }

    public Integer getPerson_count() {
        return person_count;
    }

    public void setPerson_count(Integer person_count) {
        this.person_count = person_count;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public List<Member_Age> getAge() {
        return age;
    }

    public void setAge(List<Member_Age> age) {
        this.age = age;
    }

    public Integer getFirst_order_to_days() {
        return first_order_to_days;
    }

    public void setFirst_order_to_days(Integer first_order_to_days) {
        this.first_order_to_days = first_order_to_days;
    }

    public Integer getFirst_order_am() {
        return first_order_am;
    }

    public void setFirst_order_am(Integer first_order_am) {
        this.first_order_am = first_order_am;
    }

    public Integer getLast_order_to_days() {
        return last_order_to_days;
    }

    public void setLast_order_to_days(Integer last_order_to_days) {
        this.last_order_to_days = last_order_to_days;
    }

    public Integer getWechat() {
        return wechat;
    }

    public void setWechat(Integer wechat) {
        this.wechat = wechat;
    }

    public Integer getQq() {
        return qq;
    }

    public void setQq(Integer qq) {
        this.qq = qq;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public List<crowd_media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<crowd_media> mediaList) {
        this.mediaList = mediaList;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getRed_bag() {
        return red_bag;
    }

    public void setRed_bag(Integer red_bag) {
        this.red_bag = red_bag;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getRecharge() {
        return recharge;
    }

    public void setRecharge(Integer recharge) {
        this.recharge = recharge;
    }

    public List<crowd_action> getPhone_time() {
        return phone_time;
    }

    public void setPhone_time(List<crowd_action> phone_time) {
        this.phone_time = phone_time;
    }

    public List<crowd_action> getNature() {
        return nature;
    }

    public void setNature(List<crowd_action> nature) {
        this.nature = nature;
    }

    public List<crowd_action> getResponse_time() {
        return response_time;
    }

    public void setResponse_time(List<crowd_action> response_time) {
        this.response_time = response_time;
    }

    public List<crowd_action> getStaff_sex() {
        return staff_sex;
    }

    public void setStaff_sex(List<crowd_action> staff_sex) {
        this.staff_sex = staff_sex;
    }

    public List<crowd_action> getActions() {
        return actions;
    }

    public void setActions(List<crowd_action> actions) {
        this.actions = actions;
    }

    public List<crowd_action> getOrder_action() {
        return order_action;
    }

    public void setOrder_action(List<crowd_action> order_action) {
        this.order_action = order_action;
    }

    public List<crowd_action> getActive_like() {
        return active_like;
    }

    public void setActive_like(List<crowd_action> active_like) {
        this.active_like = active_like;
    }


    public Integer getSign_date_to_days() {
        return sign_date_to_days;
    }

    public void setSign_date_to_days(Integer sign_date_to_days) {
        this.sign_date_to_days = sign_date_to_days;
    }


    public Integer getActive_order() {
        return active_order;
    }

    public void setActive_order(Integer active_order) {
        this.active_order = active_order;
    }

    public List<crowd_active> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<crowd_active> activeList) {
        this.activeList = activeList;
    }


    public List<crowd_product> getProducts() {
        return products;
    }

    public void setProducts(List<crowd_product> products) {
        this.products = products;
    }

    public Integer getPay_state() {
        return pay_state;
    }

    public void setPay_state(Integer pay_state) {
        this.pay_state = pay_state;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getOrder_total_amount() {
        return order_total_amount;
    }

    public void setOrder_total_amount(Integer order_total_amount) {
        this.order_total_amount = order_total_amount;
    }

    public Integer getOrder_rec_amount() {
        return order_rec_amount;
    }

    public void setOrder_rec_amount(Integer order_rec_amount) {
        this.order_rec_amount = order_rec_amount;
    }

    public Integer getOrder_high_am() {
        return order_high_am;
    }

    public void setOrder_high_am(Integer order_high_am) {
        this.order_high_am = order_high_am;
    }

    public Integer getOrder_low_am() {
        return order_low_am;
    }

    public void setOrder_low_am(Integer order_low_am) {
        this.order_low_am = order_low_am;
    }

    public Integer getHave_order() {
        return have_order;
    }

    public void setHave_order(Integer have_order) {
        this.have_order = have_order;
    }

    public List<crowd_disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<crowd_disease> diseases) {
        this.diseases = diseases;
    }

    public List<crowd_base_value> getMember_grade() {
        return member_grade;
    }

    public void setMember_grade(List<crowd_base_value> member_grade) {
        this.member_grade = member_grade;
    }

    public List<crowd_base_value> getMember_month() {
        return member_month;
    }

    public void setMember_month(List<crowd_base_value> member_month) {
        this.member_month = member_month;
    }

    public void setOrder_source(List<crowd_base_value> order_source) {
        this.order_source = order_source;
    }
}
