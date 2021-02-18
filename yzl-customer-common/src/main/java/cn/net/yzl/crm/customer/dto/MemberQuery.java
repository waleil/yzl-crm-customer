package cn.net.yzl.crm.customer.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 会员查询条件
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午9:09:48
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "会员查询条件")
public class MemberQuery {
	@ApiModelProperty(value = "下单时间最小值(yyyy-MM-dd HH:mm:ss)", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderTimeFrom;
	@ApiModelProperty(value = "下单时间最大值(yyyy-MM-dd HH:mm:ss)", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderTimeTo;
}
