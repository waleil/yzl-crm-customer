package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.activity.model.responseModel.MemberSysParamDetailResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.feign.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ActivityClientAPI {

    @Autowired
    ActivityFien activityFien;

    private static ActivityClientAPI activityClientAPI;

    public void setActivityFien(ActivityFien activityFien) {
        this.activityFien = activityFien;
    }

    @PostConstruct
    public void init() {
        activityClientAPI = this;
        activityClientAPI.activityFien = this.activityFien;
    }







    /**
     * 获取会员到期时间
     * @return
     */
    public static String getMemberSysParamByType(){
        ComResponse<MemberSysParamDetailResponse> response = null;
        for (int i = 0; i < 3; i++) {
            response = activityClientAPI.activityFien.getMemberSysParamByType(0);//会员
            if (200 != response.getCode()) {
                log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!");
                throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),"getMemberSysParamByType:获取DMC会员级别管理接口异常!");
            }else{
                break;
            }
        }
        if (response == null ||  response.getData() == null || 200 != response.getCode()) {
            log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!");
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),"getMemberSysParamByType:获取DMC会员级别管理接口异常!");
        }
        MemberSysParamDetailResponse data = response.getData();

        if (data.getValidityType() == null || data.getValidityType() == 0) {
            log.info("getMemberSysParamByType:会员有效期类型为长期有效！");
            return "";
        }

        if (StringUtils.isEmpty(data.getValidityMonth()) || StringUtils.isEmpty(data.getValidityDay())){
            log.error("getMemberSysParamByType:接口参数日期为空:{}-{}",data.getValidityMonth(),data.getValidityDay());
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),"getMemberSysParamByType:获取DMC的数据异常!");
        }

        //获取会员到期的年月日，格式：yyyy-MM-dd
        if (data.getValidityMonth().length() == 1){
            data.setValidityMonth("0" + data.getValidityMonth());
        }
        if (data.getValidityDay().length() == 1){
            data.setValidityDay("0" + data.getValidityDay());
        }
        String valid = DateUtil.year(DateUtil.date()) + "-" + data.getValidityMonth() + "-" + data.getValidityDay();
        return valid;
    }

    /**
     * 通过多个活动编号获取活动商品
     * wangzhe
     * 2021-02-26
     * @param activityBusNos
     * @return
     */
    public static List<ActivityDetailResponse> getActivityProductByBusNos(List<Integer> activityBusNos){
        List<ActivityDetailResponse> data = null;
        try {
            ComResponse<List<ActivityDetailResponse>> response = activityClientAPI.activityFien.getListByBusNos(activityBusNos);
            if (response.getCode() == 200) {
                data = response.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 通过活动编号获取活动商品
     * wangzhe
     * 2021-02-26
     * @param activityBusNo
     * @return
     */
    public static ActivityDetailResponse getActivityProductByBusNo(Integer activityBusNo){
        List<ActivityDetailResponse> data = getActivityProductByBusNos(Arrays.asList(activityBusNo));
        return CollectionUtil.isEmpty(data) ? null : data.get(0);
    }

}
