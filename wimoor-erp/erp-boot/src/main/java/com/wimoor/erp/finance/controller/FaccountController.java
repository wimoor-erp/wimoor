package com.wimoor.erp.finance.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.finance.pojo.entity.FinAccount;
import com.wimoor.erp.finance.pojo.entity.FinanceProject;
import com.wimoor.erp.finance.service.IFaccountService;
import com.wimoor.erp.finance.service.IFinanceProjectService;
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
	final IFinanceProjectService financeProjectService;
	@GetMapping("/getProject")
	public Result<List<FinanceProject>> getProjectAction(){
		UserInfo userinfo = UserInfoContext.get();
		List<FinanceProject> list=financeProjectService.findProject(userinfo.getCompanyid());
		return Result.success(list);
	}
	
	@GetMapping("/getPaymentMethod")
	public Result<List<PurchaseFormPaymentMethod>> getPaymentMethodAction(){
		List<PurchaseFormPaymentMethod> list=faccountService.findPurchasePayMethod();
		return Result.success(list);
	}
	
	@GetMapping("/getPaymentAccount")
	public Result<List<FinAccount>> getPaymentAccountAction(String paymethod){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<FinAccount> list=faccountService.findPayAccountByMethod(paymethod,shopid);
		return Result.success(list);
	}
	@GetMapping("/getAccountAll")
	public Result<List<FinAccount>> getAccountAllAction(){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<FinAccount> list=faccountService.findAccountAll(shopid);
		return Result.success(list);
	}
	
	@GetMapping("/findAccountArchiveAll")
	public Result<List<FinAccount>> findAccountArchiveAllAction(){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		List<FinAccount> list=faccountService.findAccountArchiveAll(shopid);
		return Result.success(list);
	}
	
	@PostMapping("/saveAccount")
	public Result<FinAccount> saveAccountAction(@RequestBody FinAccount fin){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		fin.setShopid(shopid);
		fin.setCreatedate(new Date());
		fin.setBalance(new BigDecimal(0));
		fin.setIsdelete(false);
		faccountService.saveAccount(fin);
		return Result.success(fin);
	}
	
	@PostMapping("/updateAccountName")
	public Result<FinAccount> updateAccountNameAction(@RequestBody FinAccount fin){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		fin.setShopid(shopid);
		FinAccount oldone = faccountService.getById(fin.getId());
		QueryWrapper<FinAccount> queryWrapper=new QueryWrapper<FinAccount>();
		queryWrapper.eq("shopid", fin.getShopid());
		queryWrapper.eq("paymeth", fin.getPaymeth());
		queryWrapper.eq("name", fin.getName());
		FinAccount acc = faccountService.getOne(queryWrapper);
		if(acc!=null&&!acc.getId().equals(fin.getId())) {
			throw new BizException("修改失败，该账户名称已经存在");
		}else {
			 oldone.setName(fin.getName());
			 faccountService.updateById(oldone);
		}
		
		return Result.success(fin);
	}
	
	@PostMapping("/updateAccountDelete")
	public Result<FinAccount> updateAccountDisabledAction(@RequestBody FinAccount fin){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		fin.setShopid(shopid);
		FinAccount oldone = faccountService.getById(fin.getId());
	    if(oldone.getBalance().floatValue()>0.000001||oldone.getBalance().floatValue()<-0.000001) {
	    	throw new BizException("当前账户存在余额，无法删除");
	    }else {
	    	oldone.setIsdelete(true);
	    	faccountService.updateById(oldone);
	    }
		return Result.success(fin);
	}
	
	@PostMapping("/recoverAccountDelete")
	public Result<FinAccount> recoverAccountDisabledAction(@RequestBody FinAccount fin){
		FinAccount oldone = faccountService.getById(fin.getId());
	    oldone.setIsdelete(false);
	    faccountService.updateById(oldone);
		return Result.success(fin);
	}
	
	
	@PostMapping("/updateAccountDefault")
	@Transactional
	public Result<?> updateAccountDefaultAction(@RequestBody FinAccount fin){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
	    boolean result = faccountService.lambdaUpdate().set(FinAccount::getIsdefault, false)
		                              .eq(FinAccount::getShopid, shopid)
		                              .eq(FinAccount::getPaymeth, fin.getPaymeth()).update();
		 
		faccountService.lambdaUpdate().set(FinAccount::getIsdefault, true).eq(FinAccount::getId, fin.getId()).update();
		return Result.success(result);
	}
	
	@PostMapping("/cancelAccountDefault")
	@Transactional
	public Result<?> cancelAccountDefault(@RequestBody FinAccount fin){
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		Long count = faccountService.lambdaQuery().eq(FinAccount::getShopid, shopid).eq(FinAccount::getIsdefault, true).count();
		if(count==1) {
			throw new BizException("系统中必须有一个默认卡");
		}
	    boolean result = faccountService.lambdaUpdate().set(FinAccount::getIsdefault, false).eq(FinAccount::getId, fin.getId()).update();
		return Result.success(result);
	}
}
