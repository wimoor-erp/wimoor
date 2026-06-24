package com.wimoor.sys.tool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.sys.tool.pojo.entity.LargeFileUser;
import com.wimoor.sys.tool.service.ILargeFileUserService;
import com.wimoor.sys.tool.mapper.LargeFileUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file_user】的数据库操作Service实现
* @createDate 2026-06-23 10:00:00
*/
@Service
@RequiredArgsConstructor
public class LargeFileUserServiceImpl extends ServiceImpl<LargeFileUserMapper, LargeFileUser> implements ILargeFileUserService {

}