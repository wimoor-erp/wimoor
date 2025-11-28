package com.wimoor.sys.tool.controller;

import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.dto.SysNotepadQueryDTO;
import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.wimoor.sys.tool.pojo.entity.Notepad;
import com.wimoor.sys.tool.service.ILargeFileService;
import com.wimoor.sys.tool.service.INotepadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Api(tags = "获取notepad")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class SysLargeFileController {
    final ILargeFileService iLargeFileService;
    final FileUpload fileUpload;
    @ApiOperation("查询记事本内容")
    @PostMapping(value ="/upload/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadFile(@PathVariable("type") String type,@RequestParam("file") MultipartFile file) {
        UserInfo userInfo = UserInfoContext.get();
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            fileName=fileName.split("\\.")[0]+new Date().getTime()+"."+fileName.split("\\.")[1];
            // 可以在这里添加权限验证逻辑，确保用户只能查询自己有权限的店铺数据
            LargeFile largefile = iLargeFileService.uploadLargeFile(inputStream, type, userInfo.getCompanyid() , fileName);
            return Result.success(fileUpload.getPictureImage(largefile.getLocation()));
        } catch (IOException e) {
            throw new BizException("读取异常");
        }


    }

    @ApiOperation("删除记事本内容")
    @GetMapping(value ="/delete/{type}")
    public Result<?> deleteFile(@PathVariable("type") String type,String path) {
        UserInfo userInfo = UserInfoContext.get();
        // 可以在这里添加权限验证逻辑，确保用户只能查询自己有权限的店铺数据
        path=fileUpload.pathToLocation(path);
        iLargeFileService.deleteLargeFile(type,path,userInfo.getCompanyid());
        return Result.success("删除成功");
    }



}
