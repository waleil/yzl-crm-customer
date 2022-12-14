package cn.net.yzl.crm.customer.service.amount;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;

import java.text.ParseException;
import java.util.List;

public interface MemberAmountService {

    // 获取 会员账户信息
    ComResponse<MemberAmountDto> getMemberAmount(String memberCard);
    // 获取账户明细
//    ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(String memberCard, Integer timeFlag) throws ParseException;

    ComResponse<String> operation(MemberAmountDetailVO memberAmountDetailVO);

    ComResponse<String> operationConfirm(int obtainType, String orderNo);

    public MemberAmountDetail getFrozenDetailByOrder(String orderNo, Integer obtainType);

    List<MemberAmountDetailDto> getMemberAmountDetailsBymemberCardAndOrderList(String memberCard, List<String> orderList);

    public Page<MemberAmountDetailDto> getMemberAmountDetailListByPage(String memberCard, Integer pageNo, Integer pageSize, Integer timeFlag) throws ParseException;

}
