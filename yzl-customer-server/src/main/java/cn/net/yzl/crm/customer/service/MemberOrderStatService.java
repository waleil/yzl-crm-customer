package cn.net.yzl.crm.customer.service;

import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.vo.member.MemberOrderStatUpdateVo;

import java.util.List;

public interface MemberOrderStatService {

    int insert(Member record);

    MemberOrderStat queryByMemberCode(String memberCard);


    Boolean updateMemberOrderStatistics(List<MemberOrderStatUpdateVo> orderStatUpdateVos);

}
