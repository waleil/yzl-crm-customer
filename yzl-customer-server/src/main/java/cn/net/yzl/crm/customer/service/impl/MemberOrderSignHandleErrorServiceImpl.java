package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberOrderSignHandleErrorMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressRecordDao;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandleError;
import cn.net.yzl.crm.customer.model.db.ReveiverAddress;
import cn.net.yzl.crm.customer.model.db.ReveiverAddressRecordPo;
import cn.net.yzl.crm.customer.service.MemberOrderSignHandleErrorService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberOrderSignHandleErrorServiceImpl implements MemberOrderSignHandleErrorService {

    @Autowired
    private MemberOrderSignHandleErrorMapper memberOrderSignHandleErrorMapper;

    @Override
    @Transactional
    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandleError error) {
        int insert = memberOrderSignHandleErrorMapper.insert(error);
        if (insert < 1) {
            ComResponse.success(Boolean.FALSE);
        }
        return ComResponse.success(Boolean.TRUE);
    }
}
