package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;

import java.util.List;

public interface MemberAddressService {
    ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard);

    ComResponse<String> addReveiverAddress(ReveiverAddressInsertVO reveiverAddressInsertVO);

    ComResponse<String> updateReveiverAddress(ReveiverAddressUpdateVO reveiverAddressUpdateVO);
}
