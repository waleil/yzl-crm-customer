package cn.net.yzl.crm.customer.service.impl.memberDictImpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.ActionDictMapper;
import cn.net.yzl.crm.customer.dao.MemberActionRelationMapper;
import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.service.memberDict.MemberActionRelationService;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelationList;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionDictList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberActionRelationServiceImpl implements MemberActionRelationService {

    @Autowired
    private ActionDictMapper actionDictMapper;

    @Autowired
    private MemberActionRelationMapper memberActionRelationMapper;

    @Override
    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCardAndType(String card, Integer type) {
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndType(card, type);
       /* if(memberActionRelations==null || memberActionRelations.size()<1){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }*/
        return ComResponse.success(memberActionRelations);
    }

    @Override
    public ComResponse<List<MemberActionRelationList>> selectRelationTreeByMemberCard(String card) {
        List<MemberActionRelationList> memberActionRelations = memberActionRelationMapper.selectRelationTreeByMemberCard(card);
        /*if(CollectionUtil.isEmpty(memberActionRelations)){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }*/
        return ComResponse.success(memberActionRelations);
    }


    @Override
    public ComResponse<List<MemberActionDictList>> getActionDictByMemberCard(String memberCard) {
        List<MemberActionDictList> memberActionRelations = memberActionRelationMapper.getActionDictByMemberCard(memberCard);
        if(CollectionUtil.isEmpty(memberActionRelations)){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }
        return ComResponse.success(memberActionRelations);
    }




    @Override
    public ComResponse<Integer> addRelation(MemberActionRelationDto memberActionRelationDto) {
        //??????????????????????????????????????????
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndDid(memberActionRelationDto.getMemberCard(), memberActionRelationDto.getDid());
        if(memberActionRelations !=null && memberActionRelations.size()>0){
            return  ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getCode(),ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getMessage());
        }
        //???????????????????????????????????????
        ActionDict actionDict = actionDictMapper.selectByPrimaryKey(memberActionRelationDto.getDid());
        if(actionDict==null){
            return  ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_NOT_EXIST_ERROR.getCode(),ResponseCodeEnums.MEMBER_ACTION_NOT_EXIST_ERROR.getMessage());
        }

        int insert = memberActionRelationMapper.insert(memberActionRelationDto);
        if(insert<1){
            return  ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
        }
        return ComResponse.success(insert);
    }

    @Transactional
    @Override
    public ComResponse<Integer> addRelationWithDict(MemberActionRelationDto memberActionRelationDto) {
        //id?????????????????????????????????????????????????????????????????????????????????????????????id????????????
        Integer actionId = memberActionRelationDto.getId();
        if (actionId != null) {
            ActionDict actionDict = actionDictMapper.selectByPrimaryKey(actionId);
            if (actionDict == null) {
                return  ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
            }
        }else{
            //?????????????????????????????????????????????
            List<ActionDict> actionDicts = actionDictMapper.selectByTypeAndName(memberActionRelationDto.getType(), memberActionRelationDto.getDname());
            if(CollectionUtil.isEmpty(actionDicts)){
                //??????????????? ->????????????
                ActionDictDto actionDict = new ActionDictDto();
                actionDict.setCreator(memberActionRelationDto.getCreator());
                actionDict.setName(memberActionRelationDto.getDname());
                actionDict.setType(memberActionRelationDto.getType());
                actionDict.setDelFlag(2);
                actionDict.setValue(memberActionRelationDto.getValue());
                actionDict.setValue2(memberActionRelationDto.getValue2());
                actionDictMapper.insertSelective(actionDict);
                actionId = actionDict.getId();
            }else{
                ActionDict actionDict = actionDicts.get(0);
                Integer delFlag = actionDict.getDelFlag();
                if (delFlag != null && delFlag == 0) {
                    return  ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"?????????????????????!");
                }
                //???????????????????????????????????????
                actionId = actionDict.getId();
            }
            memberActionRelationDto.setDid(actionId);
        }
        //????????????????????????????????????????????????did???
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndDid(memberActionRelationDto.getMemberCard(), memberActionRelationDto.getDid());
        //????????????????????? - ????????????
        if(CollectionUtil.isNotEmpty(memberActionRelations)) {
            return ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getCode(), ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getMessage());
        }
        //??????????????????????????? - ??????
        int insert = memberActionRelationMapper.insert(memberActionRelationDto);
        if(insert<1){
            return  ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
        }
        //?????????????????????????????????id
        return ComResponse.success(memberActionRelationDto.getDid());
    }


    /**
     * ??????id???????????????????????????
     * @param rid
     * @return
     */
    @Override
    public ComResponse<Integer> deleteRelation(Integer rid) {
        int num = memberActionRelationMapper.deleteByPrimaryKey(rid);
        if(num<1){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }
        return ComResponse.success(num);
    }

    @Transactional
    @Override
    public ComResponse<Integer> saveUpdateRelation(List<MemberActionRelationDto> memberActionRelationDtoList) {
        int num=0;
        if(CollectionUtil.isEmpty(memberActionRelationDtoList)){
            return ComResponse.success(num);
        }
        //??????????????????
        String cardNo=memberActionRelationDtoList.get(0).getMemberCard();
        //??????????????? ???????????????????????????
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCard(cardNo);
        List<MemberActionRelation> memberActionRelations2=new ArrayList<>();
        memberActionRelations2.addAll(memberActionRelations);
        //??????????????????????????????????????????????????????
        if(CollectionUtil.isEmpty(memberActionRelationDtoList)){
            for (MemberActionRelationDto memberAgeRelationDto : memberActionRelationDtoList) {
                if(memberAgeRelationDto.getId()==null){
                    num+=memberActionRelationMapper.insert(memberAgeRelationDto);
                }
            }
        }else{

            for (MemberActionRelationDto memberActionRelationDto : memberActionRelationDtoList) {
                if(memberActionRelationDto.getId()==null){
                    //????????????id??????????????????
                    num+=memberActionRelationMapper.insert(memberActionRelationDto);
                }else if(memberActionRelationDto.getId().intValue()==0){
                    //????????????id???0????????????
                    num+=memberActionRelationMapper.deleteAllByCardNo(cardNo);
                }else{
                    //????????????id?????????????????????,????????????????????????id???????????????????????????????????????????????????????????????
                    for (MemberActionRelation memberAgeRelation : memberActionRelations) {
                        if(memberAgeRelation.getId().intValue()==memberActionRelationDto.getId().intValue()){
                            num+=memberActionRelationMapper.updateByPrimaryKey(memberActionRelationDto);
                            memberActionRelations2.remove(memberAgeRelation);
                        }

                    }

                }
            }
            //????????????????????????????????????????????????
            for (MemberActionRelation memberAgeRelation : memberActionRelations2) {
                num+=memberActionRelationMapper.deleteByPrimaryKey(memberAgeRelation.getId());
            }
        }
        return ComResponse.success(num);
    }



    @Transactional
    @Override
    public ComResponse<Boolean> saveOrUpdateMemberActionRelation(String memberCard,String createNo,List<Integer> memberActionDIdList) {
        //??????null???
        if (CollectionUtil.isNotEmpty(memberActionDIdList)) {
            Iterator<Integer> iterator = memberActionDIdList.iterator();
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (next == null) {
                    iterator.remove();
                }
            }
        }
        if (CollectionUtil.isEmpty(memberActionDIdList)) {
            return ComResponse.success(true);
        }

        //??????did????????????
        List<ActionDict> actionDictByIds = actionDictMapper.getActionDictByIds(memberActionDIdList);
        if (CollectionUtil.isEmpty(actionDictByIds) || actionDictByIds.size() != memberActionDIdList.size()) {
            //??????????????????????????????
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"(?????????????????????????????????)????????????????????????!");
        }

        Map<Byte,List<ActionDict>> actionDictMap = actionDictByIds.stream()
                .collect(Collectors.groupingBy(ActionDict::getType));
        //??????????????????
        List<Integer> types = new ArrayList<>();
        //?????????????????????????????????
        List<MemberActionRelationDto> memberActions = new ArrayList<>();

        for (Map.Entry<Byte, List<ActionDict>> entry : actionDictMap.entrySet()) {
            Integer type = entry.getKey().intValue();
            types.add(type);
            List<ActionDict> actionDicts = entry.getValue();
            for (ActionDict item : actionDicts) {
                MemberActionRelationDto relationDto = new MemberActionRelationDto();
                relationDto.setMemberCard(memberCard);
                relationDto.setDid(item.getId());
                relationDto.setType(type);
                relationDto.setCreator(createNo);
                memberActions.add(relationDto);
            }
        }
        //????????????????????????????????????
        int result = memberActionRelationMapper.deleteMemberActionByMemberCardAndTypes(memberCard,types);
        //??????????????????????????????
        result = memberActionRelationMapper.insertBatch(memberActions);
        if (result < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"(????????????)????????????????????????!");
        }
        return ComResponse.success(true);

    }


}
