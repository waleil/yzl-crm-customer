//package cn.net.yzl.crm.customer.service.impl.memberDictImpl;
//
//import cn.net.yzl.crm.customer.dao.memberDictRelation.MemberAgeRelationMapper;
//import cn.net.yzl.crm.customer.service.memberDict.MemberRelationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MemberRelationServiceImpl implements MemberRelationService {
//
//    @Autowired
//    private MemberAgeRelationMapper memberAgeRelationMapper;
//
//    @Autowired
//    private MemberContactTimeRelationMapper memberContactTimeRelationMapper;
//
//
//    @Override
//    public List<MemberAgeRelation> selectAgeRelationByMemberCard(String card) {
//        return memberAgeRelationMapper.selectByMemberCard(card);
//    }
//
//    @Override
//    public int memberAgeRelationSaveUpdate(List<MemberAgeRelationDto> memberAgeRelationDtos) {
//        int num=0;
//        if(memberAgeRelationDtos!=null && memberAgeRelationDtos.size()>0){
//            String cardNo=memberAgeRelationDtos.get(0).getMemberCard();
//            //查询库中所有字典用于比对
//            List<MemberAgeRelation> memberAgeRelations = memberAgeRelationMapper.selectByMemberCard(cardNo);
//            List<MemberAgeRelation> memberAgeRelations2=new ArrayList<MemberAgeRelation>();
//            memberAgeRelations2.addAll(memberAgeRelations);
//            //如果表中字典为空，则所有数据都为新增
//            if(memberAgeRelationDtos==null || memberAgeRelationDtos.size()<1 ){
//                for (MemberAgeRelationDto memberAgeRelationDto : memberAgeRelationDtos) {
//                    if(memberAgeRelationDto.getId()==null){
//                        num+=memberAgeRelationMapper.insertSelective(memberAgeRelationDto);
//                    }
//                }
//            }else{
//
//                for (MemberAgeRelationDto memberAgeRelationDto : memberAgeRelationDtos) {
//                    if(memberAgeRelationDto.getId()==null){
//                        //传入参数id为空则为新增
//                        num+=memberAgeRelationMapper.insertSelective(memberAgeRelationDto);
//                    }else if(memberAgeRelationDto.getId().intValue()==0){
//                        //传入参数id为0则为清空
//                        num+=memberAgeRelationMapper.deleteByMemberCard(cardNo);
//                    }else{
//                        //传入参数id有值则进行删除,并移除库中被提及id的数据，则表中剩余不被提交的数据全部被删除
//                        for (MemberAgeRelation memberAgeRelation : memberAgeRelations) {
//                            if(memberAgeRelation.getId().intValue()==memberAgeRelationDto.getId().intValue()){
//                                num+=memberAgeRelationMapper.updateByPrimaryKeySelective(memberAgeRelationDto);
//                                memberAgeRelations2.remove(memberAgeRelation);
//                            }
//
//                        }
//
//                    }
//                }
//                //表中剩余不被提交的数据全部被删除
//                for (MemberAgeRelation memberAgeRelation : memberAgeRelations2) {
//                    num+=memberAgeRelationMapper.deleteByPrimaryKey(memberAgeRelation.getId());
//                }
//            }
//        }
//        return num;
//    }
//
//    @Override
//    public int memberContactTimeRelationSaveUpdate(List<MemberContactTimeRelationDto> memberContactTimeRelationDtos) {
//        int num=0;
//        if(memberContactTimeRelationDtos!=null && memberContactTimeRelationDtos.size()>0){
//            String cardNo=memberContactTimeRelationDtos.get(0).getMemberCard();
//            //查询库中所有字典用于比对
//            List<MemberContactTimeRelation> memberAgeRelations = memberContactTimeRelationMapper.selectByMemberCard(cardNo);
//            List<MemberContactTimeRelation> memberAgeRelations2=new ArrayList<MemberContactTimeRelation>();
//            memberAgeRelations2.addAll(memberAgeRelations);
//            //如果表中字典为空，则所有数据都为新增
//            if(memberContactTimeRelationDtos==null || memberContactTimeRelationDtos.size()<1 ){
//                for (MemberContactTimeRelationDto memberContactTimeRelationDto : memberContactTimeRelationDtos) {
//                    if(memberContactTimeRelationDto.getId()==null){
//                        num+=memberContactTimeRelationMapper.insertSelective(memberContactTimeRelationDto);
//                    }
//                }
//            }else{
//
//                for (MemberContactTimeRelationDto memberContactTimeRelationDto : memberContactTimeRelationDtos) {
//                    if(memberContactTimeRelationDto.getId()==null){
//                        //传入参数id为空则为新增
//                        num+=memberContactTimeRelationMapper.insertSelective(memberContactTimeRelationDto);
//                    }else if(memberContactTimeRelationDto.getId().intValue()==0){
//                        //传入参数id为0则为清空
//                        num+=memberContactTimeRelationMapper.deleteByMemberCard(cardNo);
//                    }else{
//                        //传入参数id有值则进行删除,并移除库中被提及id的数据，则表中剩余不被提交的数据全部被删除
//                        for (MemberContactTimeRelation memberContactTimeRelation : memberAgeRelations) {
//                            if(memberContactTimeRelation.getId().intValue()==memberContactTimeRelationDto.getId().intValue()){
//                                num+=memberContactTimeRelationMapper.updateByPrimaryKeySelective(memberContactTimeRelationDto);
//                                memberAgeRelations2.remove(memberContactTimeRelation);
//                            }
//
//                        }
//
//                    }
//                }
//                //表中剩余不被提交的数据全部被删除
//                for (MemberContactTimeRelation memberContactTimeRelation : memberAgeRelations) {
//                    num+=memberContactTimeRelationMapper.deleteByPrimaryKey(memberContactTimeRelation.getId());
//                }
//            }
//        }
//        return num;
//    }
//
//    @Override
//    public List<MemberContactTimeRelation> selectContactTimeRelationByMemberCard(String cardNo) {
//        return memberContactTimeRelationMapper.selectByMemberCard(cardNo);
//    }
//}
