package cn.net.yzl.crm.customer.service.impl.amount;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberAmountDao;
import cn.net.yzl.crm.customer.dao.MemberAmountDetailDao;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberOrderStatMapper;
import cn.net.yzl.crm.customer.dto.PageDTO;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberAmount;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.model.db.MemberRecharge;
import cn.net.yzl.crm.customer.service.MemberOrderStatService;
import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.service.amount.MemberRechargeService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CentYuanConvertUtil;
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

import java.math.BigDecimal;
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
    @Autowired
    MemberOrderStatMapper memberOrderStatMapper;

    @Autowired
    MemberOrderStatService memberOrderStatService;

    @Autowired
    MemberRechargeService memberRechargeService;



    @Override
    @Transactional
    public ComResponse<MemberAmountDto> getMemberAmount(String memberCard) {
        MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
        if (memberAmountDto == null) { // 如果不存在 --> 判断客户是否存在 存在才添加一个账户
            Member member = memberMapper.selectMemberByCard(memberCard);
            if (member == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"客户不存在!");
            }
            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);
            int num = memberAmountDao.insertSelective(memberAmount);
            if (num < 0) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE);
            }
            //插入成功后，重新查询账户信息
            memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
        }

        if (memberAmountDto != null) {
            memberAmountDto.setTotalMoneyD(CentYuanConvertUtil.cent2Yuan(memberAmountDto.getTotalMoney()));//总余额(包含冻结金额)
            memberAmountDto.setFrozenAmountD(CentYuanConvertUtil.cent2Yuan(memberAmountDto.getFrozenAmount()));//冻结金额
            memberAmountDto.setValidAmountD(CentYuanConvertUtil.cent2Yuan(memberAmountDto.getValidAmount()));//可用余额(元)
        }

        return ComResponse.success(memberAmountDto);
    }

//    @Override
//    public ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(String memberCard, Integer timeFlag) throws ParseException {
//        Date now = new Date();
//        if (timeFlag == 1) { // 最近三个月
//            now = DateCustomerUtils.beforeMonth(now, 2);
//        } else if (timeFlag == 2) { // 三个月以前的
//            now = null;
//        } else {
//            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "timeFlag 参数错误!");
//        }
//        List<MemberAmountDetailDto> list = memberAmountDetailDao.getMemberAmountDetailList(memberCard, now);
//
//        if (list == null || list.size() < 1) {
//            return ComResponse.nodata();
//        }
//        return ComResponse.success(list);
//    }

    @Autowired
    private RedissonClient redisson;

    /**
     * 冻结扣款 退回 充值
     * @param memberAmountDetailVO
     * @return
     */
    @Override
    @Transactional
    public ComResponse<String> operation(MemberAmountDetailVO memberAmountDetailVO) {

        String memberCard = memberAmountDetailVO.getMemberCard();
        // 用会员号 做锁
        RLock lock = redisson.getLock("operation---"+memberCard);
        //当前时间
        Date now = new Date();

        try {
            //尝试加锁300ms
            if(lock.tryLock(300, TimeUnit.MILLISECONDS)){
                // 判断账户是否存在
                ComResponse<MemberAmountDto> response = this.getMemberAmount(memberCard);
                if (response.getCode() != 200) {
                    throw new BizException(response.getCode(), response.getMessage());
                }
                MemberAmountDto memberAmountDto = response.getData();
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

                //MemberAmountDetail returnDetail = null;//退回记录
                MemberAmountDetail consumeDetail = null;//未完成的消费记录

                // 校验 参数 如果是 退回或者消费的时候 订单号必传
                if (obtainType == 1 || obtainType == 2) {
                    // 判断 订单号是否存在
                    if (StrUtil.isBlank(orderNo)) {
                        throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "订单号必传");
                    }
                    //
                    /**
                     * 1.退回时:
                     *      1.1没有消费记录，直接退回余额
                     *      1.2有消费记录时:
                     *          1.2.1.消费记录：未确认，只能退回和消费记录同样的金额   【需要校验】
                     *          1.2.2.消费记录：已经确，可以退回任意次数，任意金额 (不用校验)
                     *          1.2.3.消费记录：作废(相当于消费记录不存在)，可以退回任意次数，任意金额 (不用校验)
                     *
                     * 2.消费时：
                     *      2.1.当有：未确认 记录时，不能重复操作(不能重复消费)   【需要校验】
                     *      2.2.当有：无效的消费 记录时，(不用校验)，按无消费记录处理
                     *      2.3.当 消费记录 已经确认时，不能重复操作(不能重复消费)   【需要校验】
                     */
                    if (obtainType == 1) {
                        // 判断相应的操作订单 记录 是否存在
                        //returnDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);//退回记录
                        //consumeDetail = memberAmountDetailDao.getByTypeAndOrder(2, orderNo);//未完成的消费记录
                        Map<Byte, MemberAmountDetail> detailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(2),Arrays.asList(3));
                        consumeDetail = detailMap.get((byte)2);
                        //消费记录未确认
                        if (consumeDetail != null) {
                            //可以操作（但是金额要和冻结金额一致）
                            if (!discountMoney.equals(consumeDetail.getDiscountMoney())) {
                                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "退回金额不正确!应退金额为:" + consumeDetail.getDiscountMoney());
                            }
                        }
                    }
                    //消费扣款
                    else{
                        Map<Byte, MemberAmountDetail> detailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(2),Arrays.asList(1,3));
                        //存在未确认 或者已经确认的记录时，不能重复操作
                        if (CollectionUtil.isNotEmpty(detailMap)){
                            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType:" + obtainType + ",已经存在扣货款记录,不可重复操作!");
                        }
                        //要判断可用余额是否充足
                        else if (memberAmountDto.getValidAmount() < discountMoney) {
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
                memberAmountDetail.setCreateDate(now);
                memberAmountDetail.setStartDate(now);//记录开始时间
                memberAmountDetail.setObtainType((byte)obtainType);
                memberAmountDetail.setOrderNo(orderNo);
                memberAmountDetail.setDiscountMoney(memberAmountDetailVO.getDiscountMoney());//金额

                //是否为添加操作
                boolean isAdd = false;

                //退回
                if (obtainType == 1) {
                    //消费未确认时：直接作废
                    if (consumeDetail!= null) {
                        isAdd = false;
                        consumeDetail.setStatus((byte)2);//作废
                        consumeDetail.setEndDate(now);//作废时间

                        String remark = consumeDetail.getRemark();
                        if (StringUtil.isEmpty(remark)) {
                            remark = "";
                        }
                        if (StringUtil.isNotEmpty(memberAmountDetailVO.getRemark())){
                            remark = remark + ",退回原因:" + memberAmountDetailVO.getRemark();
                        }
                        consumeDetail.setRemark(remark);
                        //将消费的冻结金额直接减去消费金额
                        memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - discountMoney);
                        //更新消费记录的状态
                        int result = memberAmountDao.updateConsumeDetailStatus(consumeDetail);
                        if (result < 1) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "账户信息修改错误!");
                        }
                    }
                    //当1.无消费记录 或者 2.无有效消费记录 或者 3.消费记录已经确认时
                    else{
                        isAdd = true;
                        memberAmountDetail.setStatus((byte) 1);
                        memberAmountDetail.setEndDate(now);//退回完成时间
                        //直接增加账户余额
                        memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + discountMoney);
                    }
                }
                //冻结消费
                else if (obtainType == 2) {
                    isAdd = true;
                    memberAmountDetail.setStatus((byte) 3);
                    //冻结扣减金额
                    memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() + discountMoney);
                }
                //充值:直接增加账户余额
                else if (obtainType == 3) {
                    isAdd = true;
                    memberAmountDetail.setStatus((byte) 1);//直接完成
                    memberAmountDetail.setEndDate(now);//充值完成时间
                    memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + discountMoney);

                    //计算累计充值金额
                    //更新member_order_stat
                    MemberOrderStat memberOrderStat = memberOrderStatService.queryByMemberCode(memberCard);
                    if (memberOrderStat == null) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "memberOrderStat记录不存在!");
                    }

                    //累计充值金额
                    Integer totalInvestAmount = memberOrderStat.getTotalInvestAmount() == null ? 0 : memberOrderStat.getTotalInvestAmount();
                    totalInvestAmount += discountMoney;

                    MemberOrderStat update = new MemberOrderStat();
                    update.setTotalInvestAmount(totalInvestAmount);
                    update.setMemberCard(memberOrderStat.getMemberCard());

                    int result = memberOrderStatMapper.updateByPrimaryKeySelective(update);
                    if (result < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"记录数据保存失败!");
                    }
                    //新增客户充值记录
                    MemberRecharge recharge = new MemberRecharge();
                    recharge.setMemberCard(memberCard);
                    recharge.setCreateDate(now);
                    recharge.setInMoney(discountMoney);
                    result = memberRechargeService.addRecharge(recharge);
                    if (result < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ComResponse.fail(ResponseCodeEnums.SERVICE_ERROR_CODE.getCode(),"充值记录保存失败!");
                    }
                }
                // 修改账户信息
                int num = memberAmountDao.updateByPrimaryKeySelective(memberAmount);
                if (num < 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "账户信息修改错误!");
                }
                // 生成新的记录信息
                if (isAdd) {
                    num = memberAmountDetailDao.insertSelective(memberAmountDetail);
                    if (num < 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "账户信息记录保存错误!");
                    }
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
        if (obtainType != 2) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "参数obtainType不正确!");
        }
        RLock lock = redisson.getLock("operationConfirm"+orderNo);

        try {
            lock.tryLock(3, TimeUnit.SECONDS);
            logger.info("MemberAmountServiceImpl  operationConfirm () : obtainType : {},orderNO:{}", obtainType, orderNo);
            // 目前的操作 只支持 消费和退款的 确认 obtainType 为 1(退回) 2:(消费)

            // 获取消费操作记录
            //MemberAmountDetail consumeDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);
            Map<Byte, MemberAmountDetail> detailMap = memberAmountDetailDao.getDetailByTypesAndOrder(orderNo,Arrays.asList(2),Arrays.asList(3));
            MemberAmountDetail consumeDetail = detailMap.get((byte)2);
            if (consumeDetail == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType: " + obtainType + ",未找到待确认冻结记录!");
            } /*else if (consumeDetail.getStatus() == 1) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType: " + obtainType + ",已经确认过,不可重复操作!");
            } else if (consumeDetail.getStatus() != 3) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo:" + orderNo + ", obtainType: " + obtainType + ",冻结已经退回,不可确认操作!");
            }*/

            Integer frozen = consumeDetail.getDiscountMoney();//要减去的冻结金额(本次确认金额)

            // 判断顾客账户是否存在
            String memberCard = consumeDetail.getMemberCard();
            MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
            if (memberAmountDto == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
            }

            //确认金额  > 总的冻结金额
            if (frozen > memberAmountDto.getFrozenAmount()) {
                logger.info("MemberAmountServiceImpl  operationConfirm () : orderNO:{},顾客:{} 账户异常!总金额:{}，冻结金额:{},本次需要扣减金额:{}",
                        orderNo,consumeDetail.getMemberCard(),memberAmountDto.getTotalMoney(),memberAmountDto.getFrozenAmount(),frozen);
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "顾客账户异常!");
            }

            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);

            if (frozen > memberAmountDto.getTotalMoney()){
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "消费确认时,总额度不够消费!");
            }
            //扣减余额
            memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() - frozen);
            //剩余冻结金额 = 总冻结金额 - 本次确认的金额
            memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - frozen);

            // 修改账户信息
            int result = memberAmountDao.updateByPrimaryKeySelective(memberAmount);
            if (result < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常!");
            }

            consumeDetail.setStatus((byte)1);
            consumeDetail.setEndDate(new Date());//确认消费完成时间
            result = memberAmountDao.updateConsumeDetailStatus(consumeDetail);
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

    /**
     * 查询订单对应的冻结记录
     * @param orderNo 订单号
     * @param obtainType 1:退回记录 2:消费记录
     * @return 对应的余额明细记录
     */
    public MemberAmountDetail getFrozenDetailByOrder(String orderNo,Integer obtainType){
        MemberAmountDetail detail = null;
        if (obtainType != null && (obtainType ==1 || obtainType == 2)) {
            detail = memberAmountDetailDao.getByTypeAndOrder(obtainType,orderNo);
            if (detail != null && detail.getStatus() != 3) {
                detail = null;
            }
        }
        return detail;
    }

    @Override
    public List<MemberAmountDetailDto> getMemberAmountDetailsBymemberCardAndOrderList(String memberCard, List<String> orderList) {
        List<MemberAmountDetailDto> list = memberAmountDetailDao.getMemberAmountDetailsBymemberCardAndOrderList(memberCard, orderList);
        if (list == null) {
            list = Collections.emptyList();
        }
        return list;
    }

    @Override
    public Page<MemberAmountDetailDto> getMemberAmountDetailListByPage(String memberCard, Integer pageNo, Integer pageSize, Integer timeFlag) throws ParseException {

        PageDTO pageDTO = new PageDTO();

        pageDTO.setCurrentPage(pageNo);
        pageDTO.setPageSize(pageSize);

        Date now = new Date();
        now = DateCustomerUtils.beforeMonth(now, 2);
        Integer count = memberAmountDetailDao.getMemberAmountDetailListCount(memberCard, timeFlag,now);
        List<MemberAmountDetailDto> list = memberAmountDetailDao.getMemberAmountDetailListByPage(memberCard, timeFlag,now,pageDTO);

        Page<MemberAmountDetailDto> page = pageDTO.toPage(list, count);
        return page;

    }

}
