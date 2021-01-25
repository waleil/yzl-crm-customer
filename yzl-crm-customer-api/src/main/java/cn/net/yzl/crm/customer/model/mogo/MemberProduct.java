package cn.net.yzl.crm.customer.model.mogo;

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
    @ApiModelProperty("会员卡号")
    private String memberCard;

    @ApiModelProperty("商品编号")
    private String productCode;

    @ApiModelProperty("商品名称")
    private String productName;

}
