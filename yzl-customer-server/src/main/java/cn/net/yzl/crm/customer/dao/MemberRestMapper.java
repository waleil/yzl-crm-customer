package cn.net.yzl.crm.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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

	/**
	 * 按顾客姓名查询顾客卡号列表
	 * 
	 * @param memberName 顾客姓名
	 * @return 顾客卡号列表
	 * @author zhangweiwei
	 * @date 2021年3月9日,下午1:26:16
	 */
	@Select("SELECT DISTINCT member_card FROM member WHERE member_name = #{memberName}")
	List<String> selectMemberCards(@Param("memberName") String memberName);
}
