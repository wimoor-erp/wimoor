package com.wimoor.sys.tool.service;

import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Service
* @createDate 2025-10-11 11:42:11
*/
public interface ILargeFileService extends IService<LargeFile> {
    public LargeFile uploadLargeFile(InputStream dataInputStream,String type,String shopid, String imageName) ;


    void deleteLargeFile(String ftype,String destinationPath,String companyid);
}
