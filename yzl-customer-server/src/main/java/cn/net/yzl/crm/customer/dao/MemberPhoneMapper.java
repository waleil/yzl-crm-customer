package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberPhone;
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
     * @param phoneNumbers
     * @return
     */
    String getMemberCardByPhoneNumber(@Param("list") List phoneNumbers);

    List<MemberPhone> getMemberPhoneByMemberCard(@Param("memberCard") String memberCard);


    List<MemberPhone> getMemberPhoneList(String member_card);

}