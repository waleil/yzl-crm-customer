package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;

public interface MemberOrderSignHandleService {


    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandle error);

}
