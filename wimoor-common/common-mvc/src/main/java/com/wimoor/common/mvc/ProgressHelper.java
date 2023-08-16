package com.wimoor.common.mvc;


import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;

import com.wimoor.common.user.UserInfo;

import cn.hutool.extra.spring.SpringUtil;

public class ProgressHelper {
	String key;
	UserInfo user;
  public ProgressHelper(UserInfo user,String key) {
	  this.key=user.getId()+"#"+key;
	  this.user=user;
  }
  public void setProgress(Integer progress) {
	  ProgressHelper.setProgress(key, progress);
  }
  
  public Integer getProgress() {
	  return ProgressHelper.getProgress(key);
  }
  
  public static Integer getProgress(String key){
	  RedisTemplate<String, Object> redisTemplate=SpringUtil.getBean("redisTemplate");
	  if(redisTemplate==null) {
	  		return -1;
	  	}
      if (redisTemplate.hasKey(key)){
          return (Integer) redisTemplate.opsForValue().get(key);
      }else {
    	  return -1;
      }
  }
  
  public static  void  setProgress(String key,Integer progress){
	  RedisTemplate<String, Object> redisTemplate=SpringUtil.getBean("redisTemplate");
	  if(redisTemplate==null) {
	  		return ;
	  	}
	  redisTemplate.opsForValue().set(key,progress,Duration.ofHours(4));
    }
  
public static void remove(String key) {
	// TODO Auto-generated method stub
	  RedisTemplate<String, Object> redisTemplate=SpringUtil.getBean("redisTemplate");
	  if(redisTemplate==null) {
	  		return ;
	  	}
	  redisTemplate.opsForValue().set(key,100,Duration.ofSeconds(5));
}
  
  
}
