package cn.net.yzl.crm.customer.dao.mongo;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.MemberQuwarionnireDTO;
import cn.net.yzl.crm.customer.dto.crowdgroup.GroupRefMember;
import cn.net.yzl.crm.customer.mongomodel.questionnaire.MemberQuestionnaire;
import cn.net.yzl.crm.customer.sys.BizException;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import cn.net.yzl.logger.annotate.SysAccessLog;
import cn.net.yzl.logger.enums.DefaultDataEnums;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MemberQuestionnaireDao extends MongoBaseDao<MemberQuestionnaire> {
    //集合名称
    private String COLLECTION_NAME = "member_questionnaire";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    protected Class<MemberQuestionnaire> getEntityClass() {
        return MemberQuestionnaire.class;
    }

    /**
     * 保存调查问卷
     * wangzhe
     * 2021-03-24
     * @param memberQuestionnaire
     */
    @SysAccessLog(logKey = "saveMemberQuestionnaire",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.ADD)
    public Integer saveQuestionnaire(MemberQuestionnaire memberQuestionnaire) {
        if (memberQuestionnaire == null ){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        /*if ("1".equals(memberQuestionnaire.getSeqNo())) {
            QuestionnaireURJ questionnaire = memberQuestionnaire.getQuestionnaire();
        }
        else if ("1".equals(memberQuestionnaire.getSeqNo())) {
            QuestionnaireZTJ questionnaire = memberQuestionnaire.getQuestionnaire();
        }*/

        mongoTemplate.save(memberQuestionnaire);

        return 1;

    }


    /**
     * 通过主键id查询顾客下的所有调查问卷
     * wangzhe
     * 2021-03-24
     * @param primaryKey 主键id
     * @return
     */
    @SysAccessLog(logKeyParamName = "primaryKey",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public MemberQuestionnaire getMemberQuestionnaireById(String primaryKey) {
        if (StringUtil.isNullOrEmpty(primaryKey)) {
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(primaryKey));

        MemberQuestionnaire questionnaire = mongoTemplate.findOne(query, getEntityClass());

        return questionnaire;
    }

    /**
     * 通过顾客卡号查询顾客下的所有调查问卷
     * wangzhe
     * 2021-03-24
     * @param memberCard 顾客卡号
     * @return
     */
    @SysAccessLog(logKeyParamName = "memberCard",source = DefaultDataEnums.Source.MEMORY_CACHE,action = DefaultDataEnums.Action.QUERY)
    public List<MemberQuestionnaire> getMemberQuestionnaireByMemberCard(String memberCard) {
        if (StringUtil.isNullOrEmpty(memberCard)){
            throw new BizException(ResponseCodeEnums.PARAMS_ERROR_CODE);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("memberCard").is(memberCard));

        List<MemberQuestionnaire> questionnaireList = mongoTemplate.find(query, getEntityClass());

        return questionnaireList;
    }


    /**
     * 分页获取顾客调查问卷列表
     * wangzhe
     * 2021-03-25
     * @param searchDTO 查询条件对象
     * @return
     */
    public Page<MemberQuestionnaire> getQuestionnaireByPage(MemberQuwarionnireDTO searchDTO) {
        List<Criteria> criteriaList = new ArrayList<>();
        String memberCard = searchDTO.getMemberCard();
        String memberName = searchDTO.getMemberName();
        String startTime = searchDTO.getStartTime();
        String endTime = searchDTO.getEndTime();
        String seqNo = searchDTO.getSeqNo();
        Integer sex = searchDTO.getSex();

        if (!StringUtils.isEmpty(memberCard)) {
            criteriaList.add(Criteria.where("memberCard").is(memberCard));
        }
        if (!StringUtils.isEmpty(memberName)) {
            criteriaList.add(Criteria.where("memberName").is(memberName));//.regex("^.*" +memberName+ ".*$"));
        }

        if (!StringUtils.isEmpty(startTime)){
            Criteria.where("updateTime").gte(MongoDateHelper.formatD(startTime));
        }

        if (!StringUtils.isEmpty(endTime)){
            Criteria.where("updateTime").lte(MongoDateHelper.formatD(endTime));
        }


        Query query = new Query();
        if (CollectionUtil.isNotEmpty(criteriaList)) {
            int size = criteriaList.size();
            Criteria [] criterias = new Criteria[criteriaList.size()];
            for(int i = 0 ; i < size; i++){
                criterias[i] = criteriaList.get(i);
            }
            Criteria criteria = new Criteria();
            criteria.andOperator(criterias);
            query.addCriteria(criteria);
        }
        //先求总数
        int total = (int) mongoTemplate.count(query, COLLECTION_NAME);

        int skip = (searchDTO.getCurrentPage() - 1) * searchDTO.getPageSize();
        query.skip(skip).limit(searchDTO.getPageSize());
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        query.with(sort);
        query.fields().include("_id").include("memberCard").include("memberName").include("sex").include("name").include("questionnaire");
        List<MemberQuestionnaire> questionnaireList = mongoTemplate.find(query, getEntityClass());
        for (MemberQuestionnaire item : questionnaireList) {
            memberCard = item.getMemberCard();
        }
        //mongoTemplate.count计算总数
        Page page = new Page();

        PageParam pageParam = new PageParam();
        pageParam.setTotalCount(total);
        pageParam.setPageTotal(total % searchDTO.getPageSize() == 0 ? total / searchDTO.getPageSize() : total / searchDTO.getPageSize() + 1);
        pageParam.setPageNo(searchDTO.getCurrentPage());

        page.setPageParam(pageParam);
        page.setItems(questionnaireList);
        return page;
    }

    public Map<String,List<MemberQuestionnaire>> getMemberQuestionnairesByOld(List<String> memberCards) {
        if (CollectionUtil.isEmpty(memberCards)) {
            return Collections.EMPTY_MAP;
        }

        //查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("memberCard").in(memberCards));
        query.fields().include("_id").include("memberCard").include("seqNo");
        List<MemberQuestionnaire> list = mongoTemplate.find(query,this.getEntityClass(),COLLECTION_NAME);
        if(!CollectionUtil.isEmpty(list)){
            Map<String,List<MemberQuestionnaire>> map = list.stream().collect(Collectors.groupingBy(MemberQuestionnaire::getMemberCard));
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * 批量插入数据
     * wangzhe
     * 2021-03-26
     * @param list
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> Boolean insertAll(List<T> list, String collectionName) {
        BulkOperations bulkOperations= mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,GroupRefMember.class,collectionName);
        bulkOperations.insert(list).execute();
        return true;
    }
}
