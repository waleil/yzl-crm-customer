package cn.net.yzl.crm.customer.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dto.MemberQuery;
import cn.net.yzl.crm.customer.service.MemberRestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 顾客信息
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午8:24:33
 */
@Api(tags = "顾客信息")
@RestController
@RequestMapping("/member")
public class MemberRestController {
	@Resource
	private MemberRestService memberRestService;

	@ApiOperation(value = "统计激活、正常、会员总数", notes = "统计激活、正常、会员总数")
	@PostMapping("/v1/querymembercount")
	public ComResponse<Integer> queryMemberCount(@RequestBody MemberQuery memberQuery) {
		if (memberQuery.getOrderTimeFrom() == null) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "[orderTimeFrom]不能为空。");
		}
		if (memberQuery.getOrderTimeTo() == null) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "[orderTimeTo]不能为空。");
		}
		return ComResponse.success(this.memberRestService.selectMemberCount(memberQuery));
	}
}
