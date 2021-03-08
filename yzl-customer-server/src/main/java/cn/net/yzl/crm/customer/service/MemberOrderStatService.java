package cn.net.yzl.crm.customer.service;

import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;

public interface MemberOrderStatService {

    int insert(Member record);

    MemberOrderStat queryByMemberCode(String memberCard);


}
