package cn.net.yzl.crm.customer.service.impl.amount;

import cn.net.yzl.crm.customer.dao.MemberRechargeMapper;
import cn.net.yzl.crm.customer.model.db.MemberRecharge;
import cn.net.yzl.crm.customer.service.amount.MemberRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberRechargeServiceImpl implements MemberRechargeService {
    @Autowired
    private MemberRechargeMapper memberRechargeMapper;


    @Override
    @Transactional
    public Integer addRecharge(MemberRecharge memberRecharge) {
        int result = memberRechargeMapper.insertSelective(memberRecharge);
        return result;
    }
}
