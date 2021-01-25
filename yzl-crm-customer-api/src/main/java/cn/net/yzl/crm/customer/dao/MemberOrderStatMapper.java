package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.model.db.MemberOrderStatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
/**
 * @Author: lichanghong
 * @Description: 会员订单统计表
 * @Date: 2021/1/25 3:21 下午
 */

public interface MemberOrderStatMapper {

    int insertSelective(MemberOrderStat record);

    int updateByPrimaryKeySelective(MemberOrderStat record);

}