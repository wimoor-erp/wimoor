package com.wimoor.amazon.api;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wimoor.common.result.Result;

 
@Component
@FeignClient(value = "wimoor-erp")
public interface ErpClientOneFeign {
	
	
	@GetMapping("/erp/api/v1/material/getMaterialBySKU")
	public Result<Map<String,Object>> getMaterialBySKUAction(@RequestParam String sku,
			 @RequestParam String shopid);
     

}