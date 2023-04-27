package com.wimoor.auth.client.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
 
@Component
@FeignClient(value = "wimoor-admin")
public interface AdminClientOneFeign {
    /**
     * eureka-client-one的helloworld访问mapping
     */
    @RequestMapping("admin/api/v1/users/sysrole/account/{account}")
    public Result<UserInfo> getUserByUsername(@PathVariable String account);
    
    @RequestMapping("admin/api/v1/users/sysrole/openidbind")
    public Result<UserInfo> bindUserByOpenid(@RequestParam("openid")String openid,@RequestParam("appType")String appType,@RequestParam("account")String account,@RequestParam("password")String password) ;
 
    @RequestMapping("admin/api/v1/users/sysrole/findbyopenid/{openid}/{appType}")
    public Result<List<UserInfo>> findUserByOpenid(@PathVariable String openid,@PathVariable String appType) ;
    
    @RequestMapping("admin/api/v1/users/sysrole/verifyAccount")
    public Result<UserInfo> verifyAccountAction(@RequestParam("account")String account,@RequestParam("password")String password);

}