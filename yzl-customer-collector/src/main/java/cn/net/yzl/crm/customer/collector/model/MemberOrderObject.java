package cn.net.yzl.crm.customer.collector.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemberOrderObject {

    @ApiModelProperty("顾客卡号")
    private String memberCardNo;
    private List<MemberOrderDTO> orders;

}
