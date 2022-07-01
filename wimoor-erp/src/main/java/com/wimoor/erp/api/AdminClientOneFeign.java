package com.wimoor.erp.api;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wimoor.common.result.Result;


 
@Component
@FeignClient(value = "wimoor-admin")
public interface AdminClientOneFeign {
    /**
     * eureka-client-one的helloworld访问mapping
     */
	@RequestMapping("/admin/api/v1/users/sysrole/info/{userid}")
    public Result<Map<String,Object>> getUserByUserId(@PathVariable String userid);
     

}