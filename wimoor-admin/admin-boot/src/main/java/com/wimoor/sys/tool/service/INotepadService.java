package com.wimoor.sys.tool.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.sys.tool.pojo.dto.SysNotepadQueryDTO;
import com.wimoor.sys.tool.pojo.entity.Notepad;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liufei
* @description 针对表【t_sys_tool_notepad】的数据库操作Service
* @createDate 2025-10-09 13:43:07
*/
public interface INotepadService extends IService<Notepad> {
    IPage<Notepad> queryNotepad(SysNotepadQueryDTO queryDTO);

    Object saveNotepad(Notepad notepad);
}
