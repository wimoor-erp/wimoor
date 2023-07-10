package com.wimoor.erp.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;

import cn.hutool.core.bean.BeanUtil;


 
@Component
public class AdminClientOneFeignManager {
    /**
     * eureka-client-one的helloworld访问mapping
     */
	@Autowired
	AdminClientOneFeign api;
	
    public Result<Map<String,Object>> getUserByUserId(  String userid){
    	 Result<UserInfo> user = api.getUserAllByUserId(userid);
    	 if(user!=null&&user.getData()!=null) {
    		 return Result.success(BeanUtil.beanToMap(user.getData()));
    	 }else {
    		 return Result.failed();
    	 }
    }
    public String getUserName(String userid){
   	 Result<UserInfo> user = api.getUserAllByUserId(userid);
   	 if(user!=null&&user.getData()!=null) {
   		 if(user.getData().getUserinfo()!=null) {
   			 return  user.getData().getUserinfo().get("name")!=null?user.getData().getUserinfo().get("name").toString():null;
   		 }
   	 } 
   	return null;
   }
	
	public Result<UserInfo> getUserAllByUserId(  String userid){
		return api.getUserAllByUserId(userid);
	}
	
	public Result<List<Map<String,Object>>> findTagsNameByIds(  Set<String> tagsIdsList){
		return api.findTagsNameByIds(tagsIdsList);
	}
  	public Result<Map<String, Object>> findTagsName(  String shopid){
  		return api.findTagsName(shopid);
  	}
     

}