package com.wimoor.common.service.impl;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import com.wimoor.common.result.Result;
import com.wimoor.util.SpringUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class QuartzTaskFactory extends QuartzJobBean{
 
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
				try {
					   RestTemplate restTemplate=SpringUtil.getBean("restTemplateApi");
					   JobDetail detail = context.getJobDetail();
					   Object jobPath = detail.getJobDataMap().get("jobPath");
					   Object parameter = detail.getJobDataMap().get("parameter");
					   Object description= detail.getJobDataMap().get("description");
					   SystemSchedulerInit init =SpringUtil.getBean(SystemSchedulerInit.class);
					   if(!"prod".equals(init.getProfile())) {return;}
					      log.info(description+":rundate=================>"+new Date()+"============>"+parameter);
						   try {
							       Result<?> result = null;
								   if(!StrUtil.isEmptyIfStr(parameter)) {
									   result = restTemplate.getForObject(jobPath.toString(),Result.class);
								   }else {
									   result = restTemplate.getForObject(jobPath.toString(),Result.class,parameter);
								   }
								   if(result!=null) {
									   log.info("result-code:"+result.getCode());
								   }
						     }catch (Exception e) {
							   log.info("出错了:"+e.getMessage());
					        }
						   log.info(description+":enddate================="+new Date());
					}catch(Exception e){
						e.printStackTrace();
						log.info("出错了:"+e.getMessage());
					} 
			}

}
