package cn.net.yzl.crm.customer.model.db;

import java.util.Date;

public class MemberOrderStat {
    private Integer id;

    private String memberCard;

    private Integer totalCounsumAmount;

    private Integer totalInvestAmount;

    private Integer totalOrderAmount;

    private Integer orderRecAmount;

    private Date firstOrderTime;

    private Date lastOrderTime;

    private String lastBuyProductCode;

    private String firstOrderStaffNo;

    private String firstOrderNo;

    private String firstBuyProductCode;

    private Integer firstOrderAm;

    private Integer orderHighAm;

    private Integer orderLowAm;

    private Integer orderAvgAm;

    private Integer productTypeCnt;

    private Integer buyCount;

    private Integer dayAvgCount;

    private Integer yearAvgCount;

    private Integer returnGoodsRate;

    private Date createTime;
    private Date lastSignTime;

    public Date getLastSignTime() {
        return lastSignTime;
    }

    public void setLastSignTime(Date lastSignTime) {
        this.lastSignTime = lastSignTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard == null ? null : memberCard.trim();
    }

    public Integer getTotalCounsumAmount() {
        return totalCounsumAmount;
    }

    public void setTotalCounsumAmount(Integer totalCounsumAmount) {
        this.totalCounsumAmount = totalCounsumAmount;
    }

    public Integer getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(Integer totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public Integer getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(Integer totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public Integer getOrderRecAmount() {
        return orderRecAmount;
    }

    public void setOrderRecAmount(Integer orderRecAmount) {
        this.orderRecAmount = orderRecAmount;
    }

    public Date getFirstOrderTime() {
        return firstOrderTime;
    }

    public void setFirstOrderTime(Date firstOrderTime) {
        this.firstOrderTime = firstOrderTime;
    }

    public Date getLastOrderTime() {
        return lastOrderTime;
    }

    public void setLastOrderTime(Date lastOrderTime) {
        this.lastOrderTime = lastOrderTime;
    }

    public String getLastBuyProductCode() {
        return lastBuyProductCode;
    }

    public void setLastBuyProductCode(String lastBuyProductCode) {
        this.lastBuyProductCode = lastBuyProductCode == null ? null : lastBuyProductCode.trim();
    }

    public String getFirstOrderStaffNo() {
        return firstOrderStaffNo;
    }

    public void setFirstOrderStaffNo(String firstOrderStaffNo) {
        this.firstOrderStaffNo = firstOrderStaffNo == null ? null : firstOrderStaffNo.trim();
    }

    public String getFirstOrderNo() {
        return firstOrderNo;
    }

    public void setFirstOrderNo(String firstOrderNo) {
        this.firstOrderNo = firstOrderNo == null ? null : firstOrderNo.trim();
    }

    public String getFirstBuyProductCode() {
        return firstBuyProductCode;
    }

    public void setFirstBuyProductCode(String firstBuyProductCode) {
        this.firstBuyProductCode = firstBuyProductCode == null ? null : firstBuyProductCode.trim();
    }

    public Integer getFirstOrderAm() {
        return firstOrderAm;
    }

    public void setFirstOrderAm(Integer firstOrderAm) {
        this.firstOrderAm = firstOrderAm;
    }

    public Integer getOrderHighAm() {
        return orderHighAm;
    }

    public void setOrderHighAm(Integer orderHighAm) {
        this.orderHighAm = orderHighAm;
    }

    public Integer getOrderLowAm() {
        return orderLowAm;
    }

    public void setOrderLowAm(Integer orderLowAm) {
        this.orderLowAm = orderLowAm;
    }

    public Integer getOrderAvgAm() {
        return orderAvgAm;
    }

    public void setOrderAvgAm(Integer orderAvgAm) {
        this.orderAvgAm = orderAvgAm;
    }

    public Integer getProductTypeCnt() {
        return productTypeCnt;
    }

    public void setProductTypeCnt(Integer productTypeCnt) {
        this.productTypeCnt = productTypeCnt;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getDayAvgCount() {
        return dayAvgCount;
    }

    public void setDayAvgCount(Integer dayAvgCount) {
        this.dayAvgCount = dayAvgCount;
    }

    public Integer getYearAvgCount() {
        return yearAvgCount;
    }

    public void setYearAvgCount(Integer yearAvgCount) {
        this.yearAvgCount = yearAvgCount;
    }

    public Integer getReturnGoodsRate() {
        return returnGoodsRate;
    }

    public void setReturnGoodsRate(Integer returnGoodsRate) {
        this.returnGoodsRate = returnGoodsRate ;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}