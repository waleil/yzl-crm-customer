package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberPhone;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据电话号查询会员号
     * wangzhe
     * 2021-01-26
     * @param phoneNumber
     * @return
     */
    String getMemberCardByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}