package cn.net.yzl.crm.customer.feign.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MemberGradeValidDate{
    @ApiModelProperty("是否长期有效")
    private Boolean isAlways;//是否长期有效
    @ApiModelProperty("今年的会员有效期到期日期")
    private Date currentYearValidDate;
    @ApiModelProperty("有效期开始时间")
    private Date startDate;
    @ApiModelProperty("有效期结束时间")
    private Date endDate;
}