package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressMapper;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.ReveiverAddress;
import cn.net.yzl.crm.customer.service.MemberAddressService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberAddressServiceImpl implements MemberAddressService {

    @Autowired
    private ReveiverAddressMapper reveiverAddressMapper;

    @Override
    public ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard) {

        List<ReveiverAddressDto> list = reveiverAddressMapper.getReveiverAddressByMemberCard(memberCard);
        if(list==null || list.size()<1){
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }
@Autowired
private MemberMapper memberMapper;
    @Override
    @Transactional
    public ComResponse<String> addReveiverAddress(ReveiverAddressInsertVO reveiverAddressInsertVO) {

        // 判断顾客编号是否存在
        String memberCard = reveiverAddressInsertVO.getMemberCard();
        Member member = memberMapper.selectMemberByCard(memberCard);
        if(member==null){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"会员不存在");
        }
        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressInsertVO,reveiverAddress);
        int num = reveiverAddressMapper.insertSelective(reveiverAddress);
        if(num<1){
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),"数据保存失败");
        }

        return ComResponse.success();
    }

    @Override
    @Transactional
    public ComResponse<String> updateReveiverAddress(ReveiverAddressUpdateVO reveiverAddressUpdateVO) {
        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressUpdateVO,reveiverAddress);
        int num = reveiverAddressMapper.updateByPrimaryKeySelective(reveiverAddress);
        if(num<1){
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(),"数据更新失败");
        }
        return ComResponse.success();
    }
}
