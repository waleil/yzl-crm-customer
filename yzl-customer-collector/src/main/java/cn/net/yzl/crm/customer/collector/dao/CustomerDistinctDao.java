package cn.net.yzl.crm.customer.collector.dao;

import cn.net.yzl.crm.customer.collector.model.CustomerDistinct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: lichanghong
 * @Description: 同步数据
 * @Date: 2021/2/1 1:47 下午
 */
@Mapper
public interface CustomerDistinctDao {
    List<CustomerDistinct> queryAllByIdPage(int id,int pageSize);
}