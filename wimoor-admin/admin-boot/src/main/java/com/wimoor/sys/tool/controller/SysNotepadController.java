package com.wimoor.sys.tool.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;
import com.wimoor.sys.tool.pojo.dto.SysNotepadQueryDTO;
import com.wimoor.sys.tool.pojo.entity.Notepad;
import com.wimoor.sys.tool.service.INotepadService;
import com.wimoor.sys.tool.service.ISysDeepSeekService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "获取notepad")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notepad")
public class SysNotepadController {
    final INotepadService iNotepadService;

    @ApiOperation("查询记事本内容")
    @PostMapping("/list")
    public Result<?> queryNotepad(@RequestBody SysNotepadQueryDTO queryDTO) {
        UserInfo userInfo = UserInfoContext.get();
        queryDTO.setShopid(userInfo.getCompanyid());
        // 可以在这里添加权限验证逻辑，确保用户只能查询自己有权限的店铺数据
        return Result.success(iNotepadService.queryNotepad(queryDTO));
    }

    @ApiOperation("ID查询记事本内容")
    @GetMapping("/view")
    public Result<?> view(String id) {
        return Result.success(iNotepadService.getById(id));
    }

    @ApiOperation("查询记事本内容")
    @PostMapping("/save")
    public Result<?> saveNotepad(@RequestBody Notepad notepad) {
        UserInfo userInfo = UserInfoContext.get();
        // 可以在这里添加权限验证逻辑，确保用户只能查询自己有权限的店铺数据
        notepad.setShopid(userInfo.getCompanyid());
        notepad.setOperator(userInfo.getId());
        return Result.success(iNotepadService.saveNotepad(notepad));
    }

    @ApiOperation("删除记事本内容")
    @PostMapping("/removeNote")
    public Result<?> removeNote(@RequestBody Notepad notepad) {
        return Result.success(iNotepadService.removeById(notepad.getId()));
    }

}
