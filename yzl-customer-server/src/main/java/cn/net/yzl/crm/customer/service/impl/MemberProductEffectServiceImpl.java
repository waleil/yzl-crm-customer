package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
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
    public ComResponse<Boolean> save(MemberProductEffectInsertVO record) {
        //查询客户是否存在
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在!");
        }
        //系统当前时间
        DateTime now = DateUtil.date();
        //创建更新对象
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(now);

        //每天吃几次
        Integer oneToTimes = record.getOneToTimes();
        //每次吃多少
        Integer oneUseNum = record.getOneUseNum();
        //商品余量
        Integer productLastNum = record.getProductLastNum();

        //每天用量
        Integer oneNum = null;
        if (oneToTimes != null && oneUseNum != null) {
            oneNum = oneToTimes * oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //当有日用量和商品余量的时候 ==> 计算可以吃到哪一天
        if (oneNum != null && productLastNum != null && oneNum > 0  && productLastNum > 0) {
            //可以吃多少天 = 余量 ➗ 每天吃多少
            int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
            //可以吃到哪一天(截至日期当前的开始时间)
            Date dueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));
            memberProductEffect.setDueDate(dueDate);
        }
        //保存数据
        int result = memberProductEffectMapper.insertSelective(memberProductEffect);
        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败!");
        }
        //查询更新后的记录(用于保存历史记录表)
        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // 添加记录
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(now);

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
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "id不能为空!");
        }
        if (StringUtils.isEmpty(record.getMemberCard())) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "memberCard不能为空!");
        }
        //查询客户是否存在
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在!");
        }
        //查询之前的数据
        MemberProductEffect memberProductEffectBefore = memberProductEffectMapper.selectByPrimaryKey(record.getId());
        if (memberProductEffectBefore == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "记录不存在!");
        }
        //系统当前时间
        DateTime now = DateUtil.date();
        //创建更新对象
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(now);

        //每天吃几次
        Integer oneToTimes = record.getOneToTimes();
        //每次吃多少
        Integer oneUseNum = record.getOneUseNum();
        //商品余量
        Integer productLastNum = record.getProductLastNum();

        //每天用量
        Integer oneNum = null;
        if (oneToTimes != null && oneUseNum != null) {
            oneNum = oneToTimes * oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //当有日用量和商品余量的时候 ==> 计算可以吃到哪一天
        if (oneNum != null && productLastNum != null && oneNum > 0  && productLastNum > 0) {
            //可以吃多少天 = 余量 ➗ 每天吃多少
            int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
            //可以吃到哪一天(截至日期当前的开始时间)
            Date dueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));
            memberProductEffect.setDueDate(dueDate);
        }
        //更新数据
        int result = memberProductEffectMapper.updateByPrimaryKeySelective(memberProductEffect);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "数据保存失败!");
        }
        //查询更新后的记录(用于保存历史记录表)
        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // 添加记录
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setBeforeData(JSONUtil.toJsonPrettyStr(memberProductEffectBefore));
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(now);
        //保存
        result =  memberProductEffectRecordMapper.insertSelective(modifyRecord);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "记录数据保存失败!");
        }
        return result;
    }

    /**
     * 批量修改客户辅营效果记录
     * @param records
     * @return
     */
    @Transactional
    public ComResponse<Boolean> batchSaveProductEffect(List<MemberProductEffectInsertVO> records) {
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
    public ComResponse<Boolean> batchModifyProductEffect(String userNo,List<MemberProductEffectUpdateVO> records) {
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
        Integer RETURN_VISIT_RULES_CONFIG_DAY = WorkOrderClientAPI.queryReturnVisitRules();
        log.info("update member product last num:查询配置规则,当前配置为：{}",RETURN_VISIT_RULES_CONFIG_DAY);
        //系统当前时间
        DateTime now = DateUtil.date();
        //系统当前时间0点0分0秒
        Date currentDateStart = DealDateUtil.getStart(now);
        int minPrimaryKey = 0;//最小的id从0开始
        int pageSize = 2_000;//每页查询的条数
        int selectCount = 0;//实际查询的数量
        boolean hasNext = true;
        List<MemberProductEffect> updateLastNumList = new ArrayList<>();
        //停止服用商品效果记录的id
        List<Integer> stoptakingIdList = new ArrayList<>();
        //客户编号
        List<String> memberCardList = new ArrayList<>();
        //计算商品服用完日期和当前时间的天数差
        long betweenDay;
        //分页查询顾客服用商品
        List<MemberProductEffect> list;

        Integer oneNum;//每天吃多少
        Integer productLastNum;//商品余量
        Date dueDate;//商品服用完日期
        Integer count = 0;
        while (hasNext) {
            //分页查询顾客的商品服用效果
            list = memberProductEffectMapper.selectMemberProductEffectByPage(minPrimaryKey,pageSize);
            //获取实际查询的记录条数
            selectCount = list.size();
            //查询出来的数据小于pageSize，则当处理完当前的数据后，跳出while循环
            if (selectCount < pageSize) {
                //没有查询出数据，直接跳出while循环
                if (selectCount == 0) {
                    break;
                }
                hasNext = false;
            }
            minPrimaryKey = list.get(selectCount - 1).getId();
            //更新数据
            for (MemberProductEffect item : list) {
                productLastNum = item.getProductLastNum();
                oneNum = item.getEatingTime();//获取每天吃多少
                dueDate = item.getDueDate();
                //1.没有日用量 或者 没有余量 的不更新 2.没有商品服用完日期
                if (oneNum == null || productLastNum == null || oneNum <= 0 || productLastNum <= 0 || dueDate == null) {
                    continue;
                }
                //3.[已停服]的要根据商品余量重新计算商品的服用完日期(正常是日期延后一天)
                if (item.getTakingState() != null && item.getTakingState() == 2){
                    stoptakingIdList.add(item.getId());
                    continue;
                }

                // 4.处理正常服用的商品
                //可以吃多少天 = 余量 ➗ 每天吃多少
                int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
                //可以吃到哪一天(截至日期当前的开始时间)
                Date calculateDueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));

                //计算两个日期差，等于几就减去几天
                int between = (int)DateUtil.between(calculateDueDate,dueDate, DateUnit.DAY,false);
                //已经扣减过了，不用重复扣减
                if (between > 0) {
                    continue;
                }
                //计算扣减后的商品余量
                productLastNum = productLastNum - oneNum < 0 ? 0 : productLastNum - oneNum;
                //重新设置商品剩余量(余量-日用量)
                item.setProductLastNum(productLastNum);
                //商品余量为0时，更新商品的服用状态为停服，订单签收时再更新为正常服用
                if (productLastNum == 0) {
                    item.setTakingState(2);
                }
                //记录需要更新余量的商品
                updateLastNumList.add(item);
                //更新商品余量
                if (updateLastNumList.size() == 100) {
                    count = memberProductEffectMapper.updateMemberProductLastNumByPrimaryKeys(updateLastNumList);
                    log.info("update member product last num: record count:{}",count);
                    updateLastNumList.clear();
                }

                /**
                 * 判断是否需要生成次日回访的工单
                 */
                //小于最小服用天数
                betweenDay = DateUtil.between(currentDateStart, item.getDueDate(), DateUnit.DAY,false);
                if (betweenDay >= 0 && betweenDay < RETURN_VISIT_RULES_CONFIG_DAY){
                    //判断缓存是否存在(防止重复回访)
                    if (!CacheForProductEffectUtil.getAndSetNx(item.getMemberCard())) {
                        memberCardList.add(item.getMemberCard());
                    }
                }
            }
            //更新已经停服的商品的服用完日期
            if (CollectionUtil.isNotEmpty(stoptakingIdList)) {
                count = memberProductEffectMapper.updateMemberProductDueDateByPrimaryKeys(stoptakingIdList);
                log.info("update member product stop taking due date: record count:{}",count);
                stoptakingIdList.clear();
            }
            //更新商品剩余量
            if (CollectionUtil.isNotEmpty(updateLastNumList)) {
                count = memberProductEffectMapper.updateMemberProductLastNumByPrimaryKeys(updateLastNumList);
                log.info("update member product last num: record count:{}",count);
                updateLastNumList.clear();
            }
            //生成次日回访的工单
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
