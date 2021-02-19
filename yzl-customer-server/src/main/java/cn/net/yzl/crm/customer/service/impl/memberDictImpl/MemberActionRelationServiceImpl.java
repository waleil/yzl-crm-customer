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

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberActionRelationServiceImpl implements MemberActionRelationService {

    @Autowired
    private ActionDictMapper actionDictMapper;

    @Autowired
    private MemberActionRelationMapper memberActionRelationMapper;

    @Override
    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCardAndType(String card, Integer type) {
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndType(card, type);
        if(memberActionRelations==null || memberActionRelations.size()<1){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }
        return ComResponse.success(memberActionRelations);
    }

    @Override
    public ComResponse<List<MemberActionRelationList>> selectRelationTreeByMemberCard(String card) {
        List<MemberActionRelationList> memberActionRelations = memberActionRelationMapper.selectRelationTreeByMemberCard(card);
        if(CollectionUtil.isEmpty(memberActionRelations)){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }
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
        //验证是否该顾客已有该行为属性
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndDid(memberActionRelationDto.getMemberCard(), memberActionRelationDto.getDid());
        if(memberActionRelations !=null && memberActionRelations.size()>0){
            return  ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getCode(),ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getMessage());
        }
        //验证该综合行为字典是否存在
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
        //id不为空的时候判断字典表是否存在，当字典表中不存在的时候，则认为id输入错误
        Integer actionId = memberActionRelationDto.getId();
        if (actionId != null) {
            ActionDict actionDict = actionDictMapper.selectByPrimaryKey(actionId);
            if (actionDict == null) {
                return  ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
            }
        }else{
            //根据类型和名称获取行为偏好字典
            List<ActionDict> actionDicts = actionDictMapper.selectByTypeAndName(memberActionRelationDto.getType(), memberActionRelationDto.getDname());
            if(CollectionUtil.isEmpty(actionDicts)){
                //没有查询到 ->新增字典
                ActionDictDto actionDict = new ActionDictDto();
                actionDict.setCreator(memberActionRelationDto.getCreator());
                actionDict.setName(memberActionRelationDto.getDname());
                actionDict.setType(memberActionRelationDto.getType());
                actionDict.setDelFlag(2);
                actionDictMapper.insertSelective(actionDict);
                actionId = actionDict.getId();
            }else{
                //有着直接使用
                actionId = actionDicts.get(0).getId();
            }
            memberActionRelationDto.setDid(actionId);
        }
        //查询顾客行为偏好（通过会员卡号、did）
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCardAndDid(memberActionRelationDto.getMemberCard(), memberActionRelationDto.getDid());
        //顾客存在该行为 - 新增错误
        if(CollectionUtil.isNotEmpty(memberActionRelations)) {
            return ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getCode(), ResponseCodeEnums.MEMBER_ACTION_EXIST_ERROR.getMessage());
        }
        //顾客不存在该行为则 - 新增
        int insert = memberActionRelationMapper.insert(memberActionRelationDto);
        if(insert<1){
            return  ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
        }
        return ComResponse.success(insert);
    }


    /**
     * 根据id删除顾客的行为偏好
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
        //获取会员卡号
        String cardNo=memberActionRelationDtoList.get(0).getMemberCard();
        //关联字典表 获取顾客的行为偏好
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCard(cardNo);
        List<MemberActionRelation> memberActionRelations2=new ArrayList<>();
        memberActionRelations2.addAll(memberActionRelations);
        //如果表中字典为空，则所有数据都为新增
        if(CollectionUtil.isEmpty(memberActionRelationDtoList)){
            for (MemberActionRelationDto memberAgeRelationDto : memberActionRelationDtoList) {
                if(memberAgeRelationDto.getId()==null){
                    num+=memberActionRelationMapper.insert(memberAgeRelationDto);
                }
            }
        }else{

            for (MemberActionRelationDto memberActionRelationDto : memberActionRelationDtoList) {
                if(memberActionRelationDto.getId()==null){
                    //传入参数id为空则为新增
                    num+=memberActionRelationMapper.insert(memberActionRelationDto);
                }else if(memberActionRelationDto.getId().intValue()==0){
                    //传入参数id为0则为清空
                    num+=memberActionRelationMapper.deleteAllByCardNo(cardNo);
                }else{
                    //传入参数id有值则进行删除,并移除库中被提及id的数据，则表中剩余不被提及的数据全部被删除
                    for (MemberActionRelation memberAgeRelation : memberActionRelations) {
                        if(memberAgeRelation.getId().intValue()==memberActionRelationDto.getId().intValue()){
                            num+=memberActionRelationMapper.updateByPrimaryKey(memberActionRelationDto);
                            memberActionRelations2.remove(memberAgeRelation);
                        }

                    }

                }
            }
            //表中剩余不被提及的数据全部被删除
            for (MemberActionRelation memberAgeRelation : memberActionRelations2) {
                num+=memberActionRelationMapper.deleteByPrimaryKey(memberAgeRelation.getId());
            }
        }
        return ComResponse.success(num);
    }


}
