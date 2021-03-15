package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.service.MemberOrderStatService;
import cn.net.yzl.crm.customer.vo.member.MemberOrderStatUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "MemberOrderStatController", tags = {"顾客订单信息统计处理"})
@RestController
@RequestMapping("memberOrderStat")
@Slf4j
public class MemberOrderStatController {

    @Autowired
    MemberOrderStatService memberOrderStatService;

    @ApiOperation("同步顾客订单数据统计记录")
    @PostMapping("/v1/updateMemberOrderStatistics")
    public ComResponse<Boolean> updateMemberOrderStatistics(@RequestBody List<MemberOrderStatUpdateVo> orderStatUpdateVos) {
        boolean result = memberOrderStatService.updateMemberOrderStatistics(orderStatUpdateVos);
        return ComResponse.success(result);
    }


}
