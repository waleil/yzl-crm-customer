//package cn.net.yzl.crm.customer.controller;
//
//
//import cn.net.yzl.common.entity.GeneralResult;
//import cn.net.yzl.common.util.UUIDGenerator;
//import cn.net.yzl.crm.customer.dto.AfterSaleOrderResDTO;
//import cn.net.yzl.crm.customer.dto.AfterSaleOrderSelectParamterDTO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.constraints.NotBlank;
//import java.util.Collections;
//import java.util.List;
//
//@Api(tags = "售后单管理")
//@RestController
//@RequestMapping(value = AfterSaleOrderController.PATH)
//public class AfterSaleOrderController {
//    public static final String PATH = "aftersale/order";
//
//    @ApiOperation(value="查询售后订单列表")
//    @PostMapping("list")
//    public GeneralResult<List<AfterSaleOrderResDTO>> list(@RequestBody AfterSaleOrderSelectParamterDTO dto) {
//        AfterSaleOrderResDTO d =  new AfterSaleOrderResDTO();
//        return GeneralResult.success(Collections.singletonList(d));
//    }
//
//    @ApiOperation(value="查询售后订单详情")
//    @PostMapping("getById")
//    public GeneralResult<AfterSaleOrderResDTO> getById(@RequestParam("id")
//                                    @NotBlank(message="售后订单id不能为空")
//                                    @ApiParam(name="id",value="拒售后订单id",required=true)  String id) {
//        AfterSaleOrderResDTO d =  new AfterSaleOrderResDTO();
//        return GeneralResult.success(d);
//    }
//
//    @ApiOperation(value="保存")
//    @PostMapping("save")
//    public GeneralResult<Boolean> save(@RequestBody AfterSaleOrderResDTO dto) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//    @ApiOperation(value="更新")
//    @PostMapping("update")
//    public GeneralResult<Boolean> update(@RequestBody AfterSaleOrderResDTO dto) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//    @ApiOperation(value="审核-保存")
//    @PostMapping("checkSubmit")
//    public GeneralResult<Boolean> checkSubmit(@RequestParam("result")
//                                             @NotBlank(message="审核结果不能为空")
//                                             @ApiParam(name="result",value="审核结果",required=true)  String result) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//
//
//    @GetMapping("download")
//    @ApiOperation(value = "导出列表", httpMethod = "GET", notes = "导出列表",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public void download( HttpServletRequest request, HttpServletResponse response) {
//       String filename = UUIDGenerator.getUUID()+ ".xlsx";
//       response.setHeader("Content-disposition", "attachment; filename=" + filename);
//    }
//
//
//
//
//
//}
