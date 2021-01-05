//package cn.net.yzl.crm.customer.controller;
//
//
//import cn.net.yzl.common.entity.GeneralResult;
//import cn.net.yzl.common.util.UUIDGenerator;
//import cn.net.yzl.crm.customer.dto.OrderCheckSubmitDTO;
//import cn.net.yzl.crm.customer.dto.OrderResDTO;
//import cn.net.yzl.crm.customer.dto.OrderSelectParamterDTO;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.constraints.NotBlank;
//import java.util.Collections;
//import java.util.List;
//
//@Api(tags = "订单管理")
//@RestController
//@RequestMapping(value = OrderController.PATH)
//public class OrderController {
//    public static final String PATH = "order/order";
//
//
//    @ApiOperation(value="查询订单列表")
//    @PostMapping("list")
//    public GeneralResult<List<OrderResDTO>> list(@RequestBody  OrderSelectParamterDTO dto) {
//        OrderResDTO d =  new OrderResDTO();
//        return GeneralResult.success(Collections.singletonList(d));
//    }
//
//    @ApiOperation(value="查询订单详情")
//    @PostMapping("getById")
//    public GeneralResult<OrderResDTO> getById(     @RequestParam("id")
//                                    @NotBlank(message="订单id不能为空")
//                                    @ApiParam(name="id",value="订单表id",required=true)  String id) {
//        OrderResDTO d =  new OrderResDTO();
//        return GeneralResult.success(d);
//    }
//
//    @ApiOperation(value="查询订单审核列表")
//    @PostMapping("checkList")
//    public GeneralResult<List<OrderResDTO>> checkList(@RequestBody  OrderSelectParamterDTO dto) {
//        OrderResDTO d =  new OrderResDTO();
//        return GeneralResult.success(Collections.singletonList(d));
//    }
//
//    @ApiOperation(value="订单审核-提交")
//    @PostMapping("checkSubmit")
//    public GeneralResult<Boolean> checkSubmit(@RequestBody OrderCheckSubmitDTO dto) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//
////新建订单
//
//    @GetMapping("downloadTemplate")
//    @ApiOperation(value = "导入模板下载", httpMethod = "GET", notes = "下载符合条件的Excel",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
//        String filename = UUIDGenerator.getUUID()+ ".xlsx";
//        response.setHeader("Content-disposition", "attachment; filename=" + filename);
//    }
//
//    @PostMapping(value="upload",consumes="multipart/*",headers="content-type=multipart/form-data")
//    @ApiOperation(value = "上传")
//    public GeneralResult<Boolean> downloadTemplate(@ApiParam(value="上传的文件",required = true) MultipartFile multipartFile, HttpServletRequest request) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//
//    @ApiOperation(value="新建订单-保存")
//    @PostMapping("addOrder")
//    public GeneralResult<Boolean> addOrder(@RequestBody OrderResDTO dto) {
//        return GeneralResult.success(Boolean.TRUE);
//    }
//
//
//
//}
