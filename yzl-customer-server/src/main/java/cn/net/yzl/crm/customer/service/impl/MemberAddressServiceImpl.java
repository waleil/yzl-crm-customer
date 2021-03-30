package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.common.util.JsonUtil;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressMapper;
import cn.net.yzl.crm.customer.dao.ReveiverAddressRecordDao;
import cn.net.yzl.crm.customer.dto.address.ReveiverAddressDto;
import cn.net.yzl.crm.customer.dto.member.MemberReveiverAddressSerchDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.crm.customer.model.db.ReveiverAddress;
import cn.net.yzl.crm.customer.model.db.ReveiverAddressRecordPo;
import cn.net.yzl.crm.customer.service.MemberAddressService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressInsertVO;
import cn.net.yzl.crm.customer.vo.address.ReveiverAddressUpdateVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
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
        if(CollectionUtil.isNotEmpty(list) && list.size() > 1){
            Collections.sort(list, new Comparator<ReveiverAddressDto>() {
                @Override
                public int compare(ReveiverAddressDto o1, ReveiverAddressDto o2) {
                    if(o1.getUpdateTime()==null||o2.getUpdateTime()==null){
                        return 0;
                    }
                    return (int) ((int) o2.getUpdateTime().getTime()-o1.getUpdateTime().getTime());
                }
            });
        }
        return ComResponse.success(list);
    }



    @Override
    @Transactional
    public ComResponse<String> addReveiverAddress(ReveiverAddressInsertVO reveiverAddressInsertVO) {

        // 判断顾客编号是否存在
        String memberCard = reveiverAddressInsertVO.getMemberCard();
        //获取顾客对象
        Member member = memberMapper.selectMemberByCard(memberCard);
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在");
        }

        int num1 = 0;
        //更新当前顾客其他的收货地址为非默认收货地址
        if (reveiverAddressInsertVO.getDefaultFlag() != null && reveiverAddressInsertVO.getDefaultFlag() == 1) {
            num1 =  reveiverAddressRecordDao.updateDefaultFlagByMemberCard(memberCard,1,0);
            if (num1 < 0) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "更新默认收货地址失败!");
            }
        }else{
            //查询顾客当前的收货地址，当没有一条默认记录时，更新当前添加的记录的default_flag = 1
            int count = reveiverAddressMapper.selectDefaultCountByMemberCard(memberCard);
            if (count < 1) {
                reveiverAddressInsertVO.setDefaultFlag(1);
            }
        }
        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressInsertVO, reveiverAddress);
        //保存新的收货地址
        int num = reveiverAddressMapper.insertSelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }
        reveiverAddress.setReveiverAddressCode(reveiverAddress.getId());
        //更新code为id
        num = reveiverAddressMapper.updateByPrimaryKeySelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }

        // 添加记录
        ReveiverAddressRecordPo reveiverAddressRecordPo = new ReveiverAddressRecordPo();
        reveiverAddressRecordPo.setAfterData(JSONUtil.toJsonStr(reveiverAddress));
        reveiverAddressRecordPo.setId(null);
        reveiverAddressRecordPo.setReveiverAddressId(reveiverAddress.getId());
       num1 =  reveiverAddressRecordDao.insertSelective(reveiverAddressRecordPo);
        if (num1 < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败");
        }
        return ComResponse.success(String.valueOf(reveiverAddress.getId()));
    }

    @Override
    @Transactional
    public ComResponse<String> updateReveiverAddress(ReveiverAddressUpdateVO reveiverAddressUpdateVO) {
        // 获取数据
        ReveiverAddress reveiverAddress1 = reveiverAddressMapper.selectByPrimaryKey(reveiverAddressUpdateVO.getId());
        if (reveiverAddress1 == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "记录不存在!");
        }
        int num = 0;

        //更新当前顾客其他的收货地址为非默认收货地址
        if (reveiverAddressUpdateVO.getDefaultFlag() != null && reveiverAddressUpdateVO.getDefaultFlag() == 1) {
            num =  reveiverAddressRecordDao.updateDefaultFlagByMemberCard(reveiverAddress1.getMemberCard(),1,0);
            if (num < 0) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "更新默认收货地址失败!");
            }
        }else{
            //查询顾客当前的收货地址，当没有一条默认记录时，更新当前添加的记录的default_flag = 1
            int count = reveiverAddressMapper.selectDefaultCountByMemberCard(reveiverAddress1.getMemberCard());
            if (count < 1) {
                reveiverAddressUpdateVO.setDefaultFlag(1);
            }
        }

        ReveiverAddress reveiverAddress = new ReveiverAddress();
        BeanUtil.copyProperties(reveiverAddressUpdateVO, reveiverAddress);
        num = reveiverAddressMapper.updateByPrimaryKeySelective(reveiverAddress);
        if (num < 1) {
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "数据更新失败!");
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
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败!");
        }

        return ComResponse.success();
    }

    @Override
    public ComResponse<Page<ReveiverAddressDto>> getReveiverAddressByPage(MemberReveiverAddressSerchDTO serchDTO) {
        PageHelper.startPage(serchDTO.getCurrentPage(), serchDTO.getPageSize());
        List<ReveiverAddressDto> list = reveiverAddressMapper.getReveiverAddressByPage(serchDTO);

        if(list==null || list.size()<0){
            return ComResponse.nodata();
        }
        Page<ReveiverAddressDto> page = AssemblerResultUtil.resultAssembler(list);

        return ComResponse.success(page);
    }


    /**
     * 删除收货地址(删除地址的时候判断当前记录是不是默认收货地址，如果是默认的则将剩余记录中的最新的一条设置为默认收货地址)
     * wanghze
     * 2021-03-15
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Integer deleteAddressById(Integer id) {
        //通过id获取当前收货地址记录
        ReveiverAddress reveiverAddress = reveiverAddressMapper.selectByPrimaryKey(id);
        //判断记录是否存在
        if (reveiverAddress == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "记录不存在!");
        }
        //删除当前记录
        int result = reveiverAddressMapper.deleteByPrimaryKey(id);
        if (result < 1) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "删除记录失败!");
        }

        //判断记录是否为默认收货地址
        if (1 == reveiverAddress.getDefaultFlag()) {
            //当要删除的地址为默认的收货地址的时候，要将数据苦衷最新的一条设置为默认收货地址
            ComResponse<List<ReveiverAddressDto>> response = this.getReveiverAddress(reveiverAddress.getMemberCard());
            if (response.getCode() != 200) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "获取数据失败!");
            }
            List<ReveiverAddressDto> addressDtoList = response.getData();
            if (CollectionUtil.isNotEmpty(addressDtoList)) {
                result = reveiverAddressMapper.updateDefaultFlagById(addressDtoList.get(0).getId());
                if (result < 1) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "操作失败!");
                }
            }
        }
        return result;
    }
}
