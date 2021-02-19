package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel("日期类型")
@Data
public class DateParam {

    @ApiModelProperty("符号 >= > = <= <")
    private String symbol;

    @ApiModelProperty("日期")
    private Date date;

}
