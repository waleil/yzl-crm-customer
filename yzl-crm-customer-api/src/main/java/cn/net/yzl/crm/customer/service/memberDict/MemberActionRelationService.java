package cn.net.yzl.crm.customer.service.memberDict;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;

import java.util.List;

public interface MemberActionRelationService {

    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCardAndType(String card, Integer type);

    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCard(String card);

    public ComResponse<Integer> addRelationWithDict(MemberActionRelationDto memberActionRelationDto);

    public ComResponse<Integer> addRelation(MemberActionRelationDto memberActionRelationDto);

    public ComResponse<Integer> deleteRelation(Integer rid);

    public ComResponse<Integer> saveUpdateRelation(List<MemberActionRelationDto> memberActionRelationDtoList);
}
