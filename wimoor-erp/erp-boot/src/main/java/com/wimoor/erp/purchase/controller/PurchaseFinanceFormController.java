package com.wimoor.erp.purchase.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.purchase.pojo.dto.FinanceFormPayMethDTO;
import com.wimoor.erp.purchase.pojo.dto.PurchaseFinanceListDTO;
import com.wimoor.erp.purchase.service.IPurchaseFinanceFormService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购请款单接口")
@RestController
@SystemControllerLog( "采购请款单接口")
@RequestMapping("/api/v1/purchase_finance_form")
@RequiredArgsConstructor
public class PurchaseFinanceFormController {

	final IPurchaseFinanceFormService purchaseFinanceFormService;
	
	
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> listAction(@RequestBody PurchaseFinanceListDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("shopid", shopid); 
		
		if(StrUtil.isNotEmpty(dto.getSearch())) {
			param.put("search", "%"+dto.getSearch()+"%"); 
		}else {
			param.put("search",null); 
		}
		if(StrUtil.isNotEmpty(dto.getSupplierid())) {
			param.put("supplierid",  dto.getSupplierid() ); 
		}else {
			param.put("supplierid",null); 
		}
		if(StrUtil.isNotEmpty(dto.getSearchtype())) {
			param.put("searchtype",  dto.getSearchtype() ); 
		}else {
			param.put("searchtype",null); 
		}
		if(StrUtil.isNotEmpty(dto.getPaymethod())) {
			param.put("paymethod",  dto.getPaymethod() ); 
		}else {
			param.put("paymethod",null); 
		}
		if(StrUtil.isNotEmpty(dto.getStatus())) {
			param.put("status",dto.getStatus()); 
		}else {
			param.put("status",null); 
		}
		if(StrUtil.isNotEmpty(dto.getRemark())) {
			param.put("remark","%"+dto.getRemark()+"%"); 
		}else {
			param.put("remark",null); 
		}
		if(StrUtil.isNotEmpty(dto.getToDate())) {
			String fromDate = dto.getFromDate();
			if (GeneralUtil.isNotEmpty(fromDate)) {
				param.put("fromDate", fromDate.trim());
			}
			String toDate = dto.getToDate();
			if (GeneralUtil.isNotEmpty(toDate)) {
				param.put("toDate", toDate.trim() + " 23:59:59");
			}
		}else {
			param.put("fromDate", null);
			param.put("toDate",null); 
		}
		return Result.success(purchaseFinanceFormService.findByCondition(dto.getPage(), param));
	}
	
	@GetMapping("/getdetail")
	public Result<Map<String, Object>> getDetailAction(String id){
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFinanceFormService.getDetailMap(id, shopid));
	}
	
	@PostMapping("/approve")
	@Transactional
	public Result<Map<String, Object>> approveAction(@RequestBody List<String> ids){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFinanceFormService.approve(ids, userinfo));
	}
	
	@PostMapping("/approveReturn")
	@Transactional
	public Result<Map<String, Object>> approveReturnAction(@RequestBody List<String> ids){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFinanceFormService.approveReturn(ids, userinfo));
	}
	
	@PostMapping("/updatePay")
	@Transactional
	public Result<Map<String, Object>> updatePayAction(@RequestBody FinanceFormPayMethDTO dto){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFinanceFormService.updatePay(dto, userinfo));
	}
	
	@GetMapping("/updateRemark")
	@Transactional
	public Result<Map<String, Object>> updateRemarkAction(String id,String remark){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFinanceFormService.updateRemark(id,remark, userinfo));
	}
	
	//通过ids 去查询所有的payment项
	@PostMapping("/getData")
	public Result<List<Map<String, Object>>> getDataAction(@RequestBody List<String> ids){
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		return Result.success(purchaseFinanceFormService.getDetailData(ids,shopid));
	}
	
	
	//通过ids 去更新所有的payment 已付款。这里要关联采购单的entry
	@PostMapping("/paymentForm")
	public Result<Map<String, Object>> paymentFormAction(@RequestBody List<String> ids){
		UserInfo userinfo = UserInfoContext.get();
		return Result.success(purchaseFinanceFormService.paymentForm(ids,userinfo));
	}
	
	
	
	
	
	
}
