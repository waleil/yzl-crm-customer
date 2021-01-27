package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberProductEffectMapper;
import cn.net.yzl.crm.customer.dao.MemberProductEffectRecordMapper;
import cn.net.yzl.crm.customer.dto.member.MemberProductEffectDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberProductEffect;
import cn.net.yzl.crm.customer.model.db.MemberProductEffectRecord;
import cn.net.yzl.crm.customer.service.MemberProductEffectService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MemberProductEffectServiceImpl implements MemberProductEffectService {


    @Autowired
    MemberProductEffectMapper memberProductEffectMapper;
    @Autowired
    MemberProductEffectRecordMapper memberProductEffectRecordMapper;
    @Autowired
    MemberMapper memberMapper;


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    @Override
    public ComResponse save(MemberProductEffectVO record) {
        //查询客户是否存在
        Member member = memberMapper.selectMemberByCard(record.getMemberCard());
        if (member == null) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), "会员不存在");
        }

        //
        MemberProductEffect memberProductEffect = new MemberProductEffect();
        BeanUtil.copyProperties(record, memberProductEffect);
        record.setId(null);
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
        if (oneToTimes > 0 && oneUseNum > 0) {
            oneNum = oneToTimes*oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //商品服用完日期
        Integer eatDay = null;
        if (oneNum > 0 && productLastNum>0) {
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
    protected int modify(MemberProductEffectVO record) {
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
        Integer productLastNum = record.getProductLastNum();

        //每天用量
        Integer oneNum = null;
        if (oneToTimes > 0 && oneUseNum > 0) {
            oneNum = oneToTimes*oneUseNum;
        }
        //每天吃多少(计算)
        memberProductEffect.setEatingTime(oneNum);
        //商品服用完日期
        Integer eatDay = null;
        if (oneNum > 0 && productLastNum>0) {
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
        modifyRecord.setModifyTime(record.getUpateTime());
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
    public ComResponse batchModifyProductEffect(List<MemberProductEffectVO> records) {
        if (CollectionUtil.isEmpty(records)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"参数不能为空!");
        }
        for (MemberProductEffectVO record : records) {
            int result = this.modify(record);
            if (result != 1) {
                return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"记录数据保存失败!");
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





}
