package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressRecordDao;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.ReveiverAddress;
import cn.net.yzl.crm.customer.model.db.ReveiverAddressRecordPo;
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
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ReveiverAddressRecordDao reveiverAddressRecordDao;

    @Override
    public ComResponse<List<ReveiverAddressDto>> getReveiverAddress(String memberCard) {

        List<ReveiverAddressDto> list = reveiverAddressMapper.getReveiverAddressByMemberCard(memberCard);
        if (list == null || list.size() < 1) {
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }



    @Override
    @Transactional
    public ComResponse<String> addReveiverAddress(ReveiverAddressInsertVO reveiverAddressInsertVO) {

        // 判断顾客编号是否存在
        String memberCard = reveiverAddressInsertVO.getMemberCard();
        Member member = memberMapper.selectMemberByCard(memberCard);
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在");
        }
        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressInsertVO, reveiverAddress);
        int num = reveiverAddressMapper.insertSelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }
        reveiverAddress.setReveiverAddressCode(reveiverAddress.getId());
        //更新code
        num = reveiverAddressMapper.updateByPrimaryKeySelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }

        // 添加记录
        ReveiverAddressRecordPo reveiverAddressRecordPo = new ReveiverAddressRecordPo();
        reveiverAddressRecordPo.setAfterData(JSONUtil.toJsonStr(reveiverAddress));
        reveiverAddressRecordPo.setId(null);
        reveiverAddressRecordPo.setReveiverAddressId(reveiverAddress.getId());
       int num1 =  reveiverAddressRecordDao.insertSelective(reveiverAddressRecordPo);
        if (num1 < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败");
        }
        return ComResponse.success();
    }

    @Override
    @Transactional
    public ComResponse<String> updateReveiverAddress(ReveiverAddressUpdateVO reveiverAddressUpdateVO) {
        // 获取数据
        ReveiverAddress reveiverAddress1 = reveiverAddressMapper.selectByPrimaryKey(reveiverAddressUpdateVO.getId());

        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressUpdateVO, reveiverAddress);
        int num = reveiverAddressMapper.updateByPrimaryKeySelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "数据更新失败");
        }

        //添加记录
        ReveiverAddress reveiverAddress2 = reveiverAddressMapper.selectByPrimaryKey(reveiverAddressUpdateVO.getId());
        ReveiverAddressRecordPo reveiverAddressRecordPo = new ReveiverAddressRecordPo();
        reveiverAddressRecordPo.setBeforeData(JSONUtil.toJsonPrettyStr(reveiverAddress1));
        reveiverAddressRecordPo.setAfterData(JSONUtil.toJsonStr(reveiverAddress2));
        reveiverAddressRecordPo.setReveiverAddressId(reveiverAddressUpdateVO.getId());
        reveiverAddressRecordPo.setModifyNo(reveiverAddressUpdateVO.getUpdateCode());
        reveiverAddressRecordPo.setId(null);
        reveiverAddressRecordPo.setReveiverAddressId(reveiverAddress.getId());
        int num1 =  reveiverAddressRecordDao.insertSelective(reveiverAddressRecordPo);
        if (num1 < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败");
        }

        return ComResponse.success();
    }
}
