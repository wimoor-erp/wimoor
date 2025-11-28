package com.wimoor.amazon.transparency.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.wimoor.amazon.transparency.service.ITransparencySkuinfoService;
import com.wimoor.amazon.transparency.service.ITransparencyTaskService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "transparencySkuinfo接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/transparency/task")
public class TransparencyTaskController {
    final ITransparencyTaskService transparencyTaskService;
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result<?> saveAction(@RequestBody TransparencyTask dto) {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(transparencyTaskService.saveTask(userinfo,dto));
    }

    @ApiOperation(value = "刷新")
    @GetMapping("/refresh")
    public Result<?> refreshAction(String  taskid) {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(transparencyTaskService.refreshTask(userinfo,transparencyTaskService.getById(taskid)));
    }


    @ApiOperation(value = "报表分类")
    @PostMapping("/list")
    public Result<?> listAction(@RequestBody TransparencyDTO dto) {
        UserInfo userinfo = UserInfoContext.get();
        dto.setAuthid(StrUtil.isBlankOrUndefined(dto.getAuthid())?null:dto.getAuthid());
        dto.setSku(StrUtil.isBlankOrUndefined(dto.getSku())?null:dto.getSku());
        dto.setGroupid(StrUtil.isBlankOrUndefined(dto.getGroupid())?null:dto.getGroupid());
        dto.setStatus(StrUtil.isBlankOrUndefined(dto.getStatus())?null:dto.getStatus());
        dto.setTcodelist(dto.getTcodelist()!=null&& !dto.getTcodelist().isEmpty() ?dto.getTcodelist():null);
        dto.setTaskid(StrUtil.isNullOrUndefined(dto.getTaskid())?null:dto.getTaskid());
        IPage<Map<String,Object>> result= transparencyTaskService.listTask(userinfo,dto);
        return Result.success(result);
    }
}

