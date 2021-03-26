package cn.net.yzl.crm.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.mongo.MemberQuestionnaireDao;
import cn.net.yzl.crm.customer.dto.MemberQuwarionnireDTO;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaire;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaireDTO;
import cn.net.yzl.crm.customer.service.MemberQuestionnaireService;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberQuestionnaireServiceImpl implements MemberQuestionnaireService {

    //集合名称
    private String COLLECTION_NAME = "member_questionnaire";

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberQuestionnaireDao memberQuestionnaireDao;

    @Override
    public Boolean saveQuestionnaire(MemberQuestionnaire memberQuestionnaire) {
        //保存数据
        Boolean result = this.saveQuestionnaireList(Arrays.asList(memberQuestionnaire));
        return result;
    }

    /**
     * 批量保存调查问卷
     * wangzhe
     * 2021-03-25
     * @param memberQuestionnaires 待保存的调查问卷集合
     * @return
     */
    @Override
    public Boolean saveQuestionnaireList(List<MemberQuestionnaire> memberQuestionnaires) {
        if (CollectionUtil.isEmpty(memberQuestionnaires)) {
            return Boolean.TRUE;
        }
        Date now = new Date();
        //按顾客卡号分组
        Map<String,List<MemberQuestionnaire>> requestMemberMap = memberQuestionnaires.stream().collect(Collectors.groupingBy(MemberQuestionnaire::getMemberCard));

        //按顾客卡号分组
        Set<String> memberCardSet = requestMemberMap.keySet();
        ArrayList<String> memberCardList = new ArrayList<>(memberCardSet);
        //查询是否包含不存在的顾客
        List<MemberOrderStatViewModel> memberList = memberMapper.getMemberList(memberCardList);
        if (CollectionUtil.isEmpty(memberList) || memberList.size() != memberCardList.size()){
            throw new BizException(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(), "顾客卡号不存在!");
        }

        ////按顾客卡号分组查询之前的顾客问卷
        Map<String, List<MemberQuestionnaire>> beforeMemberMap = memberQuestionnaireDao.getMemberQuestionnairesByOld(memberCardList);

        //遍历需要保存的问卷
        for (Map.Entry<String, List<MemberQuestionnaire>> entry : requestMemberMap.entrySet()) {
            String memberCard = entry.getKey();
            //获取已经存在的调查问卷
            List<MemberQuestionnaire> beforeQuestionnaireList = beforeMemberMap.get(memberCard);
            Map<String, MemberQuestionnaire> groupBySeqNoMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(beforeQuestionnaireList)) {
                for (MemberQuestionnaire beforeQuestionnaire : beforeQuestionnaireList) {
                    groupBySeqNoMap.put(beforeQuestionnaire.getSeqNo(), beforeQuestionnaire);
                }
            }
            //每个顾客对应的问卷集合
            List<MemberQuestionnaire> questionnaireList = entry.getValue();
            for (MemberQuestionnaire questionnaire : questionnaireList) {
                //对应序列号的上次问卷
                MemberQuestionnaire memberBeforeQuestionnaire = groupBySeqNoMap.get(questionnaire.getSeqNo());
                if (memberBeforeQuestionnaire == null) {
                    //设置创建时间
                    questionnaire.setCreateTime(now);
                }else{
                    questionnaire.set_id(memberBeforeQuestionnaire.get_id());
                }
                //设置更新时间
                questionnaire.setUpdateTime(now);
                memberQuestionnaireDao.saveQuestionnaire(questionnaire);
            }
        }
        //Boolean result = memberQuestionnaireDao.insertAll(memberQuestionnaires, COLLECTION_NAME);

        return Boolean.TRUE;
    }

    @Override
    public Page<MemberQuestionnaireDTO> getQuestionnaireByPage(MemberQuwarionnireDTO searchDTO) {
        if (searchDTO.getCurrentPage() == null || searchDTO.getCurrentPage() == 0) {
            searchDTO.setCurrentPage(1);
        }
        if (searchDTO.getPageSize() == null || searchDTO.getPageSize() == 0) {
            searchDTO.setPageSize(10);
        }
        Page<MemberQuestionnaireDTO> page = memberQuestionnaireDao.getQuestionnaireByPage(searchDTO);
        List<MemberQuestionnaireDTO> items = page.getItems();
        if (CollectionUtil.isNotEmpty(items)) {
            Map<String,List<MemberQuestionnaireDTO>> map = items.stream().collect(Collectors.groupingBy(MemberQuestionnaireDTO::getMemberCard));

            Set<String> memberCardSet = map.keySet();
            ArrayList<String> memberCardList = new ArrayList<>(memberCardSet);
            List<MemberOrderStatViewModel> memberList = memberMapper.getMemberList(memberCardList);

            Map<String, MemberOrderStatViewModel> memberMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(memberList)){
                for (MemberOrderStatViewModel member : memberList) {
                    memberMap.put(member.getMember_card(), member);
                }
            }
            for (MemberQuestionnaireDTO item : items) {
                MemberOrderStatViewModel model = memberMap.get(item.getMemberCard());
                if (model == null) {
                    continue;
                }
                item.setSex(model.getSex());
                if (item.getSex() == 0) {
                    item.setSexName("男");
                } else if (item.getSex() == 1) {
                    item.setSexName("女");
                }else{
                    item.setSexName("未知");
                }
                item.setGradeId(model.getM_grade_id());
                item.setGradeName(model.getM_grade_name());
                item.setMemberName(model.getMember_name());
            }
        }
        return page;

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
