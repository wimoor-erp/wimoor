package com.wimoor.erp.purchase.controller;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.purchase.pojo.entity.PurchasePlan;
import com.wimoor.erp.purchase.service.IPurchasePlanService;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "采购单接口")
@RestController
@SystemControllerLog( "补货规划接口")
@RequestMapping("/api/v1/purchase/plan")
@RequiredArgsConstructor
public class PlanController {
	final IPurchasePlanService  iPurchasePlanService;
	final protected ISerialNumService serialNumService;
	final IMaterialService iMaterialService;
	@GetMapping("/getPlan")
	public Result<List<PurchasePlan>> getPlanAction() throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		return Result.success(iPurchasePlanService.getPlanByShopid(shopid));
	}
	
	@PostMapping("/savePlan")
    @Transactional
	public Result<PurchasePlan> savePlanAction(@RequestBody PurchasePlan plan) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		plan.setShopid(shopid);
		plan.setCreatetime(new Date());
		plan.setOpttime(new Date());
		plan.setCreator(userinfo.getId());
		plan.setOperator(userinfo.getId());
		try {
			plan.setNumber(serialNumService.readSerialNumber(shopid, "PP"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				plan.setNumber(serialNumService.readSerialNumber(shopid, "PP"));
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new ERPBizException("编码获取失败,请联系管理员");
			}
		}
		return Result.success(iPurchasePlanService.savePlan(plan));
	}
	
	@PostMapping("/updatePlan")
    @Transactional
	public Result<PurchasePlan> updatePlanAction(@RequestBody PurchasePlan plan) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		plan.setShopid(shopid);
		plan.setOpttime(new Date());
		plan.setOperator(userinfo.getId());
		return Result.success(iPurchasePlanService.updatePlan(plan));
	}
	
	@GetMapping("/deletePlan")
    @Transactional
	public Result<?> deletePlanAction(String id) throws ERPBizException {
		UserInfo userinfo = UserInfoContext.get();
		PurchasePlan oldone = iPurchasePlanService.getById(id);
	    oldone.setDisable(true);
	    oldone.setOperator(userinfo.getId());
	    oldone.setOpttime(new Date());
	    iPurchasePlanService.updateById(oldone);
		return Result.success();
	}
	
	@GetMapping("/getLast")
	public Result<?> getLastRecordAction(String id) throws ERPBizException {
		Material material = iMaterialService.getById(id);
		return Result.success(iPurchasePlanService.getLastForm(BeanUtil.beanToMap(material)));
	}
	
	@PostMapping("/getLasts")
	public Result<?> getLastRecordsAction(@RequestBody List<String> ids) throws ERPBizException {
		return Result.success(iPurchasePlanService.getLastForms(ids));
	}
	
	
	@GetMapping("/getLast3")
	public Result<?> getLast3RecordAction(String id) throws ERPBizException {
		Material material = iMaterialService.getById(id);
		return Result.success(iPurchasePlanService.getLast3Form(BeanUtil.beanToMap(material)));
	}
}
