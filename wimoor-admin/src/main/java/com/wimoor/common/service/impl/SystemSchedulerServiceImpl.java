package com.wimoor.common.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.mapper.QuartzTaskMapper;
import com.wimoor.common.pojo.entity.QuartzJobsVO;
import com.wimoor.common.pojo.entity.QuartzTask;
import com.wimoor.common.service.SystemSchedulerService;

@Service
public class SystemSchedulerServiceImpl implements SystemSchedulerService {
    @Autowired
    SchedulerFactoryBeanWithShutdownDelay schedulerFactory;
    @Autowired
    QuartzTaskMapper quartzTaskMapper;
    @Override
    public boolean addScheduler(QuartzTask quartzTask) {
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            JobDetail jobDetail = buildJod(quartzTask);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTask.getCron()).withMisfireHandlingInstructionFireAndProceed();
            // 构建触发器trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzTask.getName(), quartzTask.getFgroup())//设置一个触发器标识,这里设置了跟JobDetail使用一样的命名以及分组
                    .forJob(jobDetail)//绑定trigger到jobdetail
                    .withSchedule(scheduleBuilder)
                    .withPriority(quartzTask.getPriority())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
 
 
    private static JobDetail buildJod(QuartzTask quartzTask) {
    	JobKey jobKey = JobKey.jobKey(quartzTask.getName(), quartzTask.getFgroup());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("id", quartzTask.getId());
        jobDataMap.put("jobPath", quartzTask.getPath());
        jobDataMap.put("parameter", quartzTask.getParameter());
        jobDataMap.put("description", quartzTask.getDescription());
        jobDataMap.put("bean", quartzTask.getBean());
        jobDataMap.put("method", quartzTask.getMethod());
        jobDataMap.put("createTime", System.currentTimeMillis());
        return JobBuilder.newJob(QuartzTaskFactory.class)
                .withIdentity(jobKey)
                .withDescription(quartzTask.getDescription())
                .storeDurably()
                .requestRecovery(false)
                .setJobData(jobDataMap)
                .build();
    }
  
   public void refreshTask() {
	   deleteAllTask();
	   insertTask();
   }
   public void deleteAllTask() {
	   List<QuartzJobsVO> slist = this.listScheduler();
	   for(QuartzJobsVO item:slist) {
		   this.deleteScheduler(item.getJobDetailName(), item.getGroupName());
	   }
   }
   public void insertTask(){
		   QueryWrapper<QuartzTask> queryWrapper=new QueryWrapper<QuartzTask>();
		   queryWrapper.eq("isdelete", 0);
		   List<QuartzTask> list = quartzTaskMapper.selectList(queryWrapper);
		   list.forEach(item->{
			   if(this.findScheduler(item.getName(), item.getFgroup())==false) {
				   this.addScheduler(item);
			   }
		  });
	  

    }
	
    
    @Override
    public boolean updateScheduler(String jobDetailName, String jobDetailGroup, String cron) {
        try {
          	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobDetailName, jobDetailGroup);
            if (!CronExpression.isValidExpression(cron) || !scheduler.checkExists(jobKey)) {
                return false;
            }
            //triggerKey为添加定时任务时配置的name,group，这里是添加的时候设置的name跟group跟jobdetail是一样的
            TriggerKey triggerKey = TriggerKey.triggerKey(jobDetailName, jobDetailGroup);
            Trigger newTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
 
            scheduler.rescheduleJob(triggerKey, newTrigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean deleteScheduler(String jobDetailName, String jobDetailGroup) {
        try {
          	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobDetailName, jobDetailGroup);
            if (!scheduler.checkExists(jobKey)) {
                return false;
            }
            scheduler.deleteJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean puaseScheduler(String jobDetailName, String jobDetailGroup) {
        try {
          	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobDetailName, jobDetailGroup);
            if (!scheduler.checkExists(jobKey)) {
                return false;
            }
            scheduler.pauseJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    @Override
    public boolean resumeScheduler(String jobDetailName, String jobDetailGroup) {
        try {
          	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobDetailName, jobDetailGroup);
            if (!scheduler.checkExists(jobKey)) {
                return false;
            }
            scheduler.resumeJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean findScheduler(String jobDetailName, String jobDetailGroup) {
        try {
          	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobDetailName, jobDetailGroup);
            if (!scheduler.checkExists(jobKey)) {
                return false;
            }
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	 
	public List<QuartzJobsVO> listScheduler() {
		// TODO Auto-generated method stub
      	Scheduler scheduler = schedulerFactory.getScheduler();
		 GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
	        Set<JobKey> jobKeys = null;
	        List<QuartzJobsVO> jobList = new ArrayList<QuartzJobsVO>();
	        try {
	            jobKeys = scheduler.getJobKeys(matcher);
	            for (JobKey jobKey : jobKeys) {
	                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
	                for (Trigger trigger : triggers) {
	                    QuartzJobsVO job = new QuartzJobsVO();
	                    job.setJobDetailName(jobKey.getName());
	                    job.setGroupName(jobKey.getGroup());
	                    job.setJobCronExpression("触发器:" + trigger.getKey());
	                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
	                    job.setStatus(triggerState.name());
	                    if (trigger instanceof CronTrigger) {
	                        CronTrigger cronTrigger = (CronTrigger) trigger;
	                        String cronExpression = cronTrigger.getCronExpression();
	                        job.setJobCronExpression(cronExpression);
	                        job.setTimeZone(cronTrigger.getTimeZone().getID());
	                    }
	                    jobList.add(job);
	                }
	            }
	 
	        } catch (SchedulerException e) {
	            e.printStackTrace();
	        }
	        return jobList;

	}
  
}
