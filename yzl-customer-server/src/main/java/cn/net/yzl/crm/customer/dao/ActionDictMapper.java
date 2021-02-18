package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.dto.member.ActionDictDto;
import cn.net.yzl.crm.customer.viewmodel.memberActionModel.ActionDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: lichanghong
 * @Description: 顾客行为dao层
 * @Date: 2021/1/25 4:33 下午
 */

@Repository
public interface ActionDictMapper {

    int deleteByPrimaryKey(@Param("id") Integer id,@Param("updator") String updator);

    int deleteByType(@Param("typeId") Integer typeId, @Param("updator") String updator);

    int insertSelective(ActionDictDto record);

    ActionDict selectByPrimaryKey(Integer id);

    List<ActionDict> selectByType(Integer type);

    List<ActionDict> selectByTypeAndName(@Param("type") Integer type,@Param("name") String name);

    int updateByPrimaryKeySelective(ActionDictDto record);

    int selectCountForRelationByDid(Integer id);

    int selectCountForRelationByType(Integer type);

    Integer updateActionDictWhereStatusIs2();
}