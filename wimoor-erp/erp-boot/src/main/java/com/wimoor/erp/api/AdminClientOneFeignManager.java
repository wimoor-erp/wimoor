package com.wimoor.erp.api;

import java.util.HashMap;
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
 
	
	public Result<UserInfo> getUserAllByUserId(  String userid){
		return api.getUserAllByUserId(userid);
	}
	
	public Result<List<Map<String,Object>>> findTagsNameByIds(  Set<String> tagsIdsList){
		return api.findTagsNameByIds(tagsIdsList);
	}
  	public Result<Map<String, Object>> findTagsName(  String shopid){
  		return api.findTagsName(shopid);
  	}
     
    public String getUserName(String userid){
    	Result<Map<String,String>> result= api.getNameMap();
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData().get(userid);
    	}
		return null;
    }
    
    public Map<String,String> getAllUserName(){
    	Result<Map<String,String>> result= api.getNameMap();
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		return result.getData();
    	}
		return null;
    }
    
   public Map<String,String> findOwnerAllAction() {
	   Result<List<Map<String, Object>>> response= this.api.findOwnerAllAction();
	   Map<String,String> map=new HashMap<String,String>();
	   if(Result.isSuccess(response)&&response.getData()!=null) {
		   List<Map<String, Object>> list = response.getData();
		   for(Map<String, Object> item:list) { 
			  map.put(item.get("name").toString(), item.get("id").toString()); 
		   }
	   }
	   return map;
   }
}