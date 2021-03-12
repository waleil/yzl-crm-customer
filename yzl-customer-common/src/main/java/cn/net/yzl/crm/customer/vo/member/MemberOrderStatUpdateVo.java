package cn.net.yzl.crm.customer.vo.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "MemberOrderStatStatisticsVo",description = "顾客消费能力统计")
public class MemberOrderStatUpdateVo {

    @ApiModelProperty(value = "会员卡号",required = true)
    private String memberCard;
    @ApiModelProperty(value = "退货数量",required = true)
    private Integer refundCnt;
    @ApiModelProperty(value = "总订单数量，不包含取消订单和审批未通过",required = true)
    private Integer totalOrderCnt;
    @ApiModelProperty(value = "总签收数量，从当年一月一日开始按照签收日期确定",required = true)
    private Integer totalSignCnt4TYear;
}
