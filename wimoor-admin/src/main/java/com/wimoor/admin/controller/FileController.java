package com.wimoor.admin.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.OSSApiService;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Api(tags = "文件接口")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final OSSApiService oSSApiService;
    @PostMapping
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", paramType = "form", dataType = "__file"),
            @ApiImplicitParam(name = "path", value = "路径", paramType = "query", dataType = "string")
    })
    public Result<String> upload(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "path") String path
    ) {
        try {
            int lastIndex = path.lastIndexOf("/");
        	String bucketName = path.substring(path.lastIndexOf("/", lastIndex - 1) + 1, lastIndex);
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String name = IdUtil.simpleUUID() + "." + suffix;
            String fullName = path.substring(lastIndex + 1)+"/"+name;
            oSSApiService.putObject(bucketName, fullName, file.getInputStream());
            String resultPath ="https://"+bucketName+".oss-cn-shenzhen.aliyuncs.com/"+fullName;
            return Result.success(resultPath);
        } catch (Exception e) {
            throw new BizException(e.getMessage());
        }
    }

    @DeleteMapping
    @ApiOperation(value = "文件删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", required = true, paramType = "query"),
    })
    @SneakyThrows
    public Result<?> removeFile(@RequestParam String path) {
        int lastIndex = path.lastIndexOf("/");
        String bucketName = path.substring(path.lastIndexOf("/", lastIndex - 1) + 1, lastIndex);
        String objectName = path.substring(lastIndex + 1);
        oSSApiService.removeObject(bucketName, objectName);
        return Result.success();
    }

}
