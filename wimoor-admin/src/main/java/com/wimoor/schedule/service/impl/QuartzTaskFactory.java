package com.wimoor.schedule.service.impl;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import com.wimoor.common.result.Result;

import cn.hutool.extra.spring.SpringUtil;

public class QuartzTaskFactory extends QuartzJobBean{
 
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		   JobDetail detail = context.getJobDetail();
		   Object path = detail.getJobDataMap().get("jobPath");
		   if(path!=null) {
			   RestTemplate restTemplayte=SpringUtil.getBean("restTemplateApi");
			   Result<?> result = restTemplayte.getForObject(path.toString(),Result.class);
			   System.out.println(detail.getKey()+"定时任务工作："+path+ result.getCode()+ " 当前时间：" + new Date());
		   }
 
	}

 

}
