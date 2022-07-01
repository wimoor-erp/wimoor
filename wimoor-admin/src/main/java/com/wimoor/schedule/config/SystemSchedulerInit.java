package com.wimoor.schedule.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.schedule.mapper.QuartzTaskMapper;
import com.wimoor.schedule.pojo.entity.QuartzTask;
import com.wimoor.schedule.pojo.vo.QuartzJobsVO;
import com.wimoor.schedule.service.SystemSchedulerService;

@Configuration
public class SystemSchedulerInit implements ApplicationRunner {
   @Autowired
   SystemSchedulerService systemSchedulerService;
   @Autowired
   QuartzTaskMapper quartzTaskMapper;
   @Override
   public void run(ApplicationArguments args) throws Exception {
	   List<QuartzJobsVO> slist = systemSchedulerService.listScheduler();
	   for(QuartzJobsVO item:slist) {
		   systemSchedulerService.deleteScheduler(item.getJobDetailName(), item.getGroupName());
	   }
	   QueryWrapper<QuartzTask> queryWrapper=new QueryWrapper<QuartzTask>();
	   queryWrapper.eq("isdelete", 0);
	   List<QuartzTask> list = quartzTaskMapper.selectList(queryWrapper);
	   list.forEach(item->{
		   if(systemSchedulerService.findScheduler(item.getName(), item.getFgroup())==false) {
			   systemSchedulerService.addScheduler(item);
		   }
	  });
	 

   }
}
 