package com.wimoor.amazon.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wimoor.api.admin.pojo.dto.SysUserRoleDTO;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;


 
@Component
@FeignClient(value = "wimoor-admin")
public interface AdminClientOneFeign {
    /**
     * eureka-client-one的helloworld访问mapping
     */
	@RequestMapping("/admin/api/v1/users/sysrole/userid/{userid}")
    public Result<UserInfo>  getUserByUserId(@PathVariable String userid);
	
    @GetMapping("/admin/api/v1/users/roles/{userid}")
    public Result<List<SysUserRoleDTO>> getUserRoleById(@PathVariable String userid) ;

    @RequestMapping("/admin/api/v1/users/sysrole/account/{account}")
    public Result<UserInfo> getUserByUsername(@PathVariable String account);

    @PostMapping("/admin/api/v1/sysTags/listname")
	public Result<List<Map<String,Object>>> findTagsNameByIds(@RequestBody Set<String> tagsIdsList);

    @PostMapping("/admin/api/v1/sysTags/mapname/{shopid}")
  	public Result<Map<String, Object>> findTagsName(@PathVariable String shopid);
    
}