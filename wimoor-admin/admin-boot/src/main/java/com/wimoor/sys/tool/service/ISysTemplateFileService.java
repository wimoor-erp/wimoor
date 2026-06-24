package com.wimoor.sys.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.pojo.entity.SysTemplateFile;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Service
* @createDate 2025-10-11 11:42:11
*/
public interface ISysTemplateFileService extends IService<SysTemplateFile> {

    void downloadFile(ServletOutputStream fOut, UserInfo userinfo,  String templateid,Map<String,Object> params);

    Map<String, Object> deleteFile(String uploadid);

    List<Map<String, Object>> getRecord(String companyid);

    Map<String, Object> uploadFile(UserInfo user,String ftype, InputStream inputStream, String originalFilename);
}
