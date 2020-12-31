package cn.net.yzl.crm.customer.controller;


import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.util.UUIDGenerator;
import cn.net.yzl.crm.customer.dto.CheckOrderResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

@Api(tags = "订单审核管理")
@RestController
@RequestMapping(value = CheckOrderController.PATH)
public class CheckOrderController {
    public static final String PATH = "checkorder/order";


    @ApiOperation(value="查询拒收订单列表")
    @PostMapping("list")
    public GeneralResult<List<CheckOrderResDTO>> list( @RequestParam("expressWaybillId")
                                                           @NotBlank(message="快递单号不能为空")
                                                           @ApiParam(name="expressWaybillId",value="快递单号",required=true)  String expressWaybillId) {
        CheckOrderResDTO d =  new CheckOrderResDTO();
        return GeneralResult.success(Collections.singletonList(d));
    }

    @ApiOperation(value="查询拒收订单详情")
    @PostMapping("getById")
    public GeneralResult<CheckOrderResDTO> getById(     @RequestParam("id")
                                    @NotBlank(message="拒收订单id不能为空")
                                    @ApiParam(name="id",value="拒收订单id",required=true)  String id) {
        CheckOrderResDTO d =  new CheckOrderResDTO();
        return GeneralResult.success(d);
    }

    @ApiOperation(value="预退")
    @PostMapping("preRetirement")
    public GeneralResult<Boolean> preRetirement(     @RequestParam("id")
                                                        @NotBlank(message="拒收订单id不能为空")
                                                        @ApiParam(name="id",value="拒收订单id",required=true)  String id) {
        return GeneralResult.success(Boolean.TRUE);
    }



    @GetMapping("downloadTemplate")
    @ApiOperation(value = "导入模板下载", httpMethod = "GET", notes = "下载符合条件的Excel",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadTemplate( HttpServletRequest request, HttpServletResponse response) {
        //String filename = StringUtil.encodeDownloadFileName("DownloadInfo" + DateUtil.yyyyMMdd.format(new Date()) + ".xlsx", userAgent);
       String filename = UUIDGenerator.getUUID()+ ".xlsx";
       response.setHeader("Content-disposition", "attachment; filename=" + filename);
    }

    @PostMapping(value="upload",consumes="multipart/*",headers="content-type=multipart/form-data")
    @ApiOperation(value = "上传")
    public GeneralResult<Boolean> downloadTemplate(@ApiParam(value="上传的文件",required = true) MultipartFile multipartFile, HttpServletRequest request) {

        return GeneralResult.success(Boolean.TRUE);
    }



}
