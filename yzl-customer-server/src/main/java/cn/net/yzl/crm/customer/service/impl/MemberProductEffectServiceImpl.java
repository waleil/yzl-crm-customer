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
import cn.net.yzl.crm.customer.dto.member.MemberproductMinNumDTO;
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
        //????????????????????????
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "???????????????!");
        }
        //??????????????????
        DateTime now = DateUtil.date();
        //??????????????????
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(now);

        //???????????????
        Integer oneToTimes = record.getOneToTimes();
        //???????????????
        Integer oneUseNum = record.getOneUseNum();
        //????????????
        Integer productLastNum = record.getProductLastNum();

        //????????????
        Integer oneNum = null;
        if (oneToTimes != null && oneUseNum != null) {
            oneNum = oneToTimes * oneUseNum;
        }
        //???????????????(??????)
        memberProductEffect.setEatingTime(oneNum);
        //??????????????????????????????????????? ==> ???????????????????????????
        if (oneNum != null && productLastNum != null && oneNum > 0  && productLastNum > 0) {
            //?????????????????? = ?????? ??? ???????????????
            int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
            //?????????????????????(?????????????????????????????????)
            Date dueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));
            memberProductEffect.setDueDate(dueDate);
        }
        //????????????
        int result = memberProductEffectMapper.insertSelective(memberProductEffect);
        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "??????????????????!");
        }
        //????????????????????????(???????????????????????????)
        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // ????????????
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(now);

        //??????
        result =  memberProductEffectRecordMapper.insertSelective(modifyRecord);

        if (result < 1) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"????????????????????????!");
        }
        return ComResponse.success();
    }


    @Transactional
    protected int modify(MemberProductEffectUpdateVO record) {
        if (record.getId() == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "id????????????!");
        }
        if (StringUtils.isEmpty(record.getMemberCard())) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "memberCard????????????!");
        }
        //????????????????????????
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "???????????????!");
        }
        //?????????????????????
        MemberProductEffect memberProductEffectBefore = memberProductEffectMapper.selectByPrimaryKey(record.getId());
        if (memberProductEffectBefore == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "???????????????!");
        }
        //??????????????????
        DateTime now = DateUtil.date();
        //??????????????????
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        memberProductEffect.setUpateTime(now);

        //???????????????
        Integer oneToTimes = record.getOneToTimes();
        //???????????????
        Integer oneUseNum = record.getOneUseNum();
        //????????????
        Integer productLastNum = record.getProductLastNum();

        //????????????
        Integer oneNum = null;
        if (oneToTimes != null && oneUseNum != null) {
            oneNum = oneToTimes * oneUseNum;
        }
        //???????????????(??????)
        memberProductEffect.setEatingTime(oneNum);
        //??????????????????????????????????????? ==> ???????????????????????????
        if (oneNum != null && productLastNum != null && oneNum > 0  && productLastNum > 0) {
            //?????????????????? = ?????? ??? ???????????????
            int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
            //?????????????????????(?????????????????????????????????)
            Date dueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));
            memberProductEffect.setDueDate(dueDate);
        }
        //????????????
        int result = memberProductEffectMapper.updateByPrimaryKeySelective(memberProductEffect);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "??????????????????!");
        }
        //????????????????????????(???????????????????????????)
        MemberProductEffect memberProductEffectAfter = memberProductEffectMapper.selectByPrimaryKey(memberProductEffect.getId());

        // ????????????
        MemberProductEffectRecord modifyRecord = new MemberProductEffectRecord();
        modifyRecord.setBeforeData(JSONUtil.toJsonPrettyStr(memberProductEffectBefore));
        modifyRecord.setAfterData(JSONUtil.toJsonPrettyStr(memberProductEffectAfter));
        modifyRecord.setModifyNo(record.getUpdator());
        modifyRecord.setProductEffectId(memberProductEffect.getId());

        modifyRecord.setModifyTime(now);
        //??????
        result =  memberProductEffectRecordMapper.insertSelective(modifyRecord);

        if (result < 1) {
            throw new BizException(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(), "????????????????????????!");
        }
        return result;
    }

    /**
     * ????????????????????????????????????
     * @param records
     * @return
     */
    @Transactional
    public ComResponse<Boolean> batchSaveProductEffect(List<MemberProductEffectInsertVO> records) {
        if (CollectionUtil.isEmpty(records)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"??????????????????!");
        }
        for (MemberProductEffectInsertVO record : records) {
            ComResponse result = this.save(record);
            if (result.getStatus() != 1) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"????????????????????????!");
            }
        }
        return ComResponse.success();
    }


    /**
     * ????????????????????????????????????
     * @param records
     * @return
     */
    @Transactional
    public ComResponse<Boolean> batchModifyProductEffect(String userNo,List<MemberProductEffectUpdateVO> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            for (MemberProductEffectUpdateVO record : records) {
                //???????????????
                if (StringUtils.isEmpty(record.getUpdator())) {
                    record.setUpdator(userNo);
                }
                int result = this.modify(record);
                if (result != 1) {
                    return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"????????????????????????!");
                }
            }
        }
        return ComResponse.success();
    }


    /**
     * ??????????????????????????????
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
     * ?????????????????????????????????
     * wangzhe
     * 2021-02-05
     * @return
     */
    @Override/*22:00*/
    public ComResponse<Boolean> updateMemberProductLastNumAndCreateWorkOrder() {
        log.info("update member product last num: start,????????????{}",new Date());
        //??????????????????
        Integer RETURN_VISIT_RULES_CONFIG_DAY = WorkOrderClientAPI.queryReturnVisitRules();
        log.info("update member product last num:??????????????????,??????????????????{}",RETURN_VISIT_RULES_CONFIG_DAY);
        //??????????????????
        DateTime now = DateUtil.date();
        //??????????????????0???0???0???
        Date currentDateStart = DealDateUtil.getStart(now);
        int minPrimaryKey = 0;//?????????id???0??????
        int pageSize = 2_000;//?????????????????????
        int selectCount = 0;//?????????????????????
        boolean hasNext = true;
        List<MemberProductEffect> updateLastNumList = new ArrayList<>();
        //?????????????????????????????????id
        List<Integer> stoptakingIdList = new ArrayList<>();
        //????????????
        List<String> memberCardList = new ArrayList<>();
        //??????????????????????????????????????????????????????
        long betweenDay;
        //??????????????????????????????
        List<MemberProductEffect> list;

        Integer oneNum;//???????????????
        Integer productLastNum;//????????????
        Date dueDate;//?????????????????????
        Integer count = 0;
        while (hasNext) {
            //???????????????????????????????????????
            list = memberProductEffectMapper.selectMemberProductEffectByPage(minPrimaryKey,pageSize);
            //?????????????????????????????????
            selectCount = list.size();
            //???????????????????????????pageSize?????????????????????????????????????????????while??????
            if (selectCount < pageSize) {
                //????????????????????????????????????while??????
                if (selectCount == 0) {
                    break;
                }
                hasNext = false;
            }
            minPrimaryKey = list.get(selectCount - 1).getId();
            //????????????
            for (MemberProductEffect item : list) {
                productLastNum = item.getProductLastNum();
                oneNum = item.getEatingTime();//?????????????????????
                dueDate = item.getDueDate();
                //1.??????????????? ?????? ???????????? ???????????? 2.???????????????????????????
                if (oneNum == null || productLastNum == null || oneNum <= 0 || productLastNum <= 0 || dueDate == null) {
                    continue;
                }
                //3.[?????????]????????????????????????????????????????????????????????????(???????????????????????????)
                if (item.getTakingState() != null && item.getTakingState() == 2){
                    stoptakingIdList.add(item.getId());
                    continue;
                }

                // 4.???????????????????????????
                //?????????????????? = ?????? ??? ???????????????
                int eatDay = (int)Math.ceil(productLastNum / (float) oneNum);
                //?????????????????????(?????????????????????????????????)
                Date calculateDueDate = DateUtil.beginOfDay(DateUtil.offsetDay(now, eatDay - 1));

                //????????????????????????????????????????????????
                int between = (int)DateUtil.between(calculateDueDate,dueDate, DateUnit.DAY,false);
                //???????????????????????????????????????
                if (between > 0) {
                    continue;
                }
                //??????????????????????????????
                productLastNum = productLastNum - oneNum < 0 ? 0 : productLastNum - oneNum;
                //???????????????????????????(??????-?????????)
                item.setProductLastNum(productLastNum);
                //???????????????0????????????????????????????????????????????????????????????????????????????????????
                if (productLastNum == 0) {
                    item.setTakingState(2);
                }
                //?????????????????????????????????
                updateLastNumList.add(item);
                //??????????????????
                if (updateLastNumList.size() == 100) {
                    count = memberProductEffectMapper.updateMemberProductLastNumByPrimaryKeys(updateLastNumList);
                    log.info("update member product last num: record count:{}",count);
                    updateLastNumList.clear();
                }

                /**
                 * ?????????????????????????????????????????????
                 */
                //????????????????????????
                betweenDay = DateUtil.between(currentDateStart, item.getDueDate(), DateUnit.DAY,false);
                if (betweenDay >= 0 && betweenDay < RETURN_VISIT_RULES_CONFIG_DAY){
                    //????????????????????????(??????????????????)
                    if (!CacheForProductEffectUtil.getAndSetNx(item.getMemberCard())) {
                        memberCardList.add(item.getMemberCard());
                    }
                }
            }
            //?????????????????????????????????????????????
            if (CollectionUtil.isNotEmpty(stoptakingIdList)) {
                count = memberProductEffectMapper.updateMemberProductDueDateByPrimaryKeys(stoptakingIdList);
                log.info("update member product stop taking due date: record count:{}",count);
                stoptakingIdList.clear();
            }
            //?????????????????????
            if (CollectionUtil.isNotEmpty(updateLastNumList)) {
                count = memberProductEffectMapper.updateMemberProductLastNumByPrimaryKeys(updateLastNumList);
                log.info("update member product last num: record count:{}",count);
                updateLastNumList.clear();
            }
            //???????????????????????????
            if (CollectionUtil.isNotEmpty(memberCardList)){
                boolean result = WorkOrderClientAPI.productDosage(memberCardList);
                if (!result) {
                    log.info("update member product last num:??????????????????????????????!");
                }
                memberCardList.clear();
            }
        }
        log.info("update member product last num: end,????????????{}",new Date());
        return ComResponse.success(true);
    }

    @Override
    public List<MemberproductMinNumDTO> getMemberproductMinNumByMemberCards(List<String> memberCards) {
        return memberProductEffectMapper.getMemberproductMinNumByMemberCards(memberCards);
    }


}
