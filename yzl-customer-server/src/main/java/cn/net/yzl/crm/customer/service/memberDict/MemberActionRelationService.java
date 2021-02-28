package cn.net.yzl.crm.customer.service.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelationList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionDictList;

import java.util.List;

public interface MemberActionRelationService {

    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCardAndType(String card, Integer type);

    public ComResponse<List<MemberActionRelationList>> selectRelationTreeByMemberCard(String card);

    public ComResponse<List<MemberActionDictList>> getActionDictByMemberCard(String memberCard);

    public ComResponse<Integer> addRelationWithDict(MemberActionRelationDto memberActionRelationDto);

    public ComResponse<Integer> addRelation(MemberActionRelationDto memberActionRelationDto);

    public ComResponse<Integer> deleteRelation(Integer rid);

    public ComResponse<Integer> saveUpdateRelation(List<MemberActionRelationDto> memberActionRelationDtoList);

    public ComResponse<Boolean> saveOrUpdateMemberActionRelation(String memberCard,String createNo,List<Integer> memberActionDIdList);
}
