package cn.net.yzl.crm.customer.service.memberDict;

import cn.net.yzl.crm.customer.dto.member.MemberAgeRelationDto;
import cn.net.yzl.crm.customer.dto.member.MemberContactTimeRelationDto;

import java.util.List;

public interface MemberRelationService {

    public int memberAgeRelationSaveUpdate(List<MemberAgeRelationDto> memberAgeRelationDto);

    public int memberContactTimeRelationSaveUpdate(List<MemberContactTimeRelationDto> memberContactTimeRelationDto);

}
