package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.crm.customer.dao.MemberOrderStatMapper;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.service.MemberOrderStatService;
import cn.net.yzl.crm.customer.vo.member.MemberOrderStatUpdateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class memberOrderStatServiceImpl implements MemberOrderStatService {

    @Autowired
    MemberOrderStatMapper memberOrderStatMapper;


    @Override
    public int insert(Member record) {
        return 0;
    }

    @Override
    public MemberOrderStat queryByMemberCode(String memberCard) {
        MemberOrderStat orderStat = memberOrderStatMapper.queryByMemberCode(memberCard);
        if (orderStat == null) {
            orderStat = new MemberOrderStat();
            orderStat.setMemberCard(memberCard);
            synchronized(this){
                int result = memberOrderStatMapper.insertSelective(orderStat);
                if (result > 1) {
                    orderStat = memberOrderStatMapper.queryByMemberCode(memberCard);
                }
            }
        }
        return orderStat;
    }

    @Override
    public Boolean updateMemberOrderStatistics(List<MemberOrderStatUpdateVo> orderStatUpdateVos) {
        if (CollectionUtil.isEmpty(orderStatUpdateVos)) {
            return true;
        }
        long start = System.currentTimeMillis();
        log.info("updateMemberOrderQuotaTask:开始执行!,需要处理的数据为:{}条,当前时间为:{}",orderStatUpdateVos.size(),start);
        Map<String,List<MemberOrderStatUpdateVo>> statStatisticsVosMap = orderStatUpdateVos.stream()
                .collect(Collectors.groupingBy(MemberOrderStatUpdateVo::getMemberCard));

        Set<String> memberCardSet = statStatisticsVosMap.keySet();

        List<cn.net.yzl.crm.customer.model.db.MemberOrderStat> memberOrderStats = memberOrderStatMapper.queryByMemberCodes(new ArrayList<>(memberCardSet));
        if (CollectionUtil.isEmpty(memberOrderStats)) {
            return true;
        }
        Integer returnOrderNum = null;
        Integer totalOrderNum = null;
        Integer signCnt4TYear = null;

        for (cn.net.yzl.crm.customer.model.db.MemberOrderStat stat : memberOrderStats) {
            List<MemberOrderStatUpdateVo> vo = statStatisticsVosMap.get(stat.getMemberCard());

            returnOrderNum = vo.get(0).getRefundCnt();//退货数量
            totalOrderNum = vo.get(0).getTotalOrderCnt();//总订单数量，不包含取消订单和审批未通过
            signCnt4TYear = vo.get(0).getTotalSignCnt4TYear();//总签收数量，从当年一月一日开始按照签收日期确定
            if (returnOrderNum == null || totalOrderNum == null || returnOrderNum == 0 || totalOrderNum == 0){
                stat.setReturnGoodsRate(0);//退货率
            }else {
                BigDecimal returnRate = new BigDecimal(returnOrderNum).divide(new BigDecimal(totalOrderNum),4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(10000));
                stat.setReturnGoodsRate(returnRate.intValue());//退货率
            }
            if (signCnt4TYear == null || signCnt4TYear == 0) {
                stat.setYearAvgCount(0);//年度平均购买天数
            }else {
                BigDecimal yearAvgCount = new BigDecimal(365).divide(new BigDecimal(signCnt4TYear),0, BigDecimal.ROUND_HALF_UP);
                stat.setYearAvgCount(yearAvgCount.intValue());//年度平均购买天数
            }
        }
        //更新数据
        int result = memberOrderStatMapper.updateOrderStatQuota(memberOrderStats);
        long end = System.currentTimeMillis();
        log.info("updateMemberOrderQuotaTask:数据更新完成，一共更新:{}条数据,执行时长能够为:{}",result,(end-start));

        return true;
    }
}
