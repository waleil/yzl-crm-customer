package cn.net.yzl.crm.customer.dao;

import org.apache.ibatis.annotations.Select;

import cn.net.yzl.crm.customer.dto.MemberQuery;

/**
 * 顾客信息
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午8:34:59
 */
public interface MemberRestMapper {
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
	@Select("SELECT COUNT(1) FROM member WHERE is_active=2 AND member_status=1 AND vip_flag=1 AND last_order_time>=#{orderTimeFrom} AND last_order_time<=#{orderTimeTo}")
	Integer selectMemberCount(MemberQuery memberQuery);
}
