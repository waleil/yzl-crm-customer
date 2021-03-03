package cn.net.yzl.crm.customer.service.impl.amount;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberAmountDao;
import cn.net.yzl.crm.customer.dao.MemberAmountDetailDao;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.MemberAmount;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.DateCustomerUtils;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import jodd.util.StringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MemberAmountServiceImpl implements MemberAmountService {
    private static final Logger logger = LoggerFactory.getLogger(MemberAmountServiceImpl.class);
    @Autowired
    private MemberAmountDao memberAmountDao;
    @Autowired
    private MemberAmountDetailDao memberAmountDetailDao;
    @Autowired
    private MemberMapper memberMapper;
    @Override
    @Transactional
    public ComResponse<MemberAmountDto> getMemberAmount(String memberCard) {
        MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
        if (memberAmountDto == null) { // 如果不存在 就添加一个账户
            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);
            int num = memberAmountDao.insertSelective(memberAmount);
            if (num < 0) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE);
            }
        }
        return ComResponse.success(memberAmountDto);
    }

    @Override
    public ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(String memberCard, Integer timeFlag) throws ParseException {
        Date now = new Date();
        if (timeFlag == 1) { // 最近三个月
            now = DateCustomerUtils.beforeMonth(now, 2);
        } else if (timeFlag == 2) { // 三个月以前的
            now = null;
        } else {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "timeFlag 参数错误!");
        }
        List<MemberAmountDetailDto> list = memberAmountDetailDao.getMemberAmountDetailList(memberCard, now);

        if (list == null || list.size() < 1) {
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }

    @Autowired
    private RedissonClient redisson;

    @Override
    @Transactional
    public ComResponse<String> operation(MemberAmountDetailVO memberAmountDetailVO) {

        //当类型为2的时候，直接完成扣减
        Integer operateType = memberAmountDetailVO.getOperateType() == null || memberAmountDetailVO.getOperateType() != 2 ? 1 : 2;
        boolean isComplete = operateType == 2;

        String memberCard = memberAmountDetailVO.getMemberCard();
        // 用会员号 做锁
        RLock lock = redisson.getLock("operation---"+memberCard);

        try {
            //尝试加锁300ms
            if(lock.tryLock(300, TimeUnit.MILLISECONDS)){
                // 判断账户是否存在
                MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
                if (memberAmountDto == null) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
                }
                //操作金额
                Integer discountMoney = memberAmountDetailVO.getDiscountMoney();
                if (discountMoney < 0) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "金额错误!");
                }

                    //操作类型
                int obtainType = memberAmountDetailVO.getObtainType();
                //操作订单号
                String orderNo = memberAmountDetailVO.getOrderNo();

                MemberAmountDetail consumeDetail = null;//消费记录
                MemberAmountDetail returnDetail = null;//退回记录

                // 校验 参数 如果是 退回或者消费的时候 订单号必传
                if (obtainType == 1 || obtainType == 2) {
                    // 判断 订单号是否存在
                    if (StrUtil.isBlank(orderNo)) {
                        throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "订单号必传");
                    }
                    // 判断相应的操作订单 记录 是否存在
                    //MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);
                    //查询状态为1,3的
                    Map<Byte,MemberAmountDetail> memberAmountDetailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(1,2),Arrays.asList(1,3));
                    if (memberAmountDetailMap.get((byte)obtainType) != null) {
                        throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType:" + obtainType + ",已经操作过,不可重复操作!");
                    }

                    consumeDetail = memberAmountDetailMap.get((byte)2);//消费记录
                    returnDetail = memberAmountDetailMap.get((byte)1);//退回记录

                    //退回的时候，必须要有消费记录
                    if (obtainType == 1) {
                        if (consumeDetail == null) {
                            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType:" + obtainType + ",没有找消费记录,不可退回操作!");
                        }
                        //金额要和消费金额完全一致
                        else if (!discountMoney.equals(consumeDetail.getDiscountMoney())) {
                            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "退回金额不正确!应退金额为:"+consumeDetail.getDiscountMoney());
                        }
                    }
                    //消费的时候
                    else if (obtainType == 2) {
                        //要判断可用余额是否充足
                        if (memberAmountDto.getValidAmount() < discountMoney) {
                            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户余额不足!");
                        }

                    }
                }

                //用于更新账户信息
                MemberAmount memberAmount = new MemberAmount();
                memberAmount.setMemberCard(memberCard);

                MemberAmountDetail memberAmountDetail = new MemberAmountDetail();
                //BeanUtil.copyProperties(memberAmountDetailVO, memberAmountDetail);
                memberAmountDetail.setMemberCard(memberCard);
                memberAmountDetail.setRemark(memberAmountDetailVO.getRemark());
                memberAmountDetail.setCreateDate(new Date());
                memberAmountDetail.setObtainType((byte)obtainType);
                memberAmountDetail.setOrderNo(orderNo);
                memberAmountDetail.setDiscountMoney(memberAmountDetailVO.getDiscountMoney());
                //操作类型
                memberAmountDetail.setOperateType(operateType);
                //退回
                if (obtainType == 1) {
                    //消费未确认时：不管是否一步退回，都将状态改为5
                    if (consumeDetail.getStatus() != 1) {
                        memberAmountDetail.setStatus((byte)5);//未完成退回
                        consumeDetail.setStatus((byte)5);//未完成取消
                        //将消费的冻结金额直接减去消费金额
                        memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - discountMoney);
                        //增加可用余额
                        memberAmount.setValidAmount(memberAmountDto.getValidAmount() + discountMoney);
                    }
                    //当消费记录的状态为待完成时
                    else{
                        //一步退回时
                        if (isComplete) {
                            memberAmountDetail.setStatus((byte)4);//退回
                            consumeDetail.setStatus((byte)4);//已完成退回
                            //直接增加账户余额
                            memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + discountMoney);
                            //直接增加可用余额
                            memberAmount.setValidAmount(memberAmountDto.getValidAmount() + discountMoney);
                        }
                        //退回冻结时
                        else{
                            memberAmountDetail.setStatus((byte)3);//未完成退回
                            memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() + discountMoney);
                        }
                    }

                    //更新消费记录的状态
                    int result = memberAmountDao.updateConsumeDetailStatus(consumeDetail);
                    if (result < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "账户信息修改错误!");
                    }
                }
                //消费
                else if (obtainType == 2) {
                    //直接扣减
                    if (isComplete) {
                        memberAmountDetail.setStatus((byte) 1);
                        //总金额直接扣减
                        memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() - discountMoney);
                        //可用余额直接扣减
                        memberAmount.setValidAmount(memberAmountDto.getValidAmount() - discountMoney);
                    }
                    //冻结消费
                    else{
                        memberAmountDetail.setStatus((byte) 3);
                        //冻结扣减金额
                        memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() + discountMoney);
                        //减少可用余额
                        memberAmount.setValidAmount(memberAmountDto.getValidAmount() - discountMoney);
                    }
                }
                //充值:直接增加账户余额
                else if (obtainType == 3) {
                    memberAmountDetail.setStatus((byte) 1);
                    memberAmountDetail.setOperateType(2);
                    memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + discountMoney);
                    memberAmount.setValidAmount(memberAmountDto.getValidAmount() + discountMoney);
                }
                // 修改账户信息
                int num = memberAmountDao.updateByPrimaryKeySelective(memberAmount);

                if (num < 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "账户信息修改错误!");
                }
                // 生成新的记录信息
                int num1 = memberAmountDetailDao.insertSelective(memberAmountDetail);
                if (num1 < 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "账户信息记录保存错误!");
                }
            }else{
                throw new BizException(ResponseCodeEnums.REPEAT_ERROR_CODE.getCode(), "当前账户正在下单操作，请勿重复下单!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return ComResponse.success();
    }

    /**
     * 扣减账户余额确认
     * wangzhe
     * 2021-03-03
     * @param obtainType
     * @param orderNo 订单号
     * @return
     */
    @Override
    @Transactional
    public ComResponse<String> operationConfirm(int obtainType, String orderNo) {
        if (obtainType != 1 && obtainType != 2) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "参数obtainType不正确!");
        }
        RLock lock = redisson.getLock("operationConfirm"+orderNo);

        try {
            lock.tryLock(3, TimeUnit.SECONDS);
            logger.info("MemberAmountServiceImpl  operationConfirm () : obtainType : {},orderNO:{}", obtainType, orderNo);
            // 目前的操作 只支持 消费和退款的 确认 obtainType 为 1(退回) 2:(消费)

            // 获取 操作记录
            //MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);
            Map<Byte, MemberAmountDetail> memberAmountDetailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(1, 2),Arrays.asList(1,3));
            //获取消费冬冻结记录
            MemberAmountDetail frozenDetail = memberAmountDetailMap.get((byte) obtainType);
            if (frozenDetail == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType: " + obtainType + ",未找到冻结记录!");
            } else if (frozenDetail.getStatus() == 1) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType: " + obtainType + ",已经确认过,不可重复操作!");
            }

            MemberAmountDetail consumeDetail = memberAmountDetailMap.get((byte)2);//消费记录
            MemberAmountDetail returnDetail = memberAmountDetailMap.get((byte)1);//退回记录

            Integer frozen = frozenDetail.getDiscountMoney();//要减去的冻结金额(本次确认金额)

            // 判断顾客账户是否存在
            String memberCard = frozenDetail.getMemberCard();
            MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
            if (memberAmountDto == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
            }

            //确认金额  > 总的冻结金额
            if (frozen > memberAmountDto.getFrozenAmount()) {
                logger.info("MemberAmountServiceImpl  operationConfirm () : orderNO:{},顾客:{} 账户异常!总金额:{}，冻结金额:{},本次需要扣减金额:{}",
                        orderNo,frozenDetail.getMemberCard(),memberAmountDto.getTotalMoney(),memberAmountDto.getFrozenAmount(),frozen);
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "顾客账户异常!");
            }

            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);

            // 判断 是消费 的时候 总金额是否满足扣除
            if (obtainType == 2) { // 2:(消费确认)
                if (frozen > memberAmountDto.getTotalMoney()){
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "消费确认时,总额度不够消费!");
                }
                //扣减余额
                memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() - frozen);
                consumeDetail.setStatus((byte)1);
            }
            //退回的确认的时候把消费确认的状态改为4
            else if (obtainType == 1) {//退回确认
                //增加总余额
                memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + frozen);
                //增加可用余额
                memberAmount.setValidAmount(memberAmountDto.getValidAmount() + frozen);
                returnDetail.setStatus((byte)4);
                consumeDetail.setStatus((byte)4);
            }
            //剩余冻结金额 = 总冻结金额 - 本次确认的金额
            memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - frozen);

            // 修改账户信息
            int result = memberAmountDao.updateByPrimaryKeySelective(memberAmount);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
            }
            result = memberAmountDao.updateConsumeDetailStatus(consumeDetail);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
            }
            if (returnDetail != null) {
                result = memberAmountDao.updateConsumeDetailStatus(returnDetail);
                if (result < 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
                }
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ComResponse.success();
    }

    /**
     * 取消退回
     * wangzhe
     * 2021-03-03
     * @param orderNo 订单号
     * @return
     */
    @Override
    @Transactional
    public ComResponse<String> operationReurnCancel(String orderNo) {
        if (StringUtil.isEmpty(orderNo)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "参数orderNo不能为空!");
        }
        RLock lock = redisson.getLock("operationConfirm"+orderNo);

        try {
            lock.tryLock(3, TimeUnit.SECONDS);
            logger.info("MemberAmountServiceImpl  operationConfirm ():orderNO:{}", orderNo);
            // 目前的操作 只支持 消费和退款的 确认 obtainType 为 1(退回) 2:(消费)

            // 获取 操作记录
           // MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(1, orderNo);
            Map<Byte, MemberAmountDetail> memberAmountDetailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(1),Arrays.asList(3));
            //获取消费冬冻结记录
            MemberAmountDetail frozenDetail = memberAmountDetailMap.get((byte)1);
            if (frozenDetail == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ",未找到冻结记录!");
            }
            Integer frozen = frozenDetail.getDiscountMoney();//要减去的冻结金额(本次确认金额)

            // 判断顾客账户是否存在
            String memberCard = frozenDetail.getMemberCard();
            MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
            if (memberAmountDto == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
            }

            //确认金额  > 总的冻结金额
            if (frozen > memberAmountDto.getFrozenAmount()) {
                logger.info("MemberAmountServiceImpl  operationConfirm () : orderNO:{},顾客:{} 账户异常!总金额:{}，冻结金额:{},本次需要取消冻结金额:{}",
                        orderNo,frozenDetail.getMemberCard(),memberAmountDto.getTotalMoney(),memberAmountDto.getFrozenAmount(),frozen);
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "顾客账户异常!");
            }
            //设置余额明细记录的状态为2作废
            frozenDetail.setStatus((byte)2);

            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);
            //剩余冻结金额 = 总冻结金额 - 本次确认的金额
            memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - frozen);

            // 修改账户信息
            int result = memberAmountDao.updateByPrimaryKeySelective(memberAmount);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
            }
            result = memberAmountDao.updateConsumeDetailStatus(frozenDetail);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ComResponse.success();
    }
}
