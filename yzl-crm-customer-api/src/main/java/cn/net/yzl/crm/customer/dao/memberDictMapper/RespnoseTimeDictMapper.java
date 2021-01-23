package cn.net.yzl.crm.customer.dao.memberDictMapper;

import cn.net.yzl.crm.customer.dto.member.RespnoseTimeDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.RespnoseTimeDict;

public interface RespnoseTimeDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RespnoseTimeDictDto record);

    int insertSelective(RespnoseTimeDictDto record);

    RespnoseTimeDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RespnoseTimeDictDto record);

    int updateByPrimaryKey(RespnoseTimeDictDto record);
}