package cn.net.yzl.crm.customer.controller;

import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value="CustomerController",tags = {"顾客服务"})
@RestController
@RequestMapping("member")
public class CustomerController {

    @Autowired
    MemberService memberService;

    @ApiOperation("获取顾客列表")
    @PostMapping("v1/getMemberListByPage")
    public GeneralResult<Page<Member>> getMemberListByPage(@RequestBody MemberSerchConditionDTO dto) {
        Page<Member> memberPage = memberService.findPageByCondition(dto);
        return GeneralResult.success(memberPage);

    }

    @ApiOperation(value="保存会员基本信息")
    @PostMapping("v1/save")
    public GeneralResult<Boolean> save(@RequestBody Member dto) {
        int result = memberService.insert(dto);
        if(result == 1){
            return GeneralResult.success(Boolean.TRUE);
        }else{
            return GeneralResult.success(Boolean.FALSE);
        }

    }
    @ApiOperation(value="更新会员基本信息")
    @PostMapping("v1/updateByMemberCart")
    public GeneralResult<Boolean> updateByMemberCart(@RequestBody Member dto) {

        int result = memberService.updateByMemberCardSelective(dto);
        if(result == 1){
            return GeneralResult.success(Boolean.TRUE);
        }else{
            return GeneralResult.success(Boolean.FALSE);
        }
    }

    @ApiOperation(value="根据顾客号查询顾客基本信息")
    @GetMapping("v1/getMember")
    public GeneralResult<Member> getMember(@RequestParam("memberCard") String  memberCard) {
        Member memberEntity = memberService.selectMemberByCard(memberCard);
        return GeneralResult.success(memberEntity);
    }

    @ApiOperation("获取顾客级别")
    @GetMapping("v1/getMemberGrad")
    public GeneralResult getMemberGrad() {
        return GeneralResult.success(memberService.getMemberGrad());
    }
}
