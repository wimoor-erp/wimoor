package com.wimoor.sys.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.sys.tool.pojo.entity.SysTemplateFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Mapper
* @createDate 2025-10-11 11:42:11
* @Entity com.wimoor.sys.tool.pojo.entity.LargeFile
*/
@Mapper
public interface SysTemplateFileMapper extends BaseMapper<SysTemplateFile> {

    List<Map<String, Object>> selectRecordByShop(@Param("shopid")String shopid);

    SysTemplateFile findByNameAndShopid(@Param("shopid")String shopid, @Param("filename")String filename);
}




