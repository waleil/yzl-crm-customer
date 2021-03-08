package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberPhone;

import java.util.List;

public interface MemberPhoneService {
    /**
     * 保存客户电话号码
     * wangzhe
     * 2021-01-26
     * @param phoneNumber
     * @return
     */

    ComResponse<String> getMemberCardByphoneNumber(String phoneNumber);


    ComResponse<Member> getMemberByphoneNumber(String phoneNumber);

    List<MemberPhone> getMemberPhoneList(String member_card);
}
