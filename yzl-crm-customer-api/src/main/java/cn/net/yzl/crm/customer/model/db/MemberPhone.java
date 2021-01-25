package cn.net.yzl.crm.customer.model.db;

import java.util.Date;

public class MemberPhone {
    private Integer id;

    private String memberCard;

    private String phoneNumber;

    private String phonePlace;

    private Boolean serviceProvider;

    private Boolean phoneType;

    private Boolean enabled;

    private String creatorNo;

    private Date createTime;

    private String updatorNo;

    private Date updateTime;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getPhonePlace() {
        return phonePlace;
    }

    public void setPhonePlace(String phonePlace) {
        this.phonePlace = phonePlace == null ? null : phonePlace.trim();
    }

    public Boolean getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(Boolean serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Boolean getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Boolean phoneType) {
        this.phoneType = phoneType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatorNo() {
        return creatorNo;
    }

    public void setCreatorNo(String creatorNo) {
        this.creatorNo = creatorNo == null ? null : creatorNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdatorNo() {
        return updatorNo;
    }

    public void setUpdatorNo(String updatorNo) {
        this.updatorNo = updatorNo == null ? null : updatorNo.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}