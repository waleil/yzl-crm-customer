package cn.net.yzl.crm.customer.collector.dao;


import cn.net.yzl.crm.customer.collector.model.mogo.MemberOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMDao {

    List<MemberOrder> queryOrderByMemberCard(List<String> codes);
}