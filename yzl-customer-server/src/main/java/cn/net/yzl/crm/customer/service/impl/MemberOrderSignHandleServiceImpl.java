package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dao.MemberOrderSignHandleMapper;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;
import cn.net.yzl.crm.customer.service.MemberOrderSignHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberOrderSignHandleServiceImpl implements MemberOrderSignHandleService {

    @Autowired
    private MemberOrderSignHandleMapper memberOrderSignHandleMapper;

    @Override
    @Transactional
    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandle error) {
        int insert = memberOrderSignHandleMapper.insert(error);
        if (insert < 1) {
            ComResponse.success(Boolean.FALSE);
        }
        return ComResponse.success(Boolean.TRUE);
    }
}
