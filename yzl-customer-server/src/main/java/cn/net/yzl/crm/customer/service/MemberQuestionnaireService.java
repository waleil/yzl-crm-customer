package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.MemberQuwarionnireDTO;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaire;

import java.util.List;

public interface MemberQuestionnaireService {


    /**
     * 保存调查问卷
     * @param memberQuestionnaire
     * @return
     */
    Boolean saveQuestionnaire(MemberQuestionnaire memberQuestionnaire);

    /**
     * 分页获取调查问卷
     * @param searchDTO
     * @return
     */
    Page<MemberQuestionnaire> getQuestionnaireByPage(MemberQuwarionnireDTO searchDTO);

    /**
     * 根据顾客卡号查询顾客的调查问卷
     * wangzhe
     * 2021-03-25
     * @param memberCard
     * @return
     */
    List<MemberQuestionnaire> getMemberQuestionnaireByMemberCard(String memberCard);

    /**
     * 根据主键id查询顾客的调查问卷
     * wangzhe
     * 2021-03-25
     * @param primaryKey
     * @return
     */
    MemberQuestionnaire getMemberQuestionnaireById(String primaryKey);

    Boolean saveQuestionnaireList(List<MemberQuestionnaire> memberQuestionnaires);
}
