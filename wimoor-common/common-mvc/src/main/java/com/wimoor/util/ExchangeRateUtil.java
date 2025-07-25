package com.wimoor.util;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;

public class ExchangeRateUtil {
	
	@SuppressWarnings("unchecked")
	public static String getRate(String from, String to)   {
		@SuppressWarnings("rawtypes")
		RedisTemplate  redisTemplate=SpringUtil.getBean("redisTemplate");
		if(redisTemplate==null) {
    		return null;
    	}
	     if (redisTemplate.hasKey("currency:"+from+":"+to)){
	    	 return (String) redisTemplate.opsForValue().get("currency:"+from+":"+to);
	        }
	        RestTemplate restTemplate=SpringUtil.getBean("restTemplateApi");
	     	if(restTemplate==null) {
	    		return null;
	    	}
	    	System.out.println("restTemplate---"+restTemplate);
	        Result<?> result =null;
	        try {
	        	result= restTemplate.getForObject("http://wimoor-amazon/amazon/api/v1/summary/getCurrency/"+to,Result.class);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        if(result!=null&&Result.isSuccess(result)&&result.getData()!=null) {
				Map<String,Double> rateChange = (Map<String,Double>) result.getData();
	            if(CollUtil.isNotEmpty(rateChange)) {
	            	 for (  Entry<String, Double> entry : rateChange.entrySet()) {
	            		 String value=entry.getValue().toString();
	                     redisTemplate.opsForValue().set("currency:"+entry.getKey()+":"+to,value);
	                 }
	            }
	            if (redisTemplate.hasKey("currency:"+from+":"+to)){
	                return   (String) redisTemplate.opsForValue().get("currency:"+from+":"+to);
	            }
	        }
	        return null;
	}
	public static BigDecimal  changeCurrencyByLocal(String from, String to, BigDecimal money) {
		if(money==null)return null;
		if(money.equals(new BigDecimal("0"))||(money.floatValue()<0.0000000001&&money.floatValue()>-0.0000000001)) {
			return new BigDecimal("0");
		}
		String rate = ExchangeRateUtil.getRate(from, to);
		if(rate==null) {
			throw new BizException("无法找到对应汇率");
		}else {
			BigDecimal tempmoney = money;
			BigDecimal result = tempmoney.multiply(new BigDecimal(rate));
			return result;
		}
		
	}
 
}
