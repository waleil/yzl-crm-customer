package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.net.yzl.activity.model.requestModel.SendCouponByMemberLevelRequest;
import cn.net.yzl.activity.model.responseModel.ActivityDetailResponse;
import cn.net.yzl.activity.model.responseModel.MemberAccountResponse;
import cn.net.yzl.activity.model.responseModel.MemberLevelPagesResponse;
import cn.net.yzl.activity.model.responseModel.MemberSysParamDetailResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.feign.client.Activity.ActivityFien;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.feign.model.MemberGradeValidDate;
import cn.net.yzl.crm.customer.model.MemberOrderObject;
import cn.net.yzl.crm.customer.model.PageParam;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 订单相关接口
 * wangzhe
 * 2021-02-26
 */
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
     * 获取今年的会员到期时间
     * wangzhe
     * 2021-02-26
     * @return
     */
    /*public static String getMemberGradeValidDate(){
        ComResponse<MemberSysParamDetailResponse> response = null;
        for (int i = 0; i < 3; i++) {
            try {
                response = activityClientAPI.activityFien.getMemberSysParamByType(0);//会员
                if (response.getCode() == 200) {
                    break;
                }
            } catch (Exception e) {
                log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!",e);
            }
        }
        if (response == null || response.getCode() != 200) {
            log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!");
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),"activityFien:getMemberGradeValidDate:获取DMC会员级别管理接口异常!");
        }
        MemberSysParamDetailResponse data = response.getData();
        if (data == null) {
            log.info("getMemberSysParamByType:DMC返回数据为空！");
            return "";
        }

        if (data.getValidityType() == null || data.getValidityType() == 0) {
            log.info("getMemberSysParamByType:会员有效期类型为长期有效！");
            return "";
        }

        if (StringUtils.isEmpty(data.getValidityMonth()) || StringUtils.isEmpty(data.getValidityDay())){
            log.error("getMemberSysParamByType:接口参数日期为空:{}-{}",data.getValidityMonth(),data.getValidityDay());
            throw new BizException(ResponseCodeEnums.API_ERROR_CODE.getCode(),"activityFien:getMemberGradeValidDate:获取DMC的数据异常!");
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
    }*/

    /**
     * 获取DMC会员有效期配置
     * wangzhe
     * 2021-03-12
     * @return
     */
    public static MemberGradeValidDate getMemberGradeValidDateObj(){
        String valid = "";
        MemberGradeValidDate validDateObj = new MemberGradeValidDate();
        validDateObj.setIsAlways(null);
        try {
            ComResponse<MemberSysParamDetailResponse> response = null;
            for (int i = 0; i < 3; i++) {
                try {
                    response = activityClientAPI.activityFien.getMemberSysParamByType(0);//会员
                    if (response.getCode() == 200) {
                        break;
                    }
                } catch (Exception e) {
                    log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!",e);
                }
            }
            if (response == null || response.getCode() != 200) {
                log.error("getMemberSysParamByType:获取DMC会员级别管理接口异常!");
            }
            MemberSysParamDetailResponse data = response.getData();
            if (data == null) {
                log.info("getMemberSysParamByType:DMC返回数据为空！");
                validDateObj.setIsAlways(true);//请求成功，返回数据为空，则为长期有效
                return validDateObj;
            }

            if (data.getValidityType() == null || data.getValidityType() == 0) {
                log.info("getMemberSysParamByType:会员有效期类型为长期有效！");
                validDateObj.setIsAlways(true);
                return validDateObj;
            }

            if (StringUtils.isEmpty(data.getValidityMonth()) || StringUtils.isEmpty(data.getValidityDay())) {
                log.error("getMemberSysParamByType:接口参数日期为空:{}-{}", data.getValidityMonth(), data.getValidityDay());
            } else {
                //获取会员到期的年月日，格式：yyyy-MM-dd
                if (data.getValidityMonth().length() == 1) {
                    data.setValidityMonth("0" + data.getValidityMonth());
                }
                if (data.getValidityDay().length() == 1) {
                    data.setValidityDay("0" + data.getValidityDay());
                }
                valid = DateUtil.year(DateUtil.date()) + "-" + data.getValidityMonth() + "-" + data.getValidityDay();
                validDateObj.setIsAlways(false);


                Date startDate = null;
                Date endDate = null;
                //格式化会员到期时间
                Date validDate = DateUtil.parse(valid, "yyyy-MM-dd");

                //当前日期字符串，格式：yyyy-MM-dd
                String today= DateUtil.today();
                Date todayDate = DateUtil.parse(today);
                long between = DateUtil.between(todayDate, validDate, DateUnit.DAY, false);
                if (between <= 0) {
                    startDate = DateUtil.offsetDay(validDate, 1);
                    endDate = DateUtil.endOfDay(DateUtil.date());//一天的结束，结果：2017-03-01 23:59:59
                }else {
                    //没过:到期时间一年前 到今天
                    Calendar c = Calendar.getInstance();
                    c.setTime(validDate);
                    c.add(Calendar.YEAR, -1);
                    c.add(Calendar.DATE,1);
                    startDate = DateUtil.beginOfDay(c.getTime());//一年前 + 1天
                    //今天的结束，结果：2017-03-01 23:59:59
                    endDate = DateUtil.endOfDay(DateUtil.date());//今天
                }
                validDateObj.setCurrentYearValidDate(validDate);
                validDateObj.setStartDate(startDate);
                validDateObj.setEndDate(endDate);
            }
        } catch (Exception e) {
            log.error("getMemberSysParamByType:DMC获取会员有效期接口异常",e);
        }
        return validDateObj;
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

    /**
     * 获取会员级别
     * wangzhe
     * 2021-02-26
     * @return
     */
    public static List<MemberLevelPagesResponse> getMemberLevelList(){
        List<MemberLevelPagesResponse> dmcLevelData = null;
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(1);pageParam.setPageSize(20);
        try {
            ComResponse<Page<MemberLevelPagesResponse>> dmcLevelResponse = activityClientAPI.activityFien.getMemberLevelPages(pageParam);
            Page<MemberLevelPagesResponse> data = dmcLevelResponse.getData();
            if (dmcLevelResponse != null && data != null && CollectionUtil.isNotEmpty(data.getItems())) {
                dmcLevelData = data.getItems();
                //等级倒叙排序
                Collections.sort(dmcLevelData, new Comparator<MemberLevelPagesResponse>() {
                    public int compare(MemberLevelPagesResponse o1, MemberLevelPagesResponse o2) {
                        return o2.getMemberLevelGrade() - o1.getMemberLevelGrade();
                    }
                });
            }
            return dmcLevelData;
        } catch (Exception e) {
            log.error("activityFienget:MemberLevelPages:" + e.getMessage());
        }
        return dmcLevelData;
    }

    /**
     * 从DMC获取:是否有积分、红包、优惠券
     * wangzhe
     * 2021-02-26
     * @param memberCard 会员卡号
     * @return
     */
    public static MemberAccountResponse getAccountByMemberCard(String memberCard){
        MemberAccountResponse data = null;
        try {
            ComResponse<MemberAccountResponse> response = activityClientAPI.activityFien.getAccountByMemberCard(memberCard);
            if (response != null && response.getCode() == 200) {
                data = response.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     *
     * @param memberCard 顾客卡号
     * @param mGradeId 会员级别id
     * wangzhe
     * 2021-03-27
     * @return
     */
    public static Boolean sendCouponByMemberLevel(String memberCard,Integer mGradeId){
        Boolean result = false;
        try {
            SendCouponByMemberLevelRequest request = new SendCouponByMemberLevelRequest();
            request.setMemberCard(memberCard);
            request.setMemberLevelGrade(mGradeId);
            ComResponse<Boolean> response = activityClientAPI.activityFien.sendCouponByMemberLevel(request);
            if (response != null && response.getCode() == 200) {
                result = true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return result;
    }



}
