package com.wimoor.erp.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.wimoor.common.mvc.BizException;
import com.wimoor.util.SpringUtil;

public class LockCheckUtils {
	StringRedisTemplate stringRedisTemplate;
	String key;
	
public LockCheckUtils(StringRedisTemplate stringRedisTemplate,String key, int expier){
	 String times=stringRedisTemplate.opsForValue().get(key);
	 if(times!=null) {
		 throw new BizException("请稍等"+expier+"秒后重试");
	 }else {
		 stringRedisTemplate.opsForValue().set(key,"1",expier,java.util.concurrent.TimeUnit.SECONDS);
	 }
	 this.stringRedisTemplate=stringRedisTemplate;
	 this.key=key;
   }

public LockCheckUtils(StringRedisTemplate stringRedisTemplate,String key){
	 String times=stringRedisTemplate.opsForValue().get(key);
	 if(times!=null) {
		 throw new BizException("请稍等10秒后重试");
	 }else {
		 stringRedisTemplate.opsForValue().set(key,"1",60,java.util.concurrent.TimeUnit.SECONDS);
	 }
	 this.stringRedisTemplate=stringRedisTemplate;
	 this.key=key;
  }

public LockCheckUtils(String key){
	 StringRedisTemplate stringRedisTemplate =SpringUtil.getBean("stringRedisTemplate");
	 String times=stringRedisTemplate.opsForValue().get(key);
	 if(times!=null) {
		 throw new BizException("请稍等10秒后重试");
	 }else {
		 stringRedisTemplate.opsForValue().set(key,"1",60,java.util.concurrent.TimeUnit.SECONDS);
	 }
	 this.stringRedisTemplate=stringRedisTemplate;
	 this.key=key;
 }

public Boolean clear(){
		return stringRedisTemplate.delete(key);
  }
}
