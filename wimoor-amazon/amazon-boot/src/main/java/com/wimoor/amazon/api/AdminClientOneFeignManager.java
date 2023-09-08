package com.wimoor.amazon.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.admin.pojo.dto.SysEmailDTO;
import com.wimoor.admin.pojo.dto.SysUserRoleDTO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;


 
@Component
public class AdminClientOneFeignManager {
	@Autowired
	AdminClientOneFeign api;
    /**
     * eureka-client-one的helloworld访问mapping
     */
    public Result<UserInfo>  getUserByUserId( String userid){
    	return api.getUserByUserId(userid);
    }
	
    public Result<List<SysUserRoleDTO>> getUserRoleById( String userid){
    	return api.getUserRoleById(userid);
    }

	public Result<List<Map<String,Object>>> findTagsNameByIds(Set<String> tagsIdsList){
		return api.findTagsNameByIds(tagsIdsList);
	}

  	public Result<Map<String, Object>> findTagsName( String shopid){
  		return api.findTagsName(shopid);
  	}

    public void sendBoss(SysEmailDTO dto) {
		 api.sendBoss(dto); 
    }
    
}