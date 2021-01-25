package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberPhone;
import org.springframework.stereotype.Repository;

/**
 * @Author: lichanghong
 * @Description: 会员手机号接口
 * @Date: 2021/1/25 3:21 下午
 */
@Repository
public interface MemberPhoneMapper {

    int insert(MemberPhone record);

    int insertSelective(MemberPhone record);


    MemberPhone selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberPhone record);

    int updateByPrimaryKey(MemberPhone record);

}