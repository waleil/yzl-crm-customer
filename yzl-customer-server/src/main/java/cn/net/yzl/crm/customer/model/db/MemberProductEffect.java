package cn.net.yzl.crm.customer.model.db;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value="商品服用效果vo类",description="商品服用效果vo类" )
public class MemberProductEffect {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.id
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.member_card
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("会员卡号")
    private String memberCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.order_no
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("主键")
    private String orderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.product_code
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("商品编号")
    private String productCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.product_name
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.eating_time
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("每天吃多少")
    private Integer eatingTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.product_last_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("商品剩余量")
    private Integer productLastNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.due_date
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("商品服用完日期")
    private Date dueDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.taking_state
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("服用状态")
    private Integer takingState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.taking_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("服用效果")
    private Integer takingEffect;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.product_count
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("购买商品数")
    private Integer productCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.one_use_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("每次吃多少")
    private Integer oneUseNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member_product_effect.one_to_times
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @ApiModelProperty("每天吃几次")
    private Integer oneToTimes;

    @ApiModelProperty("修改人")
    private String updator;

    @ApiModelProperty("修改时间")
    private Date upateTime;






    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.id
     *
     * @return the value of member_product_effect.id
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.id
     *
     * @param id the value for member_product_effect.id
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.member_card
     *
     * @return the value of member_product_effect.member_card
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public String getMemberCard() {
        return memberCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.member_card
     *
     * @param memberCard the value for member_product_effect.member_card
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard == null ? null : memberCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.order_no
     *
     * @return the value of member_product_effect.order_no
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.order_no
     *
     * @param orderNo the value for member_product_effect.order_no
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.product_code
     *
     * @return the value of member_product_effect.product_code
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.product_code
     *
     * @param productCode the value for member_product_effect.product_code
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.product_name
     *
     * @return the value of member_product_effect.product_name
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public String getProductName() {
        return productName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.product_name
     *
     * @param productName the value for member_product_effect.product_name
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.eating_time
     *
     * @return the value of member_product_effect.eating_time
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getEatingTime() {
        return eatingTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.eating_time
     *
     * @param eatingTime the value for member_product_effect.eating_time
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setEatingTime(Integer eatingTime) {
        this.eatingTime = eatingTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.product_last_num
     *
     * @return the value of member_product_effect.product_last_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getProductLastNum() {
        return productLastNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.product_last_num
     *
     * @param productLastNum the value for member_product_effect.product_last_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setProductLastNum(Integer productLastNum) {
        this.productLastNum = productLastNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.due_date
     *
     * @return the value of member_product_effect.due_date
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.due_date
     *
     * @param dueDate the value for member_product_effect.due_date
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.taking_state
     *
     * @return the value of member_product_effect.taking_state
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getTakingState() {
        return takingState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.taking_state
     *
     * @param takingState the value for member_product_effect.taking_state
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setTakingState(Integer takingState) {
        this.takingState = takingState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.taking_effect
     *
     * @return the value of member_product_effect.taking_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getTakingEffect() {
        return takingEffect;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.taking_effect
     *
     * @param takingEffect the value for member_product_effect.taking_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setTakingEffect(Integer takingEffect) {
        this.takingEffect = takingEffect;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.product_count
     *
     * @return the value of member_product_effect.product_count
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getProductCount() {
        return productCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.product_count
     *
     * @param productCount the value for member_product_effect.product_count
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.one_use_num
     *
     * @return the value of member_product_effect.one_use_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getOneUseNum() {
        return oneUseNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.one_use_num
     *
     * @param oneUseNum the value for member_product_effect.one_use_num
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setOneUseNum(Integer oneUseNum) {
        this.oneUseNum = oneUseNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member_product_effect.one_to_times
     *
     * @return the value of member_product_effect.one_to_times
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public Integer getOneToTimes() {
        return oneToTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member_product_effect.one_to_times
     *
     * @param oneToTimes the value for member_product_effect.one_to_times
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    public void setOneToTimes(Integer oneToTimes) {
        this.oneToTimes = oneToTimes;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpateTime() {
        return upateTime;
    }

    public void setUpateTime(Date upateTime) {
        this.upateTime = upateTime;
    }
}