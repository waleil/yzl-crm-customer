package cn.net.yzl.crm.customer.model.mogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberProduct
 * @description 顾客购买商品信息
 * @date: 2021/1/25 11:19 上午
 */
@Data
public class MemberProduct {
    @JsonIgnore
    @ApiModelProperty("会员卡号")
    private String memberCard;

    @ApiModelProperty("商品编号")
    private String productCode;
    @JsonIgnore
    @ApiModelProperty("商品名称")
    private String productName;
    /**
     * 服用状态: 1 正常，2 非常好，3已停服
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @JsonIgnore
    private Integer takingState;

    /**
     * 服用效果 1好，2一般，3没有效果
     * @mbggenerated Mon Jan 25 21:24:38 CST 2021
     */
    @JsonIgnore
    private Integer takingEffect;

}
