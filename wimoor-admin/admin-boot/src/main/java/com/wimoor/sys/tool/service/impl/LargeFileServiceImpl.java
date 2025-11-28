package com.wimoor.sys.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.service.impl.StorageLargeService;
import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.wimoor.sys.tool.service.ILargeFileService;
import com.wimoor.sys.tool.mapper.LargeFileMapper;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Service实现
* @createDate 2025-10-11 11:42:11
*/
@Service
@RequiredArgsConstructor
public class LargeFileServiceImpl extends ServiceImpl<LargeFileMapper, LargeFile> implements ILargeFileService {

    final StorageLargeService storageLargeService;

    String destinationPathBucketName(String destinationPath){
        if(!destinationPath.contains(storageLargeService.getBucketName())) {
            if(destinationPath.substring(0,1).equals("/")) {
                destinationPath= storageLargeService.getBucketName()+destinationPath;
            }else {
                destinationPath= storageLargeService.getBucketName()+"/"+destinationPath;
            }
        }
        return destinationPath;
    }
    public LargeFile uploadLargeFile(String imgsrc, String destinationPath ) throws IOException {
        String ftppath = "";
        LargeFile LargeFile = null;
        String oldimageName = null;
        String oldlocation=null;
        destinationPath=destinationPathBucketName(destinationPath);
        if (destinationPath.indexOf((storageLargeService.getBucketName()+"/")) == 0) {
            ftppath = destinationPath.substring((storageLargeService.getBucketName()+"/").length(), destinationPath.length());
            if (ftppath.charAt(ftppath.length() - 1) == '/') {
                ftppath = ftppath.substring(0, ftppath.length() - 1);
            }
        }
        if (destinationPath.charAt(destinationPath.length() - 1) != '/') {
            destinationPath = destinationPath + "/";
        }
        if (imgsrc.contains("data:image/") && imgsrc.contains("base64,")) {
            String ext = imgsrc.substring(imgsrc.indexOf("data:image/") + "data:image/".length(), imgsrc.indexOf(";"));// 图片后缀
            String base64ImgData = imgsrc.substring(imgsrc.indexOf("base64,") + "base64,".length());// 图片数据
            byte[] bs = GeneralUtil.decryptBASE64(base64ImgData);
            long date = new Date().getTime();
            String imageName = date + "_after." + ext;
            String filePath2 = System.getProperty("java.io.tmpdir") + "/" + imageName;
            try {
                Thumbnails.of(new ByteArrayInputStream(bs)).scale(1f).outputQuality(0.3f).toFile(filePath2);
            } catch (Exception e) {
                storageLargeService.putObject(storageLargeService.getBucketName(), ftppath+"/"+imageName, new ByteArrayInputStream(bs));
            }
            try {
                storageLargeService.putObject(storageLargeService.getBucketName(), ftppath+"/"+imageName, new FileInputStream(filePath2));
                if (LargeFile != null) {
                    LargeFile.setLocation(destinationPath + imageName);
                    LargeFile.setOpttime(new Date());
                    this.baseMapper.updateById(LargeFile);
                } else {
                    LargeFile = new LargeFile();
                    LargeFile.setLocation(destinationPath + imageName);
                    LargeFile.setOpttime(new Date());
                    this.baseMapper.insert(LargeFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return LargeFile;
    }

    public LargeFile uploadLargeFile(InputStream dataInputStream,  String type,String shopid, String imageName)   {
        String path=shopid+"/"+type;
        String destinationPath=storageLargeService.getBucketName();
        if (destinationPath.charAt(destinationPath.length() - 1) != '/') {
            destinationPath = destinationPath + "/";
        }
        String objectName=path+ "/"+imageName;
        String location=destinationPath + path+ "/"+imageName;
        LambdaQueryWrapper<LargeFile> query = new LambdaQueryWrapper<>();
        query.eq(LargeFile::getLocation, location);
        LargeFile largeFile = this.baseMapper.selectOne(query);
        Boolean flag = storageLargeService.putObject(storageLargeService.getBucketName(), objectName, dataInputStream);
        if (flag) {
            if (largeFile != null) {
                largeFile.setFtype(type);
                largeFile.setLocation(location);
                largeFile.setOpttime(new Date());
                largeFile.setShopid(shopid);
                this.baseMapper.updateById(largeFile);
            } else {
                largeFile = new LargeFile();
                largeFile.setFtype(type);
                largeFile.setShopid(shopid);
                largeFile.setLocation(location);
                largeFile.setOpttime(new Date());
                this.baseMapper.insert(largeFile);
            }
        }
        return largeFile;
    }

     @Override
    public void deleteLargeFile(String ftype,String destinationPath,String shopid) {
        LambdaQueryWrapper<LargeFile> query = new LambdaQueryWrapper<>();
        query.eq(LargeFile::getFtype, ftype);
        query.eq(LargeFile::getShopid, shopid);
        query.eq(LargeFile::getLocation, destinationPath);
        LargeFile file = this.baseMapper.selectOne(query);
        if(file!=null){
            destinationPath=destinationPath.replace(storageLargeService.getBucketName()+"/", "");
            storageLargeService.removeObject(storageLargeService.getBucketName(), destinationPath);
            this.baseMapper.deleteById(file.getId());
        }

    }

    public void removeFiles(List<LargeFile> list){
        if (list == null || list.size() == 0) {
            try {
                for(LargeFile largeFile : list){
                    storageLargeService.removeObject(storageLargeService.getBucketName(), largeFile.getLocation());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getSourceFileName(String sourcePath) {
        String[] imageNames = sourcePath.split("\\\\");
        return  imageNames[imageNames.length - 1];
    }
    public String createRandFileName(String imageName) {
        imageName= imageName.split("\\.")[1];
        Double rand =   (Math.random()*1000);
        imageName=(new Date().getTime()+""+rand.intValue())+"."+imageName;
        return imageName;
    }
    public String buildDestinationPath(String destinationPath) {
        if (destinationPath.indexOf((storageLargeService.getBucketName()+"/")) == 0) {
            destinationPath = destinationPath.substring((storageLargeService.getBucketName()+"/").length(), destinationPath.length());
            if (destinationPath.charAt(destinationPath.length() - 1) == '/') {
                destinationPath = destinationPath.substring(0, destinationPath.length() - 1);
            }
        }
        return destinationPath;
    }
    public LargeFile downloadLargeFileMaterial(String LargeFileid, String sourcePath, String destinationPath) throws Exception {
        LargeFile LargeFile = null;
        String uri = null;
        String imageName = null;
        destinationPath=destinationPathBucketName(destinationPath);
        String oldname=null;
        if (GeneralUtil.isNotEmpty(LargeFileid)) {
            LargeFile = this.getById(LargeFileid);
            if (LargeFile != null) {
                if(GeneralUtil.isNotEmpty(LargeFile.getLocation())) {
                    uri = LargeFile.getLocation();
                }
                String[] imageNames = uri.split("/");
                imageName = imageNames[imageNames.length - 1];
                oldname=imageName;
            } else {
                LargeFile = new LargeFile();
                imageName =getSourceFileName(sourcePath);
            }
        }else {
            LargeFile = new LargeFile();
            imageName =getSourceFileName(sourcePath);
        }
        imageName=createRandFileName(imageName);
        LargeFile.setLocation(destinationPath + imageName);
        destinationPath=buildDestinationPath(destinationPath);
        try {
            if(StrUtil.isNotEmpty(oldname)) {
                storageLargeService.removeObject(storageLargeService.getBucketName(), destinationPath+"/"+oldname);
            }
            storageLargeService.putObject(storageLargeService.getBucketName(),destinationPath+"/"+imageName, new URL("file:\\"+sourcePath).openStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (GeneralUtil.isEmpty(LargeFileid)) {
            LargeFile.setOpttime(new Date());
            this.baseMapper.insert(LargeFile);
        } else {
            LargeFile.setOpttime(new Date());
            this.baseMapper.updateById(LargeFile);
        }
        return LargeFile;
	}
}




