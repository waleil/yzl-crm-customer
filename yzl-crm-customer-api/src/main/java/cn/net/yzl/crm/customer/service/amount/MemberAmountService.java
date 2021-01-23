package cn.net.yzl.crm.customer.service.amount;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;

import java.text.ParseException;
import java.util.List;

public interface MemberAmountService {

    // 获取 会员账户信息
    ComResponse<MemberAmountDto> getMemberAmount(String memberCard);
    // 获取账户明细
    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(String memberCard, Integer timeFlag) throws ParseException;

    ComResponse<String> edit(MemberAmountDetailVO memberAmountDetailVO);
}
