package cn.net.yzl.crm.customer.dao.memberDictMapper;

import cn.net.yzl.crm.customer.dto.member.ComprehensiveBehaviorDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberDictModel.ComprehensiveBehaviorDict;

public interface ComprehensiveBehaviorDictMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ComprehensiveBehaviorDictDto record);

    int insertSelective(ComprehensiveBehaviorDictDto record);

    ComprehensiveBehaviorDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ComprehensiveBehaviorDictDto record);

    int updateByPrimaryKey(ComprehensiveBehaviorDictDto record);
}