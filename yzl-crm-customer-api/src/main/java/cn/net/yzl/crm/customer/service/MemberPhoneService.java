package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;

public interface MemberPhoneService {
    /**
     * 保存客户电话号码
     * wangzhe
     * 2021-01-26
     * @param phoneNumber
     * @return
     */
    ComResponse<String> getMemberCard(String phoneNumber);
}
