//package cn.net.yzl.crm.customer.service.impl.memberDictImpl;
//
//import cn.net.yzl.crm.customer.dao.memberDictMapper.AgeDictMapper;
//import cn.net.yzl.crm.customer.service.memberDict.MemberDictService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MemberDictServiceImpl implements MemberDictService {
//
//    @Autowired
//    private AgeDictMapper ageDictMapper;
//
//    @Autowired
//    private ContactTimeDictMapper contactTimeDictMapper;
//
//    @Override
//    public List<AgeDict> getAgeDictList(){
//        List<AgeDict> ageDicts = ageDictMapper.selectAll();
//        return ageDicts;
//    }
//
//    @Transactional
//    @Override
//    public int memberAgeSaveUpdate(List<AgeDictDto> ageDictDtos) {
//        int num=0;
//        if(ageDictDtos!=null && ageDictDtos.size()>0){
//            String updator=ageDictDtos.get(0).getUpdator();
//          //查询库中所有字典用于比对
//            List<AgeDict> ageDicts = ageDictMapper.selectAll();
//            List<AgeDict> ageDicts2=new ArrayList<AgeDict>();
//            ageDicts2.addAll(ageDicts);
//            //如果表中字典为空，则所有数据都为新增
//            if(ageDicts==null || ageDicts.size()<1 ){
//                for (AgeDictDto ageDictDto:ageDictDtos) {
//                    if(ageDictDto.getId()==null){
//                    ageDictDto.setCreator(updator);
//                    num+=ageDictMapper.insertSelective(ageDictDto);
//                    }
//                }
//            }else{
//                for (AgeDictDto ageDictDto : ageDictDtos) {
//                    if(ageDictDto.getId()==null){
//                        //传入参数id为空则为新增
//                        num+=ageDictMapper.insertSelective(ageDictDto);
//                    }else if(ageDictDto.getId().intValue()==0){
//                        //传入参数id为0则为清空
//                        num+=ageDictMapper.deleteAll();
//                    }else{
//                        //传入参数id有值则进行删除,并移除库中被提及id的数据，则表中剩余不被提交的数据全部被删除
//                        for (AgeDict ageDict : ageDicts) {
//                            if(ageDict.getId().intValue()==ageDictDto.getId().intValue()){
//                                num+=ageDictMapper.updateByPrimaryKeySelective(ageDictDto);
//                                ageDicts2.remove(ageDict);
//                            }
//                        }
//                    }
//                }
//                        //表中剩余不被提交的数据全部被删除
//                for (AgeDict ageDict : ageDicts2) {
//                    num+=ageDictMapper.deleteByPrimaryKey(ageDict.getId(),updator);
//                }
//            }
//        }
//        return num;
//    }
//
//
//    @Override
//    public List<ContactTimeDict> getContactTimeDictList(){
//        List<ContactTimeDict> contactTimeDicts = contactTimeDictMapper.selectAll();
//        return contactTimeDicts;
//    }
//
//    @Transactional
//    @Override
//    public int memberContactTimeSaveUpdate(List<ContactTimeDictDto> contactTimeDictDtos) {
//        int num=0;
//        if(contactTimeDictDtos!=null && contactTimeDictDtos.size()>0){
//            //查询库中所有字典用于比对
//            List<ContactTimeDict> contactTimeDicts = contactTimeDictMapper.selectAll();
//            List<ContactTimeDict> contactTimeDicts2=new ArrayList<ContactTimeDict>();
//            contactTimeDicts2.addAll(contactTimeDicts);
//            //如果表中字典为空，则所有数据都为新增
//            if(contactTimeDicts==null || contactTimeDicts.size()<1 ){
//                for (ContactTimeDictDto contactTimeDictDto : contactTimeDictDtos) {
//                    if(contactTimeDictDto.getId()==null){
//                        num+=contactTimeDictMapper.insertSelective(contactTimeDictDto);
//                    }
//                }
//            }else{
//
//                for (ContactTimeDictDto contactTimeDictDto : contactTimeDictDtos) {
//                    if(contactTimeDictDto.getId()==null){
//                        //传入参数id为空则为新增
//                        num+=contactTimeDictMapper.insertSelective(contactTimeDictDto);
//                    }else if(contactTimeDictDto.getId().intValue()==0){
//                        //传入参数id为0则为清空
//                        num+=contactTimeDictMapper.deleteAll();
//                    }else{
//                        //传入参数id有值则进行删除,并移除表中被提及id的数据，则表中剩余不被提交的数据全部被删除
//                        for (ContactTimeDict contactTimeDict : contactTimeDicts) {
//                            if(contactTimeDict.getId().intValue()==contactTimeDictDto.getId().intValue()){
//                                num+=contactTimeDictMapper.updateByPrimaryKeySelective(contactTimeDictDto);
//                                contactTimeDicts2.remove(contactTimeDict);
//                            }
//                        }
//
//                    }
//                }
//                //表中剩余不被提交的数据全部被删除
//                for (ContactTimeDict contactTimeDict : contactTimeDicts2) {
//                    num+=contactTimeDictMapper.deleteByPrimaryKey(contactTimeDict.getId());
//                }
//            }
//        }
//
//        return num;
//    }
//}
