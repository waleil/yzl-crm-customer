package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.MemberProductEffect;
import cn.net.yzl.crm.customer.model.mogo.MemberProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberProductEffectMapper {


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_product_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    int insert(MemberProductEffect record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_product_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    int insertSelective(MemberProductEffect record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_product_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    MemberProductEffect selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_product_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    int updateByPrimaryKeySelective(MemberProductEffect record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member_product_effect
     *
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    int updateByPrimaryKey(MemberProductEffect record);

    List<MemberProduct> queryByMemberCodes(List<String> codes);
}