package com.wimoor.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.admin.pojo.entity.SystemSubscription;
@Mapper
public interface SystemSubscriptionMapper extends BaseMapper<SystemSubscription>{

   List<SystemSubscription> findByUser(String userid);
   
   public List<String> myLongRunningRrxMonitor();

   void executeLongRunningSQLKill(Map<String,Object> maps);
	
}