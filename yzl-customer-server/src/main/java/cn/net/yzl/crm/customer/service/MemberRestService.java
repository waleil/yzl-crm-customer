package cn.net.yzl.crm.customer.service;

import java.util.List;

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

	/**
	 * 按顾客姓名查询顾客卡号列表
	 * 
	 * @param memberName 顾客姓名
	 * @return 顾客卡号列表
	 * @author zhangweiwei
	 * @date 2021年3月9日,下午1:26:16
	 */
	List<String> selectMemberCards(String memberName);
}
