package com.wimoor.erp.finance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.finance.service.IFinanceProjectService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "财务接口")
@RestController
@SystemControllerLog( "财务记账")
@RequestMapping("/api/v1/fin/project")
@RequiredArgsConstructor
public class FinanceProjectController {

	final IFaccountService faccountService;
	final IFinanceProjectService financeProjectService;
	
	@GetMapping("/getProject")
	public Result<List<FinanceProject>> getProjectAction(){
		UserInfo userinfo = UserInfoContext.get();
		List<FinanceProject> list=financeProjectService.findProject(userinfo.getCompanyid());
		return Result.success(list);
	}
 
	@GetMapping("/saveProject")
	public Result<Map<String, Object>> saveProject(String name){
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> result = financeProjectService.saveProject(name,userinfo);
		return Result.success(result);
	}
	
	@GetMapping("/updateProject")
	public Result<Map<String, Object>> updateProject(String id,String name){
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> result = financeProjectService.updateProject(id, name,userinfo);
		return Result.success(result);
	}
	
	@GetMapping("/delProject")
	public Result<Map<String, Object>> delProject(String id){
		Map<String, Object> result = financeProjectService.delProject(id);
		return Result.success(result);
	}

}
