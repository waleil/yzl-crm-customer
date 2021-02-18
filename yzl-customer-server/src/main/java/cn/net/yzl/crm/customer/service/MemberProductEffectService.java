package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberProductEffectDTO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectInsertVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;

import java.util.List;

public interface MemberProductEffectService {


    /**
     * 添加记录
     * wangzhe
     * 20221-01-27
     * @param record
     * @return
     */
    ComResponse save(MemberProductEffectInsertVO record);

    ComResponse batchSaveProductEffect(List<MemberProductEffectInsertVO> record);

    /**
     * 批量添加记录
     * wangzhe
     * 20221-01-27
     * @param record
     * @return
     */
    ComResponse batchModifyProductEffect(List<MemberProductEffectUpdateVO> record);

    ComResponse<List<MemberProductEffectDTO>> getProductEffects(MemberProductEffectSelectVO productEffect);

    ComResponse<Boolean> updateMemberProductLastNumAndCreateWorkOrder();
}

