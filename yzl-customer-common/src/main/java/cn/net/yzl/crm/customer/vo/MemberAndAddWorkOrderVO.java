package cn.net.yzl.crm.customer.vo;

import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.vo.work.WorkOrderBeanVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberAndAddWorkOrderVO {

    @ApiModelProperty(value = "会员信息",name = "memberVO")
    Member memberVO;

    @ApiModelProperty(value = "工单信息",name = "workOrderBeanVO")
    WorkOrderBeanVO workOrderBeanVO;

}
