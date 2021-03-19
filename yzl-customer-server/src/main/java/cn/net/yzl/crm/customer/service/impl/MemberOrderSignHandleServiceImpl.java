package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberOrderSignHandleMapper;
import cn.net.yzl.crm.customer.dto.PageDTO;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;
import cn.net.yzl.crm.customer.service.MemberOrderSignHandleService;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class MemberOrderSignHandleServiceImpl implements MemberOrderSignHandleService {

    @Autowired
    MemberService memberService;
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

    @Override
    @Transactional
    public Boolean dealFailRecord(Integer primaryKey) {
        MemberOrderSignHandle orderSignHandle = memberOrderSignHandleMapper.selectByPrimaryKey(primaryKey);
        if (orderSignHandle == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "记录不存在");
        }
        if (orderSignHandle.getStatus() == 1) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "消息已经处理成功,不能重复操作!");
        }
        try {
            String orderData = orderSignHandle.getOrderData();
            OrderSignInfo4MqVO order = JSONUtil.toBean(orderData, OrderSignInfo4MqVO.class);

            ComResponse<Boolean> response = memberService.orderSignUpdateMemberData(order);
            if (response.getCode() == 200) {
                order.setStatus(1);
                int result = memberOrderSignHandleMapper.updateSuccessStatusByPrimaryKey(primaryKey);
            }

        } catch (Exception e) {
            log.error("dealFailRecord:数据处理异常!",e);
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "数据处理异常!");
        }


        return null;
    }

    @Override
    public Page<MemberOrderSignHandle> getFailRecordList(PageDTO page) {

        CompletableFuture<Integer> cfCount=CompletableFuture.supplyAsync(()->this.memberOrderSignHandleMapper.getFailRecordListCount());
        CompletableFuture<List<MemberOrderSignHandle>> cfList=CompletableFuture.supplyAsync(()->this.memberOrderSignHandleMapper.getFailRecordList(page));
        CompletableFuture.allOf(cfCount,cfList);
        List<MemberOrderSignHandle> list = Collections.emptyList();
        try {
            list = cfList.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getLocalizedMessage(),e);
        }
        Integer totalCount =0;
        try {
            totalCount = cfCount.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getLocalizedMessage(),e);
        }

        return page.toPage(list,totalCount);
    }



    @Override
    public Integer updateByPrimaryKeySelective(MemberOrderSignHandle signHandle) {
        if (signHandle == null) {
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "主键不能为空!");
        }
        //通过id查询记录，判断记录是否存在
        MemberOrderSignHandle memberOrderSignHandle = memberOrderSignHandleMapper.selectByPrimaryKey(signHandle.getId());
        if (memberOrderSignHandle == null) {
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "记录不存在!");
        }
        /*if (memberOrderSignHandle.getStatus() == 1) {
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "记录已经处理成功,不允许修改!");
        }*/
        int result = memberOrderSignHandleMapper.updateByPrimaryKeySelective(signHandle);
        if (result < 0) {
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "记录修改失败!");
        }
        return result;
    }
}
