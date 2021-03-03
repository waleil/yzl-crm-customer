package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MemberAmountDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAmountDetail record);

    int insertSelective(MemberAmountDetail record);

    MemberAmountDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAmountDetail record);

    int updateByPrimaryKey(MemberAmountDetail record);

    List<MemberAmountDetailDto> getMemberAmountDetailList(@Param("memberCard") String memberCard,@Param("now") Date now);

    MemberAmountDetail getByTypeAndOrder(@Param("obtainType") int obtainType,@Param("orderNo") String orderNo);

    @MapKey("obtainType")
    Map<Byte, MemberAmountDetail> getDetailByTypesAndOrder(@Param("orderNo") String orderNo,@Param("obtainTypes") List<Integer> obtainTypes,@Param("status") List<Integer> status);

    int updateAmountDetailStatus(@Param("oldStatus") int i,@Param("newStatus") int i1,@Param("orderNo")  String orderNo);



}