package com.wimoor.amazon.inboundV2.controller;

import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentCustomsService;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentTemplateFileService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "发货海关信息")
@RestController
@RequestMapping("/api/v2/ship/customs")
@SystemControllerLog("发货报表")
@RequiredArgsConstructor
public class ShipInboundShipmentCustomsController {

        @Resource
        IShipInboundShipmentCustomsService shipInboundShipmentCustomsService;
        final IShipInboundShipmentTemplateFileService shipInboundShipmentTemplateFileService;
        @GetMapping(value = "/downloadShipTemplateFile")
        public void downloadShipTemplateFileAction(String shipmentid,String templateid, HttpServletResponse response) {
                UserInfo userinfo = UserInfoContext.get();
                try {
                        // 创建新的Excel工作薄
                        response.setContentType("application/force-download");// 设置强制下载不打开
                        response.addHeader("Content-Disposition", "attachment;fileName=" + shipmentid+ System.currentTimeMillis() + ".zip");// 设置文件名
                        ServletOutputStream fOut = response.getOutputStream();
                        // 将数据写入Excel
                        shipInboundShipmentCustomsService.downloadShipTemplateFile(fOut, userinfo,shipmentid,templateid);
                        fOut.flush();
                        fOut.close();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }



        @ApiOperation(value = "删除海关文件")
        @GetMapping("/deleteCustomsFile")
        public Result<Map<String, Object>> deleteCustomsFileAction(@ApiParam("上传海关记录ID")@RequestParam String uploadid){
                Map<String, Object> maps=shipInboundShipmentTemplateFileService.deleteCustomsFile(uploadid);
                return Result.success(maps);
        }

        @ApiOperation(value = "获取报关信息历史上传记录")
        @GetMapping("/getShipmentCustomsRecord")
        public Result<List<Map<String,Object>>> getShipmentCustomsRecordAction(){
                UserInfo user=UserInfoContext.get();
                List<Map<String,Object>> recoirdlist=shipInboundShipmentTemplateFileService.getShipmentCustomsRecord(user.getCompanyid());
                return Result.success(recoirdlist);
        }

        @ApiOperation(value = "上传海关文件")
        @PostMapping(value="/uploadCustomsFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public Result<Map<String, Object>> uploadCustomsFileAction(
                @RequestParam("file") MultipartFile file
        ){
                UserInfo user=UserInfoContext.get();
                Map<String, Object> maps=null;
                try {
                        maps = shipInboundShipmentTemplateFileService.uploadShipmentcustomsFile(user,file.getInputStream(),file.getOriginalFilename());
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return Result.success(maps);
        }




}
