package cn.net.yzl.crm.customer.service;

import cn.net.yzl.crm.customer.dto.MemberQuery;

/**
 * 顾客信息
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午8:31:21
 */
public interface MemberRestService {
	/**
	 * is_active=2：激活<br>
	 * member_status=1：正常<br>
	 * vip_flag=1：会员<br>
	 * 
	 * @param memberQuery {@link MemberQuery}
	 * @return 会员总数
	 * @author zhangweiwei
	 * @date 2021年2月5日,下午8:59:00
	 */
	Integer selectMemberCount(MemberQuery memberQuery);
}
