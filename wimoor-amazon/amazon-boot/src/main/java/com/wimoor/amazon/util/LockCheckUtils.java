package com.wimoor.amazon.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.wimoor.common.mvc.BizException;

public class LockCheckUtils {
	StringRedisTemplate stringRedisTemplate;
	String key;
	
public LockCheckUtils(StringRedisTemplate stringRedisTemplate,String key, int expier){
	 String times=stringRedisTemplate.opsForValue().get(key);
	 if(times!=null) {
		 throw new BizException("历史操作进行中，请稍等"+expier+"秒后重试");
	 }else {
		 stringRedisTemplate.opsForValue().set(key,"1",expier,java.util.concurrent.TimeUnit.SECONDS);
	 }
	 this.stringRedisTemplate=stringRedisTemplate;
	 this.key=key;
   }

public LockCheckUtils(StringRedisTemplate stringRedisTemplate,String key){
	 String times=stringRedisTemplate.opsForValue().get(key);
	 if(times!=null) {
		 throw new BizException("历史操作进行中，请稍等10秒后重试");
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
