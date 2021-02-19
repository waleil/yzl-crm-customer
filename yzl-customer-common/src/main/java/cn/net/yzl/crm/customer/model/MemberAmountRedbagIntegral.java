package cn.net.yzl.crm.customer.model;

public class MemberAmountRedbagIntegral {
    private Integer id;

    private String memberCard;

    private Integer lastIntegral;

    private Integer lastRedBag;

    private Boolean isHistory;

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

    public Integer getLastIntegral() {
        return lastIntegral;
    }

    public void setLastIntegral(Integer lastIntegral) {
        this.lastIntegral = lastIntegral;
    }

    public Integer getLastRedBag() {
        return lastRedBag;
    }

    public void setLastRedBag(Integer lastRedBag) {
        this.lastRedBag = lastRedBag;
    }

    public Boolean getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Boolean isHistory) {
        this.isHistory = isHistory;
    }
}