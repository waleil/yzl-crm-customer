package cn.net.yzl.crm.customer.model.db;

import java.util.Date;

public class ProductConsultation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_consultation.id
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_consultation.member_card
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    private String memberCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_consultation.product_code
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    private String productCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_consultation.product_name
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    private String productName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column product_consultation.consultation_time
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    private Date consultationTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_consultation.id
     *
     * @return the value of product_consultation.id
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_consultation.id
     *
     * @param id the value for product_consultation.id
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_consultation.member_card
     *
     * @return the value of product_consultation.member_card
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public String getMemberCard() {
        return memberCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_consultation.member_card
     *
     * @param memberCard the value for product_consultation.member_card
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard == null ? null : memberCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_consultation.product_code
     *
     * @return the value of product_consultation.product_code
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_consultation.product_code
     *
     * @param productCode the value for product_consultation.product_code
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_consultation.product_name
     *
     * @return the value of product_consultation.product_name
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public String getProductName() {
        return productName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_consultation.product_name
     *
     * @param productName the value for product_consultation.product_name
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column product_consultation.consultation_time
     *
     * @return the value of product_consultation.consultation_time
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public Date getConsultationTime() {
        return consultationTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column product_consultation.consultation_time
     *
     * @param consultationTime the value for product_consultation.consultation_time
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    public void setConsultationTime(Date consultationTime) {
        this.consultationTime = consultationTime;
    }
}