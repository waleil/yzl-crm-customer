package cn.net.yzl.crm.customer.service.impl.amount;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberAmountDao;
import cn.net.yzl.crm.customer.dao.MemberAmountDetailDao;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDetailDto;
import cn.net.yzl.crm.customer.dto.amount.MemberAmountDto;
import cn.net.yzl.crm.customer.model.MemberAmount;
import cn.net.yzl.crm.customer.model.MemberAmountDetail;
import cn.net.yzl.crm.customer.service.amount.MemberAmountService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.DateCustomerUtils;
import cn.net.yzl.crm.customer.vo.MemberAmountDetailVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MemberAmountServiceImpl implements MemberAmountService {
    private static final Logger logger = LoggerFactory.getLogger(MemberAmountServiceImpl.class);
    @Autowired
    private MemberAmountDao memberAmountDao;
    @Autowired
    private MemberAmountDetailDao memberAmountDetailDao;

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
        String memberCard = memberAmountDetailVO.getMemberCard();
        // 用会员号 做锁
        RLock lock = redisson.getLock("operation---"+memberCard);

        try {
            lock.tryLock(3, TimeUnit.SECONDS);

            // 判断账户是否存在
            MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);

            if (memberAmountDto == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
            }
            Integer discountMoney = memberAmountDetailVO.getDiscountMoney();
            Integer totalMoney = memberAmountDto.getTotalMoney();
            int obtainType = memberAmountDetailVO.getObtainType();
            String orderNo = memberAmountDetailVO.getOrderNo();
            // 校验 参数 如果是 退回或者消费的时候 订单号必传
            if (obtainType == 1 || obtainType == 2) {
                // 判断 订单号是否存在
                if (StrUtil.isBlank(orderNo)) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "订单号必传");
                }
                // 判断相应的操作订单 记录 是否存在
                MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);
                if (memberAmountDetail != null) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "orderNo : " + orderNo + ", obtainType: " + obtainType + " 已经操作过,不可重复操作");
                }
                if (obtainType == 2) { // 判断账户余额 是否够消费
                    if (totalMoney < discountMoney) {
                        throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户余额 不足!");
                    }

                }
            }
            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);

            MemberAmountDetail memberAmountDetail = new MemberAmountDetail();
            if (obtainType == 1) { // 退回
                memberAmount.setFrozenAmount(discountMoney);
                memberAmountDetail.setStatus((byte) 3); // 进行中 待确认
            } else if (obtainType == 2) { //消费
                memberAmountDetail.setStatus((byte) 3);// 进行中 待确认
                memberAmount.setFrozenAmount(discountMoney);
            } else if (obtainType == 3) { //充值
                memberAmountDetail.setStatus((byte) 1);// 进行中 正常(完成)
                memberAmount.setTotalMoney(totalMoney + discountMoney);
            }
            // 修改账户信息
            int num = memberAmountDao.updateByPrimaryKeySelective(memberAmount);

            if (num < 1) {
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "账户信息修改错误");
            }
            // 生成新的记录信息

            BeanUtil.copyProperties(memberAmountDetailVO, memberAmountDetail);
            int num1 = memberAmountDetailDao.insertSelective(memberAmountDetail);
            if (num1 < 1) {
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "账户信息记录保存错误");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return ComResponse.success();
    }

    @Override
    @Transactional
    public ComResponse<String> operationConfirm(int obtainType, String orderNo) {
        RLock lock = redisson.getLock("operationConfirm"+orderNo);

        try {
            lock.tryLock(3, TimeUnit.SECONDS);
            logger.info("MemberAmountServiceImpl  operationConfirm () : obtainType : {},orderNO : {}", orderNo, orderNo);
            // 目前的操作 只支持 消费和退款的 确认 obtainType 为 1(退回) 2:(消费)
            // 获取 操作记录
            MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType, orderNo);

            if (memberAmountDetail == null) {
                logger.info("obtainType: {},orderNo : {} 记录不存在!", obtainType, orderNo);
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "订单 记录不存在!");
            }
            if(memberAmountDetail.getStatus()!=3){
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "订单状态不正确 只有进行中的订单才能确认!");
            }

            // 判断 如果 存在 是否已经 确认
            Byte status = memberAmountDetail.getStatus();
            if (status == 1) {
                logger.info("orderNo : {} 对应的记录操作 已经完成,不可重复操作!", orderNo);
            }
            // 修改修改记录的状态
            MemberAmountDetail memberAmountDetail1 = new MemberAmountDetail();
            memberAmountDetail1.setId(memberAmountDetail.getId());
            memberAmountDetail1.setStatus((byte) 1);
            int num = memberAmountDetailDao.updateByPrimaryKeySelective(memberAmountDetail1);
            if (num < 1) {
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客订单记录 修改异常");
            }
            // 判断顾客账户是否存在
            String memberCard = memberAmountDetail.getMemberCard();
            MemberAmountDto memberAmountDto = memberAmountDao.getMemberAmount(memberCard);
            if (memberAmountDto == null) {
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "账户不存在!");
            }
            Integer discountMoney = memberAmountDetail.getDiscountMoney();
            // 判断账户冻结的 余额是否满足 当前要添加或减少的 金额
            Integer frozenAmount = memberAmountDto.getFrozenAmount();
            if (discountMoney > frozenAmount) {
                logger.info("MemberAmountServiceImpl  operationConfirm () : obtainType : {},orderNO : {} ,金额有误!", orderNo, orderNo);
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "数据操作有误!");
            }
            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);
            memberAmount.setFrozenAmount(memberAmountDto.getFrozenAmount() - discountMoney);

            // 判断 是消费 的时候 总金额是否满足扣除
            if (obtainType == 2) { // 2:(消费)
                Integer totalMoney = memberAmountDto.getTotalMoney();
                if (totalMoney < discountMoney) {
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "消费 确认时,总额度 不够消费的!");
                }
                memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() - discountMoney);
            } else if (obtainType == 1) {

                memberAmount.setTotalMoney(memberAmountDto.getTotalMoney() + discountMoney);
            }

            // 修改账户信息
            int num1 = memberAmountDao.updateByPrimaryKeySelective(memberAmount);
            if (num < 1) {
                throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(), "顾客账户信息修改异常");
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ComResponse.success();
    }
}
