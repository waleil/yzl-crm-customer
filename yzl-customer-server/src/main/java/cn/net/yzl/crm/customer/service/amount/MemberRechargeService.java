package cn.net.yzl.crm.customer.service.amount;

import cn.net.yzl.crm.customer.model.db.MemberRecharge;

public interface MemberRechargeService {

    /**
     * 添加充值记录
     * wangzhe
     * 2021-03-18
     * @param memberRecharge
     * @return
     */
    public Integer addRecharge(MemberRecharge memberRecharge);

}
