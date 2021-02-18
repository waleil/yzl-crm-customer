package cn.net.yzl.crm.customer.feign.client.Activity;

import cn.net.yzl.activity.model.responseModel.MemberLevelDetailResponse;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.MemberGrad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityHelper {


    @Autowired
    static ActivityFien activityFien;




    /**
     * 从DMC获取会员信息
     * @param id
     * @return
     */
    public static MemberGrad getMemberGrandFromDMC(Integer id) {

        MemberGrad memberGrad = null;
        try {
            ComResponse<MemberLevelDetailResponse> grandById = activityFien.getGrandById(id);
            if (grandById != null) {
                MemberLevelDetailResponse data = grandById.getData();
                if (data != null) {
                    memberGrad = new MemberGrad();
                    memberGrad.setId(data.getMemberLevelGrade());
                    memberGrad.setName(data.getMemberLevelName());
                }
            }
        } catch (Exception e) {
            log.error("获取会员信息错误{},会员级别为",id);
        }

        return memberGrad;


    }
}
