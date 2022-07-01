package com.wimoor.schedule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.schedule.pojo.entity.QuartzTask;
import com.wimoor.schedule.pojo.vo.QuartzJobsVO;
import com.wimoor.schedule.service.SystemSchedulerService;


@RestController
public class SchedulerController {
    @Autowired
    SystemSchedulerService systemSchedulerService;
 
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody QuartzTask quartzTask) {
        return Result.judge(systemSchedulerService.addScheduler(quartzTask));
    }
 
    @PostMapping("/update")
    public  Result<Boolean> update(@RequestParam String name, @RequestParam String group, @RequestParam String cron) {
        return Result.judge(systemSchedulerService.updateScheduler(name, group, cron));
    }
 
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam String name, @RequestParam String group) {
        return Result.judge(systemSchedulerService.deleteScheduler(name, group));
    }
    
    @PostMapping("/list")
    public List<QuartzJobsVO> list(@RequestParam String name, @RequestParam String group) {
        return systemSchedulerService.listScheduler();
    }
}
