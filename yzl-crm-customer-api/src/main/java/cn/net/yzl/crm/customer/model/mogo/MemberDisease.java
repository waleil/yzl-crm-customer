package cn.net.yzl.crm.customer.model.mogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberDisease
 * @description 会员病症信息
 * @date: 2021/1/25 11:10 上午
 */
@Data
public class MemberDisease {
    //病症ID
    private Integer diseaseId;
    //病症名称
    private String diseaseName;
    //会员卡号
    @JsonIgnore
    private String memberCard;
}
