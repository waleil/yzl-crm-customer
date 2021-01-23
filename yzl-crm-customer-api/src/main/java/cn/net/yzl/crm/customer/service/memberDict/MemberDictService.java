package cn.net.yzl.crm.customer.service.memberDict;


import cn.net.yzl.crm.customer.dto.member.AgeDictDto;
import cn.net.yzl.crm.customer.dto.member.ContactTimeDictDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MemberDictService {

    public int memberAgeSaveUpdate(List<AgeDictDto> ageDictDtos);

    public int memberContactTimeSaveUpdate(List<ContactTimeDictDto> ageDictDtos);
}
