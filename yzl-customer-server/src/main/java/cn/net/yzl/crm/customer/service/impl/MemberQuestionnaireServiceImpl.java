package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dao.mongo.MemberQuestionnaireDao;
import cn.net.yzl.crm.customer.dto.MemberQuwarionnireDTO;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaire;
import cn.net.yzl.crm.customer.service.MemberQuestionnaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemberQuestionnaireServiceImpl implements MemberQuestionnaireService {

    @Autowired
    MemberQuestionnaireDao memberQuestionnaireDao;

    @Override
    public Boolean saveQuestionnaire(MemberQuestionnaire memberQuestionnaire) {
        Integer result = memberQuestionnaireDao.saveQuestionnaire(memberQuestionnaire);
        return Boolean.TRUE;
    }

    @Override
    public Page<MemberQuestionnaire> getQuestionnaireByPage(MemberQuwarionnireDTO searchDTO) {
        if (searchDTO.getCurrentPage() == null || searchDTO.getCurrentPage() == 0) {
            searchDTO.setCurrentPage(1);
        }
        if (searchDTO.getPageSize() == null || searchDTO.getPageSize() == 0) {
            searchDTO.setPageSize(10);
        }

        return memberQuestionnaireDao.getQuestionnaireByPage(searchDTO);

    }

    @Override
    public List<MemberQuestionnaire> getMemberQuestionnaireByMemberCard(String memberCard) {
        return memberQuestionnaireDao.getMemberQuestionnaireByMemberCard(memberCard);
    }

    @Override
    public MemberQuestionnaire getMemberQuestionnaireById(String primaryKey) {
        return memberQuestionnaireDao.getMemberQuestionnaireById(primaryKey);
    }
}
