package cn.net.yzl.crm.customer.model.db;

import com.alibaba.druid.wall.violation.ErrorCode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MemberOrderSignHandleError implements Serializable {

    private Integer id;

    private String memberCard;

    private String orderNo;

    private String orderData;

    private String createNo;

    private Date createTime;

    private String ErrorCode;

    private String errorMsg;

    private Integer status;

}