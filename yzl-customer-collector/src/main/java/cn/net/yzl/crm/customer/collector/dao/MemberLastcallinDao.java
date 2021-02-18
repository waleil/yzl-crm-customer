package cn.net.yzl.crm.customer.collector.dao;

import cn.net.yzl.crm.customer.collector.model.MemberLastcallin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberLastcallinDao {
    List<MemberLastcallin> queryCallInByMemberCard(List<String> codes);
}