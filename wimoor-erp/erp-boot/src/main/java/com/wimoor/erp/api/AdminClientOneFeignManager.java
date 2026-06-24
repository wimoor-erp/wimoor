package com.wimoor.erp.api;

import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


 
@Component
public class AdminClientOneFeignManager {
    /**
     * eureka-client-one的helloworld访问mapping
     */
	@Autowired
	AdminClientOneFeign api;
	
    public UserInfo getUserByUserId(  String userid){
    	 Result<UserInfo> user = api.getUserByUserId(userid);
    	 if(user!=null&&user.getData()!=null) {
    		 return user.getData();
    	 }else {
    		return null;
    	 }
    }
    public Map<String,String> listType(String typeCode) {
    	Result<?> result = api.listType(typeCode);
    	Map<String,String> nameMap=new HashMap<String,String>();
    	if(Result.isSuccess(result)&&result.getData()!=null) {
    		@SuppressWarnings("unchecked")
			List<Map<String, Object>> list =  (List<Map<String, Object>>) result.getData();
    		for(Map<String, Object> item:list) {
    			nameMap.put(item.get("name").toString(), item.get("value").toString());
    		}
    		return nameMap;
    	}
    	return null;
    }
	
	public UserInfo getUserAllByUserId(  String userid){
		Result<UserInfo> result= api.getUserByUserId(userid);
		if(Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}
		return null;
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
		return new HashMap<String,String>();
    }

	public Map<String,String> getAllUserID(){
		Result<Map<String,String>> result= api.getIDMap();
		if(Result.isSuccess(result)&&result.getData()!=null) {
			return result.getData();
		}
		return new HashMap<String,String>();
	}
}