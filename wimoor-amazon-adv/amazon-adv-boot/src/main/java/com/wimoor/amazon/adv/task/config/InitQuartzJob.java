package com.wimoor.amazon.adv.task.config;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import com.wimoor.amazon.adv.task.pojo.ScheduleJob;
import com.wimoor.amazon.adv.task.pojo.SysTimeTask;
import com.wimoor.amazon.adv.task.service.ISysTimeTaskService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Slf4j
@Configuration
public class InitQuartzJob implements ApplicationRunner {
 
	static ApplicationContext appCt;
	static  SchedulerFactoryBean schedulerFactoryBean;
	@Resource
	ISysTimeTaskService sysTimeTaskService;
	@Autowired
	SchedulerFactoryBean startQuertz;
	
    @Value("${spring.profiles.active}")
    String profile;
    @Value("${spring.application.name}")
    String server;
	public   void init() {
		schedulerFactoryBean=startQuertz;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		try {
			log.info(scheduler.getSchedulerName());
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 这里从数据库中获取任务信息数据
		Example example = new Example(SysTimeTask.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("jobStatus","1"); // 已发布的定时任务
		List<SysTimeTask> list = sysTimeTaskService.selectByExample(example);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		 if(!"prod".equals(profile)) {return;}
			for (SysTimeTask sTimetask : list) {
				ScheduleJob job1 = new ScheduleJob();
				job1.setJobId(sTimetask.getId());
				job1.setJobGroup(sTimetask.getGroupName()); // 任务组
				job1.setJobName(sTimetask.getName());// 任务名称
				job1.setJobStatus(sTimetask.getJobStatus()); // 任务发布状态
				job1.setIsConcurrent(sTimetask.getIsConcurrent() == 1 ? "1" : "0"); // 运行状态
				job1.setSpringId(sTimetask.getBeanName());																// 0:stop
				job1.setCronExpression(sTimetask.getCron());
				job1.setBeanClass(sTimetask.getBeanName());// 一个以所给名字注册的bean的实例
				job1.setMethodName(sTimetask.getMenthodName());
				job1.setJobData(sTimetask.getJobData()); // 参数
				jobList.add(job1);
			}
 
		for (ScheduleJob job : jobList) {
			try {
				addJob(job);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public static void addJob(SysTimeTask sTimetask) throws SchedulerException {
		ScheduleJob job = new ScheduleJob();
		job.setJobId(sTimetask.getId());
		job.setJobGroup(sTimetask.getGroupName()); // 任务组
		job.setJobName(sTimetask.getName());// 任务名称
		job.setJobStatus(sTimetask.getJobStatus()); // 任务发布状态
		job.setIsConcurrent(sTimetask.getIsConcurrent() == 1 ? "1" : "0"); // 运行状态
		job.setSpringId(sTimetask.getBeanName());																// 0:stop
		job.setCronExpression(sTimetask.getCron());
		job.setBeanClass(sTimetask.getBeanName());// 一个以所给名字注册的bean的实例
		job.setMethodName(sTimetask.getMenthodName());
		job.setJobData(sTimetask.getJobData()); // 参数
		if (job == null || !ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		log.info(scheduler + "...........................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class
					: QuartzJobFactoryDisallowConcurrentExecution.class;
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup())
					.usingJobData("data", job.getJobData()).build();
			jobDetail.getJobDataMap().put("scheduleJob", job);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withDescription(job.getJobId().toString())
					.withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
 
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).usingJobData("data", job.getJobData())
					.withSchedule(scheduleBuilder).build();
 
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		if ("0".equals(job.getIsConcurrent())) {
			JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
			scheduler.pauseJob(jobKey);
		}
	}
	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public static void addJob(ScheduleJob job) throws SchedulerException {
		if (job == null || !ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
			return;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		log.info(scheduler + "...........................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class
					: QuartzJobFactoryDisallowConcurrentExecution.class;
			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup())
					.usingJobData("data", job.getJobData()).build();
			jobDetail.getJobDataMap().put("scheduleJob", job);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withDescription(job.getJobId().toString())
					.withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
 
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).usingJobData("data", job.getJobData())
					.withSchedule(scheduleBuilder).build();
 
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		if ("0".equals(job.getIsConcurrent())) {
			JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
			scheduler.pauseJob(jobKey);
		}
	}

	   @Override
	   public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
	     System.out.println("----------------------taskinit------------------important----------------------------------");
	     init();
	}

 
}
