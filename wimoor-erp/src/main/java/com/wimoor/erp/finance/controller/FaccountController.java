package com.wimoor.erp.finance.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPaymentMethod;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "财务接口")
@RestController
@SystemControllerLog( "财务记账")
@RequestMapping("/api/v1/faccount")
@RequiredArgsConstructor
public class FaccountController {

	final IFaccountService faccountService;
	
	@GetMapping("/getProject")
	public Result<List<FinanceProject>> getProjectAction(){
		UserInfo userinfo = UserInfoContext.get();
		List<FinanceProject> list=faccountService.findProject(userinfo.getCompanyid());
		return Result.success(list);
	}
	
	@GetMapping("/getPaymentMethod")
	public Result<List<PurchaseFormPaymentMethod>> getPaymentMethodAction(){
		List<PurchaseFormPaymentMethod> list=faccountService.findPurchasePayMethod();
		return Result.success(list);
	}
}
