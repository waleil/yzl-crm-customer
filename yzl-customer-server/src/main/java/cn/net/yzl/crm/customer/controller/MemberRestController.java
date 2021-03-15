package cn.net.yzl.crm.customer.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@ApiOperation(value = "统计激活、正常、会员总数：作者张维维", notes = "统计激活、正常、会员总数：作者张维维")
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

	@ApiOperation(value = "按顾客姓名查询顾客卡号列表：作者张维维", notes = "按顾客姓名查询顾客卡号列表：作者张维维")
	@PostMapping("/v1/querymembercards")
	public ComResponse<List<String>> queryMemberCards(@RequestParam String memberName) {
		if (!StringUtils.hasText(memberName)) {
			return ComResponse.fail(ResponseCodeEnums.ERROR, "[memberName]不能为空。");
		}
		return ComResponse.success(this.memberRestService.selectMemberCards(memberName));
	}
}
