package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.model.db.ProductConsultation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductConsultationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    int insert(ProductConsultation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    int insertSelective(ProductConsultation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    ProductConsultation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    int updateByPrimaryKeySelective(ProductConsultation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_consultation
     *
     * @mbggenerated Mon Jan 25 17:10:49 CST 2021
     */
    int updateByPrimaryKey(ProductConsultation record);


    //根据会员卡号删除客户对应编号的商品
    int deleteByMemberCardAndProductCodes(String memberCard, @Param("list") List<String> list);
}