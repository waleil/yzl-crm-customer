package cn.net.yzl.crm.customer.model;

import cn.net.yzl.order.model.vo.member.MemberOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class memberOrderObject {

    @ApiModelProperty("顾客卡号")
    private String memberCardNo;
    private List<memberOrderDTO> orders;

}
