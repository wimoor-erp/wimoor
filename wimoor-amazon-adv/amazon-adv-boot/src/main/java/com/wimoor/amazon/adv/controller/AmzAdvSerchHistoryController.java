package com.wimoor.amazon.adv.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.adv.common.pojo.AmzAdvSerchHistory;
import com.wimoor.amazon.adv.common.service.IAmzAdvSerchHistoryService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
 

@Api(tags = "广告历史查询接口")
@RestController 
@RequestMapping("/api/v1/searchHistory")
public class AmzAdvSerchHistoryController {

 
	@Resource
	IAmzAdvSerchHistoryService amzAdvSerchHistoryService;
	
	@GetMapping("/getSerchHistory")
	public Result<List<AmzAdvSerchHistory>> getSerchHistoryAction(String ftype){
		UserInfo user = UserInfoContext.get();
		return Result.success(amzAdvSerchHistoryService.getSerchHistoryAction(user.getId(), ftype));
	}
	
	@GetMapping("/addSerchHistory")
	public Result<String> addSerchHistoryAction(String condition,String ftype){
		UserInfo user = UserInfoContext.get();
		int i = amzAdvSerchHistoryService.addSerchHistoryAction(user.getId(), condition, ftype);
		if(i == 1) {
			return Result.success("SUCCESS"); 
		}
		else {
			return Result.failed();
		}
	}
	
	@GetMapping("/deleteSerchHistory")
	public Result<String> deleteSerchHistoryAction(String id){
		int i = amzAdvSerchHistoryService.deleteSerchHistoryAction(id);
		if(i == 1) {
			return Result.success("SUCCESS"); 
		}
		else {
			return Result.failed();
		}
	}
	
}
