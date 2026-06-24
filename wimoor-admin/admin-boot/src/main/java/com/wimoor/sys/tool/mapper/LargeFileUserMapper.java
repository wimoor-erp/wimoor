package com.wimoor.sys.tool.mapper;

import com.wimoor.sys.tool.pojo.entity.LargeFileUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file_user】的数据库操作Mapper
* @createDate 2026-06-23 10:00:00
* @Entity com.wimoor.sys.tool.pojo.entity.LargeFileUser
*/
@Mapper
public interface LargeFileUserMapper extends BaseMapper<LargeFileUser> {

}