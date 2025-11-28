package com.wimoor.sys.tool.controller;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.sys.tool.pojo.dto.SysChartCompletionRequestDTO;
import com.wimoor.sys.tool.service.ISysDeepSeekService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "获取deepseek回答")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deepseek")
public class SysDeepSeekController {
    final ISysDeepSeekService iSysDeepSeekService;
    @PostMapping("/search")
    public Result<?> searchAction(@RequestBody SysChartCompletionRequestDTO dto)  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.completions(userInfo,dto));
    }
    @GetMapping("/getSession")
    public Result<?> getSessionAction()  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.getSession(userInfo));
    }

    @GetMapping("/getKey")
    public Result<?> getKeyAction()  {
        UserInfo userInfo = UserInfoContext.get();
        return Result.success(iSysDeepSeekService.getKey(userInfo));
    }
}
