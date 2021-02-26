package cn.net.yzl.crm.customer.controller;


import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.member.MemberProductEffectDTO;
import cn.net.yzl.crm.customer.service.MemberProductEffectService;
import cn.net.yzl.crm.customer.vo.MemberProductEffectInsertVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectSelectVO;
import cn.net.yzl.crm.customer.vo.MemberProductEffectUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户服用效果处理类
 * wangzhe
 * 2021-01-27
 */
@RestController
@RequestMapping("memberProductEffect")
@Api(value = "顾客商品服用效果", tags = {"顾客商品服用效果"})
@Validated
public class MemberProductEffectController {


    @Autowired
    MemberProductEffectService memberProductEffectService;


    @ApiOperation(value = "批量修改商品服用效果", notes = "批量修改商品服用效果")
    @RequestMapping(value = "/v1/batchModifyProductEffect", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productEffects", value = "商品服用效果", required = true),
    })
    public ComResponse batchModifyProductEffect(@RequestBody List<MemberProductEffectUpdateVO> productEffects) {

        ComResponse result = memberProductEffectService.batchModifyProductEffect(null,productEffects);
        return result;
    }

    @ApiOperation(value = "批量保存商品服用效果", notes = "批量保存商品服用效果")
    @RequestMapping(value = "/v1/batchSaveProductEffect", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productEffects", value = "商品服用效果", required = true),
    })
    public ComResponse batchSaveProductEffect(@RequestBody List<MemberProductEffectInsertVO> productEffects) {

        ComResponse result = memberProductEffectService.batchSaveProductEffect(productEffects);
        return result;
    }

    @ApiOperation(value = "保存商品服用效果", notes = "保存商品服用效果")
    @RequestMapping(value = "/v1/saveProductEffect", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productEffect", value = "商品服用效果", required = true),
    })
    public ComResponse saveProductEffect(@RequestBody MemberProductEffectInsertVO productEffect) {

        ComResponse result = memberProductEffectService.save(productEffect);
        return result;
    }



    @ApiOperation(value = "获取商品服用效果信息", notes = "获取商品服用效果信息")
    @RequestMapping(value = "/v1/getProductEffects", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productEffect", value = "商品服用效果vo", required = true),
    })
    public ComResponse<List<MemberProductEffectDTO>> getProductEffects(@RequestBody MemberProductEffectSelectVO productEffect) {

        ComResponse result = memberProductEffectService.getProductEffects(productEffect);

        return result;

    }


    @ApiOperation(value = "商品服用效果，根据配置查询出距离商品服用完日期不足多少天的库存的会员号，进行回访", notes = "商品服用效果，根据配置查询出距离商品服用完日期不足多少天的库存的会员号，进行回访")
    @RequestMapping(value = "/v1/updateMemberProductLastNumAndCreateWorkOrder", method = RequestMethod.POST)
    public ComResponse<Boolean> updateMemberProductLastNumAndCreateWorkOrder() {
        ComResponse result = memberProductEffectService.updateMemberProductLastNumAndCreateWorkOrder();
        return result;
    }


}
