package cn.net.yzl.crm.customer.service.impl.memberDictImpl;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.ActionDictMapper;
import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.service.memberDict.ActionDictService;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionDictServiceImpl implements ActionDictService {

    @Autowired
    private ActionDictMapper actionDictMapper;

    @Override
    public ComResponse<List<ActionDict>> getDictListByType(Integer type) {
        List<ActionDict> actionDictList = actionDictMapper.selectByType(type);
        return ComResponse.success(actionDictList);
    }

    @Transactional
    @Override
    public ComResponse<Integer> saveUpdateActionDict(List<ActionDictDto> actionDictDtos) {
        int num=0;
        if(actionDictDtos!=null && actionDictDtos.size()>0){
            int type=actionDictDtos.get(0).getType();
            String updator=actionDictDtos.get(0).getUpdator();
            //查询库中所有字典用于比对
            List<ActionDict> actionDictList = actionDictMapper.selectByType(type);
            List<ActionDict> actionDictList2=new ArrayList<ActionDict>();
            actionDictList2.addAll(actionDictList);
            //如果表中字典为空，则所有数据都为新增
            if(actionDictList==null || actionDictList.size()<1 ){
                for (ActionDictDto actionDictDto : actionDictDtos) {
                    if(actionDictDto.getId()==null){
                        actionDictDto.setCreator(updator);
                        num+=actionDictMapper.insertSelective(actionDictDto);
                    }
                }
            }else{

                for (ActionDictDto actionDictDto : actionDictDtos) {
                    if(actionDictDto.getId()==null){
                        //传入参数id为空则为新增
                        actionDictDto.setCreator(updator);
                        num+=actionDictMapper.insertSelective(actionDictDto);
                    }else if(actionDictDto.getId().intValue()==0){
                        //传入参数id为0则为清空
                        int i = actionDictMapper.selectCountForRelationByType(type);
                        if(i<1){
                            num+=actionDictMapper.deleteByType(type,updator);
                        }else{
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                            return ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_USED_DELETE_ERROR.getCode(),ResponseCodeEnums.MEMBER_ACTION_USED_DELETE_ERROR.getMessage());
                        }

                    }else{
                        //传入参数id有值则进行删除,并移除表中被提及id的数据，则表中剩余不被提交的数据全部被删除
                        for (ActionDict actionDict : actionDictList) {
                            if(actionDict.getId().intValue()==actionDictDto.getId().intValue()){
                                num+=actionDictMapper.updateByPrimaryKeySelective(actionDictDto);
                                actionDictList2.remove(actionDict);
                            }
                        }

                    }
                }
                //表中剩余不被提交的数据全部被删除
                for (ActionDict contactTimeDict : actionDictList2) {
                    int i = actionDictMapper.selectCountForRelationByDid(contactTimeDict.getId());
                    if(i<1){
                        num+=actionDictMapper.deleteByPrimaryKey(contactTimeDict.getId(),updator);
                    }else{
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
                        return ComResponse.fail(ResponseCodeEnums.MEMBER_ACTION_USED_DELETE_ERROR.getCode(),ResponseCodeEnums.MEMBER_ACTION_USED_DELETE_ERROR.getMessage());
                    }

                }
            }
        }

        return ComResponse.success(num);
    }

    @Override
    public Integer updateActionDictWhereStatusIs2() {
        return actionDictMapper.updateActionDictWhereStatusIs2();
    }
}
