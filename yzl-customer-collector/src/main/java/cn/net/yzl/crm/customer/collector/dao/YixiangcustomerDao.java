package cn.net.yzl.crm.customer.collector.dao;

import cn.net.yzl.crm.customer.collector.model.Yixiangcustomer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lichanghong
 */
@Mapper
public interface YixiangcustomerDao {

    List<Yixiangcustomer> queryAllByPage(int id,int pageSize);


    List<Yixiangcustomer> queryByMemberCard(List<String> codes);

}