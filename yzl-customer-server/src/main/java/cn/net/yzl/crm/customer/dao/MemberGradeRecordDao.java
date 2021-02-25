package cn.net.yzl.crm.customer.dao;

import cn.net.yzl.crm.customer.dto.member.MemberGradeRecordDto;
import cn.net.yzl.crm.customer.model.db.MemberGradeRecordPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberGradeRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberGradeRecordPo record);

    int insertSelective(MemberGradeRecordPo record);

    MemberGradeRecordPo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberGradeRecordPo record);

    int updateByPrimaryKey(MemberGradeRecordPo record);

    List<MemberGradeRecordDto> getMemberGradeRecordList(@Param("memberCard") String memberCard);

    List<MemberGradeRecordDto> getMemberGradeRecordListByTimeRange(String memberCard, String startTime, String endTime);
}