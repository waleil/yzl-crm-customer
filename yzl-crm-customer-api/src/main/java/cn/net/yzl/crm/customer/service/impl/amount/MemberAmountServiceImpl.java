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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MemberAmountServiceImpl implements MemberAmountService {

    @Autowired
    private MemberAmountDao memberAmountDao;
    @Autowired
    private MemberAmountDetailDao memberAmountDetailDao;

    @Override
    @Transactional
    public ComResponse<MemberAmountDto> getMemberAmount(String memberCard) {
        MemberAmountDto memberAmountDto= memberAmountDao.getMemberAmount(memberCard);
        if(memberAmountDto==null){ // 如果不存在 就添加一个账户
            MemberAmount memberAmount = new MemberAmount();
            memberAmount.setMemberCard(memberCard);
            int num = memberAmountDao.insertSelective(memberAmount);
            if(num<0){
                throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE);
            }
        }
        return ComResponse.success(memberAmountDto);
    }

    @Override
    public ComResponse<List<MemberAmountDetailDto>> getMemberAmountDetailList(String memberCard, Integer timeFlag) throws ParseException {
        Date now = new Date();
        if(timeFlag==1){ // 最近三个月
           now= DateCustomerUtils.beforeMonth(now,2);
        }else if(timeFlag==2){ // 三个月以前的
            now=null;
        }else{
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"timeFlag 参数错误!");
        }
        List<MemberAmountDetailDto> list =  memberAmountDetailDao.getMemberAmountDetailList(memberCard,now);

        if(list==null || list.size()<1){
            return ComResponse.nodata();
        }
        return ComResponse.success(list);
    }

    @Override
    @Transactional
    public synchronized ComResponse<String>  edit(MemberAmountDetailVO memberAmountDetailVO) {


        String memberCard = memberAmountDetailVO.getMemberCard();
        // 判断账户是否存在
        MemberAmountDto memberAmountDto= memberAmountDao.getMemberAmount(memberCard);

        if(memberAmountDto==null){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"账户不存在!");
        }
        Integer discountMoney = memberAmountDetailVO.getDiscountMoney();
        Integer totalMoney = memberAmountDto.getTotalMoney();
        int obtainType = memberAmountDetailVO.getObtainType();
        String orderNo = memberAmountDetailVO.getOrderNo();
        // 校验 参数 如果是 退回或者消费的时候 订单号必传
        if(obtainType==1 || obtainType==2){
            // 判断 订单号是否存在
            if(StrUtil.isBlank(orderNo)){
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"订单号必传");
            }
            // 判断相应的操作订单 记录 是否存在
            MemberAmountDetail memberAmountDetail = memberAmountDetailDao.getByTypeAndOrder(obtainType,orderNo);
            if(memberAmountDetail!=null){
                throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"orderNo : "+orderNo+", obtainType: "+obtainType+" 已经操作过,不可重复操作");
            }
            if(obtainType==2){ // 判断账户余额 是否够消费
                if(totalMoney<discountMoney){
                    throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"账户余额 不足!");
                }

            }
        }
        MemberAmount memberAmount = new MemberAmount();
        memberAmount.setMemberCard(memberCard);


        if(obtainType==1){ // 退回
            memberAmount.setTotalMoney(totalMoney+discountMoney);
        }else if(obtainType==2){ //消费
            memberAmount.setTotalMoney(totalMoney-discountMoney);
        }else if(obtainType==3){ //充值
            memberAmount.setTotalMoney(totalMoney+discountMoney);

        }
        // 修改账户信息
        int num = memberAmountDao.updateByPrimaryKeySelective(memberAmount);

        if(num<1){
            throw new BizException(ResponseCodeEnums.UPDATE_DATA_ERROR_CODE.getCode(),"账户信息修改错误");
        }
       // 生成新的记录信息
        MemberAmountDetail memberAmountDetail = new MemberAmountDetail();
        BeanUtil.copyProperties(memberAmountDetailVO,memberAmountDetail);
        int num1 = memberAmountDetailDao.insertSelective(memberAmountDetail);
        if(num1<1){
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),"账户信息记录保存错误");
        }


        return ComResponse.success();
    }
}
