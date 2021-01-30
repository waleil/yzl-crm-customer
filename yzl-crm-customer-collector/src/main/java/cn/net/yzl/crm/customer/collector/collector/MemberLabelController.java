package cn.net.yzl.crm.customer.collector.collector;

import cn.net.yzl.crm.customer.collector.service.MemberLabelService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelController
 * @description todo
 * @date: 2021/1/30 12:26 上午
 */
@RestController
public class MemberLabelController {
    @Autowired
    private MemberLabelService memberLabelService;
    @ApiOperation("根据顾客表的下单时间同步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timestamp", value = "时间戳，以毫秒为单位", required = true, dataType = "long", paramType = "query")
    })
    @GetMapping("/member/syncMember")
    public boolean syncMember(long timestamp){
    return memberLabelService.syncMember(timestamp);
    }


    @ApiOperation("根据进线时间同步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "通话记录表中的主键", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/member/syncById")
    public boolean syncById(@RequestParam(defaultValue = "0") int id){
        return memberLabelService.syncById(id);
    }
}
