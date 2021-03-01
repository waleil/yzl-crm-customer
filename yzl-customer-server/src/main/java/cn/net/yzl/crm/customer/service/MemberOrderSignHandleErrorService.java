package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandleError;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;

public interface MemberOrderSignHandleErrorService {


    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandleError error);

}
