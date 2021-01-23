package cn.net.yzl.crm.customer.dto.member;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * member_character_relation
 * @author 
 */
@Data
public class MemberCharacterRelationDto implements Serializable {
    private Integer id;

    /**
     * 会员卡号
     */
    private String memberCard;

    /**
     * 性格字典id
     */
    private Integer did;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    private static final long serialVersionUID = 1L;


}