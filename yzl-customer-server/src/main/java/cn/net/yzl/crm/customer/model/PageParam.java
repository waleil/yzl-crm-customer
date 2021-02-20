package cn.net.yzl.crm.customer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageParam {

    @ApiModelProperty(value = "当前页", name = "pageNo", example = "1")
    private Integer pageNo;

    @ApiModelProperty(value = "每页的数量", name = "pageSize", example = "10")
    private Integer pageSize;

}