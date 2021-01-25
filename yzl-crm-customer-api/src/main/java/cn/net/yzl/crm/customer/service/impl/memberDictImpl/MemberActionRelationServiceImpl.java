package cn.net.yzl.crm.customer.service.impl.memberDictImpl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberActionRelationMapper;
import cn.net.yzl.crm.customer.dto.member.MemberActionRelationDto;
import cn.net.yzl.crm.customer.service.memberDict.MemberActionRelationService;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.MemberActionRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberActionRelationServiceImpl implements MemberActionRelationService {

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
    public ComResponse<List<MemberActionRelation>> selectRelationByMemberCard(String card) {
        List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCard(card);
        if(memberActionRelations==null || memberActionRelations.size()<1){
            return  ComResponse.fail(ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getCode(),ResponseCodeEnums.NO_MATCHING_RESULT_CODE.getMessage());
        }
        return ComResponse.success(memberActionRelations);
    }


    @Override
    public ComResponse<Integer> addRelation(MemberActionRelationDto memberActionRelationDto) {
        int insert = memberActionRelationMapper.insert(memberActionRelationDto);
        if(insert<1){
            return  ComResponse.fail(ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getCode(),ResponseCodeEnums.SAVE_DATA_ERROR_CODE.getMessage());
        }
        return ComResponse.success(insert);
    }


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
        if(memberActionRelationDtoList!=null && memberActionRelationDtoList.size()>0){
            String cardNo=memberActionRelationDtoList.get(0).getMemberCard();
            //查询库中所有字典用于比对
            List<MemberActionRelation> memberActionRelations = memberActionRelationMapper.selectRelationByMemberCard(cardNo);
            List<MemberActionRelation> memberActionRelations2=new ArrayList<MemberActionRelation>();
            memberActionRelations2.addAll(memberActionRelations);
            //如果表中字典为空，则所有数据都为新增
            if(memberActionRelationDtoList==null || memberActionRelationDtoList.size()<1 ){
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
        }
        return ComResponse.success(num);
    }


}
