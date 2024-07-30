package com.wimoor.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.SystemSchedulerService;


@RestController
@RequestMapping("/task")
public class TaskController {
   @Autowired
   SystemSchedulerService systemSchedulerService;
    @GetMapping("/refresh")
    public Result<?> refresh() {
    	systemSchedulerService.refreshTask();
    	return Result.success();
    }
}
