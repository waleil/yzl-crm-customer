package cn.net.yzl.crm.customer.dao;


import cn.net.yzl.crm.customer.model.db.MemberProductEffectRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProductEffectRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberProductEffectRecord record);

    int insertSelective(MemberProductEffectRecord record);

    MemberProductEffectRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberProductEffectRecord record);

    int updateByPrimaryKey(MemberProductEffectRecord record);
}