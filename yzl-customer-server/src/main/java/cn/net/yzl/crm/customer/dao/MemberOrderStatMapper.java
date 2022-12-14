package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: lichanghong
 * @Description: 会员订单统计表
 * @Date: 2021/1/25 3:21 下午
 */
@Repository
public interface MemberOrderStatMapper {

    int insertSelective(MemberOrderStat record);

    int updateByPrimaryKeySelective(MemberOrderStat record);
    /**
     * @Author: lichanghong
     * @Description:    根据顾客订单查询相关订单信息
     * @Date: 2021/1/25 3:31 下午
     * @param codes
     * @Return: java.util.List<cn.net.yzl.crm.customer.model.db.MemberOrderStat>
     */
    List<MemberOrderStat> queryByMemberCodes(List<String> codes);

    MemberOrderStat queryByMemberCode(@Param("memberCard") String memberCard);

    //List<MemberOrderStat> scanByPage(@Param("limit") int limit,@Param("pageSize") int pageSize);

    List<MemberOrderStat> scanByPage(@Param("pageSize") Integer pageSize,@Param("fromIndex") Integer fromIndex);

    int updateOrderStatQuota(@Param("list") List<MemberOrderStat> statList);
}