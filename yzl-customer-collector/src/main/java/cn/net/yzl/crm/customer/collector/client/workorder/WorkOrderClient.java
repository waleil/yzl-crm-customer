package cn.net.yzl.crm.customer.collector.client.workorder;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.collector.model.MemberLastCallInDTO;
import cn.net.yzl.crm.customer.vo.work.WorkOrderBeanVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 智能工单
 */
//@FeignClient(name = "yzl-workorder-server")
@FeignClient(name = "workOrder", url = "${api.gateway.url}/workorderServer/")
public interface WorkOrderClient {

    @GetMapping("workOrder/v1/queryReturnVisitRules")
    ComResponse<Integer> queryReturnVisitRules();

    /**
     * 智能工单-回访规则校验第一条
     * @param memberCard
     * @return
     */
    @PostMapping(value = "workOrder/v1/productDosage")
    ComResponse<Boolean> productDosage(@RequestParam("memberCard") List<String> memberCard);


    @ApiOperation(value = "顾客最后一次进线，最后一次通话")
    @GetMapping("callManage/v1/getMemberLastCallInMessagesByMemberCard")
    ComResponse<MemberLastCallInDTO> getLastCallManageByMemberCard(@RequestParam("memberCard") String memberCard);



    @PostMapping(value = "workOrder/v1/add")
    ComResponse<Void> addWorkOrder(@RequestBody WorkOrderBeanVO workOrderBean);

}














