package com.wimoor.sys.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.sys.tool.pojo.dto.SysNotepadQueryDTO;
import com.wimoor.sys.tool.pojo.entity.Notepad;
import com.wimoor.sys.tool.service.INotepadService;
import com.wimoor.sys.tool.mapper.NotepadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
* @author liufei
* @description 针对表【t_sys_tool_notepad】的数据库操作Service实现
* @createDate 2025-10-09 13:43:07
*/
@Service
@RequiredArgsConstructor
public class NotepadServiceImpl extends ServiceImpl<NotepadMapper, Notepad>
    implements INotepadService {
    @Override
    public IPage<Notepad> queryNotepad(SysNotepadQueryDTO queryDTO) {
        // 1. 创建分页对象 (页码从1开始，默认每页10条)
        IPage<Notepad> page = new Page<>(queryDTO.getCurrentpage(), queryDTO.getPagesize());

        // 2. 构建查询条件
        QueryWrapper<Notepad> queryWrapper = new QueryWrapper<>();

        // 必传条件：shopid
        queryWrapper.eq("shopid", queryDTO.getShopid());
         if(StrUtil.isNotBlank(queryDTO.getSearch())) {
            if("title".equals(queryDTO.getSearchtype())) {
                queryWrapper.like("title", "%"+queryDTO.getSearch().trim()+"%");
            }else if("content".equals(queryDTO.getSearchtype())) {
                queryWrapper.like("content", "%"+queryDTO.getSearch().trim()+"%");
            }else if("sku".equals(queryDTO.getSearchtype())) {
                queryWrapper.like("sku", "%"+queryDTO.getSearch().trim()+"%");
            }
        }

        // 按创建时间倒序 (最新的在前)
        queryWrapper.orderByDesc("opttime");

        // 3. 执行分页查询
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Object saveNotepad(Notepad notepad) {
        if (notepad.getId() == null) {
            notepad.setOpttime(new Date());
            Notepad old = this.lambdaQuery().eq(Notepad::getShopid, notepad.getShopid()).eq(Notepad::getTitle, notepad.getTitle()).one();
            if (old != null) {
                throw new BizException("标题已存在");
            }
            return baseMapper.insert(notepad);
        }else {
            notepad.setOpttime(new Date());
            return baseMapper.updateById(notepad);
        }

    }
}




