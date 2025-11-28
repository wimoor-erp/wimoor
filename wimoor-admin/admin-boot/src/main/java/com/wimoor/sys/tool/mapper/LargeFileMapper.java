package com.wimoor.sys.tool.mapper;

import com.wimoor.sys.tool.pojo.entity.LargeFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Mapper
* @createDate 2025-10-11 11:42:11
* @Entity com.wimoor.sys.tool.pojo.entity.LargeFile
*/
@Mapper
public interface LargeFileMapper extends BaseMapper<LargeFile> {

}




