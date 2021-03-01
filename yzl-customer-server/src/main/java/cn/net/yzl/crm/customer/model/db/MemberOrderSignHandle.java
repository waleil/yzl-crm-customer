package cn.net.yzl.crm.customer.model.db;

import com.alibaba.druid.wall.violation.ErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MemberOrderSignHandle implements Serializable {

    private Integer id;

    private String memberCard;

    private String orderNo;

    private String orderData;

    private String creatorNo;

    private Date createTime;

    private String updatorNo;

    private Date updateTime;

    private String ErrorCode;

    private String errorMsg;

    private Integer status;

}