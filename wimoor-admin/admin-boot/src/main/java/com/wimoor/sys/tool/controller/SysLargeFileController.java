package com.wimoor.sys.tool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.admin.common.exception.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ObjectHandler;
import com.wimoor.common.service.impl.StorageLargeService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.wimoor.sys.tool.pojo.entity.LargeFileUser;
import com.wimoor.sys.tool.service.ILargeFileService;
import com.wimoor.sys.tool.service.ILargeFileUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "用户私人文件管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class SysLargeFileController {
    final ILargeFileService iLargeFileService;
    final ILargeFileUserService iLargeFileUserService;
    final FileUpload fileUpload;
    final StorageLargeService storageLargeService;

    @ApiOperation("上传文件(通用)")
    @PostMapping(value ="/upload/{type}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadFile(@PathVariable("type") String type,@RequestParam("file") MultipartFile file) {
        UserInfo userInfo = UserInfoContext.get();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileNameWithoutExtension =fileName;
            String fileExtension = "";
            if( fileName.contains(".")){
                String[] fileInfo=fileName.split("\\.");
                fileNameWithoutExtension = fileInfo[0];
                fileExtension = fileInfo[1];
            }
            String timekey=format.format(new Date());
            if(fileExtension.equals("xml")&&fileNameWithoutExtension.contains(timekey)){
                fileName=fileNameWithoutExtension+"."+fileExtension;
            }else{
                SimpleDateFormat TIMESTAMP_MS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                fileName=fileNameWithoutExtension+TIMESTAMP_MS_FORMAT.format(new Date())+"."+fileExtension;
            }
            LargeFile largefile = iLargeFileService.uploadLargeFile(inputStream, type, userInfo.getCompanyid() , fileName);
            return Result.success(fileUpload.getPictureImage(largefile.getLocation()));
        } catch (IOException e) {
            throw new BizException("读取异常");
        }
    }

    @ApiOperation("删除文件(通用)")
    @GetMapping(value ="/delete/{type}")
    public Result<?> deleteFile(@PathVariable("type") String type,String path) {
        UserInfo userInfo = UserInfoContext.get();
        path=fileUpload.pathToLocation(path);
        iLargeFileService.deleteLargeFile(type,path,userInfo.getCompanyid());
        return Result.success("删除成功");
    }

    @ApiOperation("上传用户私人文件")
    @PostMapping(value ="/userfile/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadUserFile(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "name", required = false) String name) {
        UserInfo userInfo = UserInfoContext.get();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            InputStream inputStream = file.getInputStream();
            String originalName = file.getOriginalFilename();
            String fileExtension = "";
            String fileNameWithoutExtension = originalName;
            if(originalName.contains(".")){
                String[] fileInfo = originalName.split("\\.");
                fileNameWithoutExtension = fileInfo[0];
                fileExtension = fileInfo[fileInfo.length - 1];
            }
            // 使用时间戳生成唯一文件名
            String storageName = fileNameWithoutExtension + "_" + format.format(new Date());
            if(!fileExtension.isEmpty()){
                storageName = storageName + "." + fileExtension;
            }
            // 上传到MinIO，类型为users
            LargeFile largefile = iLargeFileService.uploadLargeFile(inputStream, "users", userInfo.getCompanyid(), storageName);
            if(largefile == null){
                throw new BizException("文件上传失败");
            }
            // 保存用户文件关联记录
            LargeFileUser fileUser = new LargeFileUser();
            fileUser.setId(largefile.getId());
            fileUser.setUserid(userInfo.getId());
            fileUser.setShopid(userInfo.getCompanyid() != null ? userInfo.getCompanyid() : null);
            fileUser.setFileid(largefile.getId());
            // 如果用户没有指定名称，使用原始文件名
            fileUser.setName(name != null && !name.isEmpty() ? name : originalName);
            fileUser.setCreatetime(new Date());
            iLargeFileUserService.save(fileUser);
            return Result.success(fileUser);
        } catch (IOException e) {
            throw new BizException("读取异常");
        }
    }

    @ApiOperation("查询当前用户的文件列表")
    @GetMapping(value ="/userfile/list")
    public Result<?> getUserFileList() {
        UserInfo userInfo = UserInfoContext.get();
        List<LargeFileUser> fileList = iLargeFileUserService.list(
            new LambdaQueryWrapper<LargeFileUser>()
                .eq(LargeFileUser::getUserid, userInfo.getId())
                .orderByDesc(LargeFileUser::getCreatetime)
        );
        return Result.success(fileList);
    }

    @ApiOperation("重命名用户文件")
    @PostMapping(value ="/userfile/rename")
    public Result<?> renameUserFile(@RequestParam("id") String id, @RequestParam("name") String name) {
        UserInfo userInfo = UserInfoContext.get();
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        // 确保只能操作自己的文件
        if(!fileUser.getUserid().equals(userInfo.getId())){
            throw new BizException("无权操作此文件");
        }
        fileUser.setName(name);
        iLargeFileUserService.updateById(fileUser);
        return Result.success(fileUser);
    }

    @ApiOperation("删除用户私人文件")
    @PostMapping(value ="/userfile/delete")
    public Result<?> deleteUserFile(@RequestParam("id") String id) {
        UserInfo userInfo = UserInfoContext.get();
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        // 确保只能删除自己的文件
        if(!fileUser.getUserid().equals(userInfo.getId())){
            throw new BizException("无权操作此文件");
        }
        // 删除MinIO中的文件
        LargeFile largeFile = iLargeFileService.getById(fileUser.getFileid());
        if(largeFile != null){
            iLargeFileService.deleteLargeFile("users", largeFile.getLocation(), userInfo.getCompanyid());
        }
        // 删除用户文件关联记录
        iLargeFileUserService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation("下载用户私人文件")
    @GetMapping(value ="/userfile/download")
    public void downloadUserFile(@RequestParam("id") String id, HttpServletResponse response) {
        UserInfo userInfo = UserInfoContext.get();
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        // 确保只能下载自己的文件
        if(!fileUser.getUserid().equals(userInfo.getId())){
            throw new BizException("无权操作此文件");
        }
        LargeFile largeFile = iLargeFileService.getById(fileUser.getFileid());
        if(largeFile == null || largeFile.getLocation() == null){
            throw new BizException("文件不存在");
        }
        String location = largeFile.getLocation();
        // 将location转换为MinIO objectName：去掉bucketName前缀
        String objectName = location.replace(storageLargeService.getBucketName() + "/", "");
        try {
            String fileName = fileUser.getName() != null ? fileUser.getName() : objectName.substring(objectName.lastIndexOf("/") + 1);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            storageLargeService.getObject(storageLargeService.getBucketName(), objectName, new ObjectHandler() {
                @Override
                public void treatReader(InputStream is, Map<String, Object> param) {
                    try (OutputStream os = response.getOutputStream()) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                        }
                        os.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, null);
        } catch (Exception e) {
            throw new BizException("下载失败");
        }
    }

    @ApiOperation("获取用户文件下载链接(可分享)")
    @GetMapping(value ="/userfile/link")
    public Result<?> getUserFileLink(@RequestParam("id") String id) {
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        LargeFile largeFile = iLargeFileService.getById(fileUser.getFileid());
        if(largeFile == null || largeFile.getLocation() == null){
            throw new BizException("文件不存在");
        }
        // 使用getPictureImage将location转换为可访问的URL
        String url = fileUpload.getPictureImage(largeFile.getLocation());
        return Result.success(url);
    }

    @ApiOperation("上传公司文件")
    @PostMapping(value ="/userfile/uploadCompany",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadCompanyFile(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "name", required = false) String name) {
        UserInfo userInfo = UserInfoContext.get();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            InputStream inputStream = file.getInputStream();
            String originalName = file.getOriginalFilename();
            String fileExtension = "";
            String fileNameWithoutExtension = originalName;
            if(originalName.contains(".")){
                String[] fileInfo = originalName.split("\\.");
                fileNameWithoutExtension = fileInfo[0];
                fileExtension = fileInfo[fileInfo.length - 1];
            }
            // 使用时间戳生成唯一文件名
            String storageName = fileNameWithoutExtension + "_" + format.format(new Date());
            if(!fileExtension.isEmpty()){
                storageName = storageName + "." + fileExtension;
            }
            // 上传到MinIO，类型为company
            LargeFile largefile = iLargeFileService.uploadLargeFile(inputStream, "company", userInfo.getCompanyid(), storageName);
            if(largefile == null){
                throw new BizException("文件上传失败");
            }
            // 保存公司文件关联记录，userid为null表示公司文件
            LargeFileUser fileUser = new LargeFileUser();
            fileUser.setId(largefile.getId());
            fileUser.setUserid(null); // 公司文件userid为null
            fileUser.setShopid(userInfo.getCompanyid());
            fileUser.setFileid(largefile.getId());
            fileUser.setName(name != null && !name.isEmpty() ? name : originalName);
            fileUser.setCreatetime(new Date());
            iLargeFileUserService.save(fileUser);
            return Result.success(fileUser);
        } catch (IOException e) {
            throw new BizException("读取异常");
        }
    }

    @ApiOperation("查询公司文件列表")
    @GetMapping(value ="/userfile/listCompany")
    public Result<?> getCompanyFileList() {
        UserInfo userInfo = UserInfoContext.get();
        List<LargeFileUser> fileList = iLargeFileUserService.list(
            new LambdaQueryWrapper<LargeFileUser>()
                .eq(LargeFileUser::getShopid, userInfo.getCompanyid())
                .isNull(LargeFileUser::getUserid)
                .orderByDesc(LargeFileUser::getCreatetime)
        );
        return Result.success(fileList);
    }

    @ApiOperation("删除公司文件")
    @PostMapping(value ="/userfile/deleteCompany")
    public Result<?> deleteCompanyFile(@RequestParam("id") String id) {
        UserInfo userInfo = UserInfoContext.get();
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        // 公司文件只验证shopid
        if(!fileUser.getShopid().equals(userInfo.getCompanyid())){
            throw new BizException("无权操作此文件");
        }
        // 删除MinIO中的文件
        LargeFile largeFile = iLargeFileService.getById(fileUser.getFileid());
        if(largeFile != null){
            iLargeFileService.deleteLargeFile("company", largeFile.getLocation(), userInfo.getCompanyid());
        }
        // 删除文件关联记录
        iLargeFileUserService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation("重命名公司文件")
    @PostMapping(value ="/userfile/renameCompany")
    public Result<?> renameCompanyFile(@RequestParam("id") String id, @RequestParam("name") String name) {
        UserInfo userInfo = UserInfoContext.get();
        LargeFileUser fileUser = iLargeFileUserService.getById(id);
        if(fileUser == null){
            throw new BizException("文件不存在");
        }
        // 公司文件只验证shopid
        if(!fileUser.getShopid().equals(userInfo.getCompanyid())){
            throw new BizException("无权操作此文件");
        }
        fileUser.setName(name);
        iLargeFileUserService.updateById(fileUser);
        return Result.success(fileUser);
    }
}
