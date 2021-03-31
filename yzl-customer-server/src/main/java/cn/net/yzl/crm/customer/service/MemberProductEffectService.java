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
    ComResponse<Boolean> save(MemberProductEffectInsertVO record);

    ComResponse<Boolean> batchSaveProductEffect(List<MemberProductEffectInsertVO> record);

    /**
     * 批量添加记录
     * wangzhe
     * 20221-01-27
     * @param userNo
     * @param record
     * @return
     */
    ComResponse<Boolean> batchModifyProductEffect(String userNo,List<MemberProductEffectUpdateVO> record);

    ComResponse<List<MemberProductEffectDTO>> getProductEffects(MemberProductEffectSelectVO productEffect);

    ComResponse<Boolean> updateMemberProductLastNumAndCreateWorkOrder();
}

