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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 订单签收推送的rabbitMQ消息
 * wangzhe
 * 2021-03-01
 */
@Slf4j
@Service
public class MemberOrderSignHandleServiceImpl implements MemberOrderSignHandleService {

    @Autowired
    MemberService memberService;
    @Autowired
    private MemberOrderSignHandleMapper memberOrderSignHandleMapper;

    /**
     * 保存记录
     * wangzhe
     * 2021-03-01
     * @param record
     * @return
     */
    @Override
    @Transactional
    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandle record) {
        int insert = memberOrderSignHandleMapper.insert(record);
        if (insert < 1) {
            ComResponse.success(Boolean.FALSE);
        }
        return ComResponse.success(Boolean.TRUE);
    }

    /**
     * 手动处理失败的消息
     * wangzhe
     * 2021-03-19
     * @param primaryKey 处理记录的主键id
     * @return 是否操作成功
     */
    @Override
    @Transactional
    public Boolean dealFailRecord(Integer primaryKey) {
        MemberOrderSignHandle orderSignHandle = memberOrderSignHandleMapper.selectByPrimaryKey(primaryKey);
        if (orderSignHandle == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), primaryKey+":记录不存在!");
        }
        if (orderSignHandle.getStatus() == 1) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "消息已经处理成功,不能重复操作!");
        }
        try {
            String orderData = orderSignHandle.getOrderData();
            OrderSignInfo4MqVO order = JSONUtil.toBean(orderData, OrderSignInfo4MqVO.class);
            //订单签收后的消息处理逻辑
            ComResponse<Boolean> response = memberService.orderSignUpdateMemberData(order);
            if (response.getCode() == 200) {
                orderSignHandle.setStatus(1);
                if (StringUtils.isEmpty(orderSignHandle.getMemberCard())) {
                    orderSignHandle.setMemberCard(order.getMemberCardNo());
                }
                if (StringUtils.isEmpty(orderSignHandle.getOrderNo())) {
                    orderSignHandle.setOrderNo(order.getOrderNo());
                }
                //处理成功后，更新消息
                int result = memberOrderSignHandleMapper.updateSuccessStatusByPrimaryKey(orderSignHandle);
            }

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("dealFailRecord:数据处理异常!",e);
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "数据处理异常!");
        }
        return Boolean.TRUE;
    }

    /**
     * 获取列表
     * wangzhe
     * 2021-03-19
     * @param page 分页参数
     * @return 分页数据
     */
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

    /**
     * 更新消息记录
     * wangzhe
     * 202-03-19
     * @param signHandle
     * @return
     */
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
        int result = memberOrderSignHandleMapper.updateByPrimaryKeyWithBLOBs(signHandle);
        if (result < 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(ResponseCodeEnums.BIZ_ERROR_CODE.getCode(), "记录修改失败!");
        }
        return result;
    }
}
