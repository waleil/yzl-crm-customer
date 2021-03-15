package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.feign.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.customer.model.MemberLastCallInDTO;
import cn.net.yzl.crm.customer.model.MemberOrderObject;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 订单相关接口
 * wangzhe
 * 2021-02-26
 */
@Component
@Slf4j
public class WorkOrderClientAPI {

    @Autowired
    WorkOrderClient workOrderClient;

    public static WorkOrderClientAPI workOrderClientAPI;

    public void setWorkOrderClient(WorkOrderClient workOrderClient) {
        this.workOrderClient = workOrderClient;
    }

    @PostConstruct
    public void init() {
        workOrderClientAPI = this;
        workOrderClientAPI.workOrderClient = this.workOrderClient;
    }

    /**
     * 顾客最后一次进线，最后一次通话
     * wangzhe
     * 2021-02-26
     *
     * @param memberCard
     * @return
     */
    public static MemberLastCallInDTO getLastCallManageByMemberCard(String memberCard) {
        MemberLastCallInDTO data = null;
        try {
            ComResponse<MemberLastCallInDTO> response = workOrderClientAPI.workOrderClient.getLastCallManageByMemberCard(memberCard);
            if (response != null && response.getCode() == 200) {
                data = response.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    /**
     * 查询出距离商品服用完日期不足多少天
     * @return
     */
    public static Integer queryReturnVisitRules(){
        //查询配置规则
        ComResponse<Integer> rules = null;
        try {
            rules = workOrderClientAPI.workOrderClient.queryReturnVisitRules();
        } catch (Exception e) {
            log.error("update member product last num:查询配置规则异常：{}",e);
        }
        Integer configDay = rules == null || rules.getData() == null ? Integer.MIN_VALUE : rules.getData();
        return configDay;
    }

    public static boolean productDosage(List<String> memberCardList){
        try {
            ComResponse<Boolean> response = workOrderClientAPI.workOrderClient.productDosage(memberCardList);
            if (response.getCode() == 200) {
                return true;
            }else{
                log.error("创建工单失败：错误码:{},错误信息:{}",response.getCode(),response.getMessage());
            }
        } catch (Exception e) {
            log.error("创建工单异常：异常信息:{}",e);
        }
        return false;
    }


}
