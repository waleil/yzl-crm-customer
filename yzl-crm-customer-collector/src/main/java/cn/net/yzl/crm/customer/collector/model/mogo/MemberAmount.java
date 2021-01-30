package cn.net.yzl.crm.customer.collector.model.mogo;

import cn.net.yzl.crm.customer.BaseObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberAmount
 * @description 会员账户信息
 * @date: 2021/1/25 11:42 下午
 */
@Data
public class MemberAmount extends BaseObject {
    @ApiModelProperty(value = "顾客卡号",name = "memberCard")
    private String memberCard;
    @ApiModelProperty(value = "总剩余金额",name = "totalMoney")
    private Integer totalMoney;
    @ApiModelProperty(value = "冻结预存款",name = "frozenAmount")
    private Integer frozenAmount;
}
