package com.wimoor.amazon.auth.service.impl;

import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.auth.mapper.AmzAuthApiTimelimitMapper;
import com.wimoor.amazon.auth.service.IAmzAuthApiTimelimitService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 新版本SPI-API使用 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-24
 */
@Service("amzAuthApiTimelimitService")
public class AmzAuthApiTimelimitServiceImpl extends ServiceImpl<AmzAuthApiTimelimitMapper, AmzAuthApiTimelimit> implements IAmzAuthApiTimelimitService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public AmzAuthApiTimelimit getApiLimit(String amazonauthid, String apiname) {
		// TODO Auto-generated method stub
		   String key=amazonauthid+"-"+apiname;
		   String limit =null;
		   try {
			   limit = stringRedisTemplate.opsForValue().get(key);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		   
	       if(limit!=null) {
	    	   JSONObject jsonObject=JSONObject.parseObject(limit);
	    	   AmzAuthApiTimelimit result=(AmzAuthApiTimelimit)JSONObject.toJavaObject(jsonObject, AmzAuthApiTimelimit.class);
	    	   return result;
	       }
		   QueryWrapper<AmzAuthApiTimelimit> queryWrapperlimit=new QueryWrapper<AmzAuthApiTimelimit>();
		   queryWrapperlimit.eq("amazonauthid",amazonauthid);
		   queryWrapperlimit.eq("apiname",apiname);
		   AmzAuthApiTimelimit entity = this.getOne(queryWrapperlimit);
		   try {
			   String json = JSONObject.toJSONString(entity);
			   stringRedisTemplate.opsForValue().set(key,json,1,java.util.concurrent.TimeUnit.DAYS);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		  
		  return entity;
	}

	@Override
	public boolean save(AmzAuthApiTimelimit entity) {
		// TODO Auto-generated method stub
	    boolean reult =true;
	    QueryWrapper<AmzAuthApiTimelimit> query=new QueryWrapper<AmzAuthApiTimelimit>();
		query.eq("amazonauthid", entity.getAmazonauthid());
		query.eq("apiname", entity.getApiname());
		AmzAuthApiTimelimit oldone=this.getOne(query);
		if(oldone==null) {
			reult = this.baseMapper.insert(entity)>0;
		    entity.setSavetime(new Date());
		    String key=entity.getAmazonauthid()+"-"+entity.getApiname();
		    String json = JSONObject.toJSONString(entity);
		    stringRedisTemplate.opsForValue().set(key,json,1,java.util.concurrent.TimeUnit.DAYS);
	   	   return reult;
		}else {
			return update(entity);
		}
	    
	}

 
	public boolean update(AmzAuthApiTimelimit entity) {
		// TODO Auto-generated method stub
		boolean reult =true;
		QueryWrapper<AmzAuthApiTimelimit> query=new QueryWrapper<AmzAuthApiTimelimit>();
		query.eq("amazonauthid", entity.getAmazonauthid());
		query.eq("apiname", entity.getApiname());
		AmzAuthApiTimelimit oldone=this.getOne(query);
		if(oldone==null) {
			return save(entity);
		}else {
			reult=this.baseMapper.update(entity, query)>0;
			entity.setSavetime(new Date());
			String key=entity.getAmazonauthid()+"-"+entity.getApiname();
			String json = JSONObject.toJSONString(entity);
			stringRedisTemplate.opsForValue().set(key,json,1,java.util.concurrent.TimeUnit.DAYS);
			return reult;
		}
	
	}

	
}
