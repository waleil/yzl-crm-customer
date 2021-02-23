package cn.net.yzl.crm.customer.model.db;

import java.util.Date;

public class OrderSignHandleError {
    private Integer id;

    private Integer membercardno;

    private Integer orderno;

    private Integer status;

    private Date createTime;

    private String orderData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMembercardno() {
        return membercardno;
    }

    public void setMembercardno(Integer membercardno) {
        this.membercardno = membercardno;
    }

    public Integer getOrderno() {
        return orderno;
    }

    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData == null ? null : orderData.trim();
    }
}