package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelationList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionDictList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberActionRelationMapper {

    int deleteByPrimaryKey(Integer id);

    int deleteBatch(@Param("list") List<MemberActionRelationDto> list);

    int deleteAllByCardNo(String cardNo);

    int insert(MemberActionRelationDto record);

    int insertBatch(@Param("list") List<MemberActionRelationDto> list);

    MemberActionRelation selectByPrimaryKey(Integer id);

    List<MemberActionRelation> selectRelationByMemberCardAndType(@Param("memberCard") String memberCard,
                                                                 @Param("type") Integer type );

    List<MemberActionRelation> selectRelationByMemberCardAndDid(@Param("memberCard") String memberCard,
                                                                 @Param("did") Integer did );

    List<MemberActionRelationList> selectRelationTreeByMemberCard(@Param("memberCard") String memberCard);

    List<MemberActionDictList> getActionDictByMemberCard(@Param("memberCard") String memberCard);

    List<MemberActionRelation> selectRelationByMemberCard(@Param("memberCard") String memberCard);

    int updateByPrimaryKey(MemberActionRelationDto record);

    List<cn.net.yzl.crm.customer.model.mogo.ActionDict> queryByMemberCodes(@Param("list") List<String> codes);

    int deleteMemberActionByMemberCardAndTypes(@Param("memberCard") String memberCard, @Param("list") List<Integer> types);

}