package cn.net.yzl.crm.customer.collector.dao;

import cn.net.yzl.crm.customer.collector.model.mogo.*;
import cn.net.yzl.crm.customer.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberMapper
 * @description 查询顾客信息
 * @date: 2021/1/29 7:51 下午
 */
@Mapper
public interface MemberMapper {

    List<MemberLabel> queryAllMemberByPage(@Param("lastOrderTime") String lastOrderTime,@Param("pageStart") int pageStart, @Param("pageSize")int pageSize);

    List<MemberLabel> queryMemberLabelByCodes(List<String> codes);

    List<MemberDisease> queryDiseaseByMemberCodes(List<String> codes);

    List<ActionDict> queryActionByMemberCodes(List<String> codes);

    List<MemberProduct> queryProductByMemberCodes(List<String> codes);

    Member selectMemberByCard(String memberCard);

    int updateByMemberCardSelective(Member dto);
}
