package cn.net.yzl.crm.customer.collector.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_grade_record
 * @author 
 */
@Data
public class MemberGradeRecordPo implements Serializable {
    private Integer id;

    /**
     * 顾客卡号
     */
    private String memberCard;

    /**
     * 会员级别id
     */
    private Integer mGradeId;

    /**
     * 会员级别名称
     */
    private String mGradeName;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;


    /**
     * 修改前的会员级别id
     */
    private Integer beforeGradeId;

    /**
     * 修改前的会员级别名称
     */
    private String beforeGradeName;
}