package cn.net.yzl.crm.customer.service.memberDict;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;

import java.util.List;


public interface ActionDictService {

    public ComResponse<List<ActionDict>> getDictListByType(Integer type);

    public ComResponse<Integer> saveUpdateActionDict(List<ActionDictDto> actionDictDtos);

    Integer updateActionDictWhereStatusIs2();
}
