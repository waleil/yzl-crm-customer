package cn.net.yzl.crm.customer.collector.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ApiModel("返回mongo的Query和update实体类")
@Data
public class QueryUpdate {

    @ApiModelProperty("获取query")
    private Query query;

    @ApiModelProperty("获取update")
    private Update update;
}
