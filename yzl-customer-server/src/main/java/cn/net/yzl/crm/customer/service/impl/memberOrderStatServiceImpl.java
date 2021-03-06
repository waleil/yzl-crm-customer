package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.MemberOrderStatMapper;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.service.MemberOrderStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
