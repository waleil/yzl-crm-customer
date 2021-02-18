package cn.net.yzl.crm.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel(value = "ProductConsultationInsertVO",description = "会员咨询产品实体")
public class ProductConsultationInsertVO {


    @ApiModelProperty(value = "顾客卡号", name = "memberCard", required = true)
    @NotBlank
    private String memberCard;
    @ApiModelProperty(value = "产品code", name = "productCode", required = true)
    @NotBlank
    private String productCode;
    @ApiModelProperty(value = "产品名称", name = "productName", required = true)
    @NotBlank
    private String productName;
    @ApiModelProperty(value = "咨询时间(时间格式:yyyy-MM-dd HH:mm:ss)", name = "consultationTime", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date consultationTime;



}