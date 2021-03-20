package cn.net.yzl.crm.customer.vo.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 工单表
 */
@Data
@ApiModel(value = "WorkOrderBeanVO",description = "保存转介绍用户的工单对象")
public class WorkOrderBeanVO implements Serializable {

    @Id
    @ApiModelProperty(value = "工单id")
    private String _id;

    @Indexed(unique = true)
    @ApiModelProperty(value = "工单号")
    private Integer code;

    @ApiModelProperty(value = "工单类型 1:热线工单，2回访工单")
    private Integer workOrderType;

    @ApiModelProperty(value = "员工编号（热线每日更新）")
    private String staffNo;

    @ApiModelProperty(value = "员工姓名（热线每日更新）")
    private String staffName;

    @ApiModelProperty(value = "员工级别（热线每日更新）")
    private String staffLevel;

    @ApiModelProperty(value = "媒体id")
    private Integer mediaId;

    @ApiModelProperty(value = "广告id")
    private Integer advId;

    @ApiModelProperty(value = "广告名称")
    private String advName;

    @ApiModelProperty(value = "媒体名称")
    private String mediaName;

    @ApiModelProperty(value = "顾客名称")
    private String memberName;

    @Indexed(unique = true)
    @ApiModelProperty(value = "顾客会员号")
    private String memberCard;

    @ApiModelProperty(value = "员工部门id（热线每日更新）")
    private Integer deptId;

    @ApiModelProperty(value = "部门名称（热线每日更新）")
    private String deptName;

    @ApiModelProperty(value = "工单类别：1:活动营销，2：常规回访，3:售后, 4：历史回访（回访工单才会有）")
    private Integer visitType;

    @ApiModelProperty(value = "是否是建档工单 0：否，1：是")
    private Integer isWorkOrder;

    @ApiModelProperty(value = "通话时间 （热线每日更新）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date callTime;

    @ApiModelProperty(value = "预约回访日期yyyy-MM-dd HH-mm-ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date visitDate;

    @ApiModelProperty(value = "主叫号码")
    private String callerPhone;

    @ApiModelProperty(value = "被叫号码")
    private String calledPhone;

    @ApiModelProperty(value = "座席已拨打次数 （热线每日更新）")
    private Integer callTimes;

    @ApiModelProperty(value = "调整次数 （热线每日更新）")
    private Integer transTimes;

    @ApiModelProperty(value = "原始工单id，用于追溯工单调整记录")
    private Integer rootId;

    @ApiModelProperty(value = "员工当日拨打状态1：已拨打，0：未拨打 （不管是热线还是回访都是次日改成未拨打）")
    private Integer callFlag;

    @ApiModelProperty(value = "上交状态 0:未上交，1：上交未通过，2:上交至经理，3：上交至待领取池")
    private Integer applyUpStatus;

    @ApiModelProperty(value = "上交原因 （热线每日更新）")
    private String applyUpMemo;

    @ApiModelProperty(value = "回拨截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date callBackDeadline;

    @ApiModelProperty(value = "分配时间 （热线每日更新）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allocateTime;

    @ApiModelProperty(value = "工单处理状态 1:未处理，2：已处理")
    private Integer status;

    @ApiModelProperty(value = "分配状态：0:未分配，1：自动分配，2：人工分配")
    private Integer allocateStatus;

    @ApiModelProperty(value = "工单接收状态 1:未接收，2：已接收")
    private Integer acceptStatus;

    @ApiModelProperty(value = "进线编号")
    private Integer callManageCode;

    @ApiModelProperty(value = "首单购买商品")
    private String firstBuyProductCode;

    @ApiModelProperty(value = "最后一次购买商品")
    private String lastBuyProductCode;

    @ApiModelProperty(value = "最后一次下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastOrderTime;

    @ApiModelProperty(value = "座席最后一次拨打时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastCallTime;

    @ApiModelProperty(value = "目标销售商品编码（最后一次进线对应的商品）")
    private String productCode;

    @ApiModelProperty(value = "目标销售商品名称（最后一次进线对应的商品）")
    private String productName;

    @ApiModelProperty(value = "1:可见，2：不可见")
    private Integer isVisiable;

    @ApiModelProperty(value = "会员级别（1：无卡，2：普卡，3：铜卡，4：银卡，5：金卡，6：钻卡，7：VIP，8：超级VIP）")
    private String mGradeCode;

    @ApiModelProperty(value = "活跃度 1：高，2：中，3：底")
    private Integer activity;

    @ApiModelProperty(value = "顾客购买产品种类个数")
    private Integer productTypeCnt;

    @ApiModelProperty(value = "顾客已购商品余量")
    private Integer productLastNum;

    @ApiModelProperty(value = "订购次数（回访）")
    private Integer orderNum;

    @ApiModelProperty(value = "工单成交状态 1：已成交，2：未成交")
    private Integer tradeStatus;

    @ApiModelProperty(value = "工单金额")
    private BigDecimal workOrderMoney;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    private String createId;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "修改人id")
    private String updateId;

    @ApiModelProperty(value = "修改人名称")
    private String updateName;

    @ApiModelProperty(value = "0:非历史数据 1:历史数据")
    private Integer historyFlag;

    @ApiModelProperty(value = "工单来源1：新分配的，2: 自取的（员工放弃自取顾客，7内自动回归待领取池），3自有的")
    private Integer souce;

    @ApiModelProperty(value = "顾客备注")
    private String memberMemo;

    /**
     * 新添加 字段，工单是否接受
     */
    @ApiModelProperty(value = "是否接受")
    private Integer accept;

}
