package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberProductEffectMapper;
import cn.net.yzl.crm.customer.dao.MemberProductEffectRecordMapper;
import cn.net.yzl.crm.customer.dto.member.MemberProductEffectDTO;
import cn.net.yzl.crm.customer.feign.api.WorkOrderClientAPI;
import cn.net.yzl.crm.customer.feign.client.workorder.WorkOrderClient;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberProductEffect;
import cn.net.yzl.crm.customer.model.db.MemberProductEffectRecord;
import cn.net.yzl.crm.customer.service.MemberProductEffectService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.CacheForProductEffectUtil;
import cn.net.yzl.crm.customer.utils.date.DealDateUtil;
import cn.net.yzl.crm.customer.vo.MemberProductEffectInsertVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MemberProductEffectServiceImpl implements MemberProductEffectService {


    @Autowired
    WorkOrderClient workOrderClient;

    @Autowired
    MemberProductEffectMapper memberProductEffectMapper;
    @Autowired
    MemberProductEffectRecordMapper memberProductEffectRecordMapper;
    @Autowired
    MemberMapper memberMapper;


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    @Override
    public ComResponse save(MemberProductEffectInsertVO record) {
        //查询客户是否存在
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在");
        }

        //
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(new Date());

        //每天吃几次
        Integer oneToTimes = record.getOneToTimes();
        //每次吃多少粒
        Integer oneUseNum = record.getOneUseNum();
        //每天吃多少(计算)
        //Integer eatingTime = record.getEatingTime();
        //商品余量
        Integer productLastNum = record.getProductLastNum();

        //每天用量
        Integer oneNum = null;
        if (oneToTimes != null && oneUseNum != null && oneToTimes > 0 && oneUseNum > 0) {
            oneNum = oneToTimes*oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //商品服用完日期
        Integer eatDay = null;
        if (oneNum != null && oneNum > 0 && productLastNum>0) {
            eatDay = productLastNum % oneNum == 0 ? productLastNum / oneNum : productLastNum / oneNum + 1;
            //获取当前时间
            Calendar current = Calendar.getInstance();
            current.add(Calendar.DATE, eatDay-1);
            Date date = null;
            try {
                date = sdf.parse(sdf.format(current.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memberProductEffect.setDueDate(date);
        }

        int result = memberProductEffectMapper.insertSelective(memberProductEffect);
        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }

        //查询上一步保存的记录
        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // 添加记录
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setModifyTime(record.getUpateTime());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(new Date());

        //保存
        result =  memberProductEffectRecordMapper.insertSelective(modifyRecord);

        if (result < 1) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录数据保存失败!");
        }
        return ComResponse.success();
    }


    @Transactional
    protected int modify(MemberProductEffectUpdateVO record) {
        if (record.getId() == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "id不能为空");
        }
        if (StringUtils.isEmpty(record.getMemberCard())) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "memberCard不能为空");
        }
        //查询客户是否存在
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在");
        }
        //查询之前的数据
        MemberProductEffect memberProductEffectBefore = memberProductEffectMapper.selectByPrimaryKey(record.getId());
        if (memberProductEffectBefore == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "记录不存在");
        }

        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(new Date());

        //每天吃几次
        Integer oneToTimes = record.getOneToTimes();
        //每次吃多少粒
        Integer oneUseNum = record.getOneUseNum();
        //每天吃多少(计算)
        //Integer eatingTime = record.getEatingTime();
        //商品余量
        Integer productLastNum = memberProductEffectBefore.getProductLastNum();

        //每天用量
        Integer oneNum = null;
        if (oneToTimes > 0 && oneUseNum > 0) {
            oneNum = oneToTimes*oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //商品服用完日期
        Integer eatDay = null;
        if (oneNum != null && oneNum > 0 && productLastNum != null && productLastNum>0) {
            eatDay = productLastNum % oneNum == 0 ? productLastNum / oneNum : productLastNum / oneNum + 1;
            //获取当前时间
            Calendar current = Calendar.getInstance();
            current.add(Calendar.DATE, eatDay-1);
            Date date = null;
            try {
                date = sdf.parse(sdf.format(current.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memberProductEffect.setDueDate(date);
        }

        int result = memberProductEffectMapper.updateByPrimaryKeySelective(memberProductEffect);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败");
        }

        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // 添加记录
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setBeforeData(JSONUtil.toJsonPrettyStr(memberProductEffectBefore));
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(new Date());

        result =  memberProductEffectRecordMapper.insertSelective(modifyRecord);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败");
        }
        return result;
    }

    /**
     * 批量修改客户辅营效果记录
     * @param records
     * @return
     */
    @Transactional
    public ComResponse batchSaveProductEffect(List<MemberProductEffectInsertVO> records) {
        if (CollectionUtil.isEmpty(records)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空!");
        }
        for (MemberProductEffectInsertVO record : records) {
            ComResponse result = this.save(record);
            if (result.getStatus() != 1) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录数据保存失败!");
            }
        }
        return ComResponse.success();
    }


    /**
     * 批量修改客户辅营效果记录
     * @param records
     * @return
     */
    @Transactional
    public ComResponse batchModifyProductEffect(String userNo,List<MemberProductEffectUpdateVO> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            for (MemberProductEffectUpdateVO record : records) {
                //修改人赋值
                if (StringUtils.isEmpty(record.getUpdator())) {
                    record.setUpdator(userNo);
                }
                int result = this.modify(record);
                if (result != 1) {
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录数据保存失败!");
                }
            }
        }
        return ComResponse.success();
    }


    /**
     * 查询顾客服用效果记录
     * wangzhe
     * 2021-01-27
     * @param productEffect
     * @return
     */
    public ComResponse<List<MemberProductEffectDTO>> getProductEffects(MemberProductEffectSelectVO productEffect){

        List<MemberProductEffectDTO> productEffectDTOs = memberProductEffectMapper.getProductEffects(productEffect);
        return ComResponse.success(productEffectDTOs);


    }

    /**
     * 修改顾客的商品剩余数量
     * wangzhe
     * 2021-02-05
     * @return
     */
    @Override/*22:00*/
    public ComResponse<Boolean> updateMemberProductLastNumAndCreateWorkOrder() {
        log.info("update member product last num: start,当前时间{}",new Date());
        //查询配置规则
        Integer configDay = WorkOrderClientAPI.queryReturnVisitRules();
        log.info("update member product last num:查询配置规则,当前配置为：{}",configDay);
        Integer pageNo = 1, pageSize = 2_000;
        boolean hasNext = true;
        //临时记录要更新余量的商品服用效果的id
        List<Integer> idList = new ArrayList<>();
        //停止服用商品效果记录的id
        List<Integer> stoptakingIdList = new ArrayList<>();

        //客户编号
        List<String> memberCardList = new ArrayList<>();
        //计算商品服用完日期和当前时间的天数差
        long betweenDay;
        //获取系统当前时间
        Date currentDateStart = DealDateUtil.getStart(new Date());
        while (hasNext) {
            //分页查询顾客的商品服用效果
            PageHelper.startPage(pageNo, pageSize);
            List<MemberProductEffect> list = memberProductEffectMapper.selectMemberProductEffectByPage();
            if (list.size() < pageSize) {
                hasNext = false;
            }
            pageNo++;
            //更新数据
            for (MemberProductEffect item : list) {
                if (item.getDueDate() == null) {
                    continue;
                }
                //已经停服的要重新计算商品的服用完日期
                if (item.getTakingState() != null && item.getTakingState() == 2){
                    //有日用量的，则进行更新
                    if (item.getEatingTime() != null && item.getEatingTime() > 0){
                        stoptakingIdList.add(item.getId());
                    }
                    continue;
                }
                idList.add(item.getId());//用户更新商品的数量
                //小于最小服用天数
                betweenDay = DateUtil.between(currentDateStart, item.getDueDate(), DateUnit.DAY,false);
                if (betweenDay >= 0 && betweenDay < configDay){
                    //判断缓存是否存在(防止重复回访)
                    if (!CacheForProductEffectUtil.getAndSetNx(item.getMemberCard())) {
                        memberCardList.add(item.getMemberCard());
                    }
                }
            }
            //更新已经停服的商品的服用完日期
            if (CollectionUtil.isNotEmpty(stoptakingIdList)) {
                Integer count = memberProductEffectMapper.updateMemberProductDueDateByPrimaryKeys(stoptakingIdList);
                log.info("update member product stop taking due date: record count:{}",count);
                stoptakingIdList.clear();
            }
            //更新商品剩余量
            if (CollectionUtil.isNotEmpty(idList)) {
                Integer count = memberProductEffectMapper.updateMemberProductLastNumByPrimaryKeys(idList);
                log.info("update member product last num: record count:{}",count);
                idList.clear();
            }
            //次日回访
            if (CollectionUtil.isNotEmpty(memberCardList)){
                boolean result = WorkOrderClientAPI.productDosage(memberCardList);
                if (!result) {
                    log.info("update member product last num:创建次日回访工单失败!");
                }
                memberCardList.clear();
            }
        }
        log.info("update member product last num: end,当前时间{}",new Date());
        return ComResponse.success(true);
    }


}
