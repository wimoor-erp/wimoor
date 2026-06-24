package com.wimoor.sys.tool.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.service.ISysTemplateFileService;
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
@RequestMapping("/api/v2/template")
@SystemControllerLog("发货报表")
@RequiredArgsConstructor
public class SysTemplateFileController {

        @Resource
        ISysTemplateFileService iSysTemplateFileService;

        @PostMapping(value = "/downloadTemplateFile")
        public void downloadTemplateFileAction(@RequestBody Map<String,Object> params, HttpServletResponse response) {
                UserInfo userinfo = UserInfoContext.get();
                try {
                        // 创建新的Excel工作薄
                        String ftype=(String)params.get("ftype");
                        String templateid=(String)params.get("templateid");
                        response.setContentType("application/force-download");// 设置强制下载不打开
                        response.addHeader("Content-Disposition", "attachment;fileName=" + ftype+ System.currentTimeMillis() + ".zip");// 设置文件名
                        ServletOutputStream fOut = response.getOutputStream();
                        // 将数据写入Excel
                        iSysTemplateFileService.downloadFile(fOut, userinfo,templateid,params);
                        fOut.flush();
                        fOut.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }




        @ApiOperation(value = "删除文件")
        @GetMapping("/deleteFile")
        public Result<Map<String, Object>> deleteFileAction(@ApiParam("上传ID")@RequestParam String uploadid){
                Map<String, Object> maps=iSysTemplateFileService.deleteFile(uploadid);
                return Result.success(maps);
        }

        @ApiOperation(value = "获取模板历史上传记录")
        @GetMapping("/getRecord")
        public Result<List<Map<String,Object>>> getRecordAction(){
                UserInfo user=UserInfoContext.get();
                List<Map<String,Object>> recoirdlist=iSysTemplateFileService.getRecord(user.getCompanyid());
                return Result.success(recoirdlist);
        }

        @ApiOperation(value = "上传模板文件")
        @PostMapping(value="/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public Result<Map<String, Object>> uploadCustomsFileAction(@RequestParam("ftype") String ftype,
                @RequestParam("file") MultipartFile file
        ){
                UserInfo user=UserInfoContext.get();
                Map<String, Object> maps=null;
                try {
                        maps = iSysTemplateFileService.uploadFile(user,ftype,file.getInputStream(),file.getOriginalFilename());
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return Result.success(maps);
        }


}
