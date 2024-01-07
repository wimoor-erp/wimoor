package com.wimoor.amazon.profit.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.profit.pojo.entity.InplaceFee;
import com.wimoor.amazon.profit.pojo.entity.ManualProcessingFee;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfigCountry;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@Api(tags = "利润计算方案模块")
@SystemControllerLog( "利润计算方案模块")
@RestController
@RequestMapping("/api/v1/profit/profitCfg")
public class ProfitCfgController {
	@Resource
	IProfitCfgService profitCfgService;

	@ResponseBody
	@ApiOperation(value = "编辑")
	@SystemControllerLog( "编辑")
	@PostMapping(value = "/edit")
	public Result<ProfitConfig> setConfigAction(@RequestBody ProfitConfig config) throws BizException {
		UserInfo user= UserInfoContext.get();
		if (user == null) {
			   throw new BizException("未登录无法编辑");
			} 
		 ProfitConfig oldcfg = profitCfgService.getById(config.getId());
		 if(oldcfg!=null&&oldcfg.getIssystem()) {
			 throw new BizException("系统默认方案不能修改");
		 }
		config.setIsdelete(false);
		config.setShopId(user.getCompanyid());
		config.setOpttime(new Date());
		config.setIssystem(false);
		config.setOperator(new BigInteger(user.getId()));
		if(StrUtil.isEmpty(config.getName())) {
			throw new BizException("方案必须输入名称");
		}
			if (config.getSalesChannel().equals("amz_byself")) {
				ProfitConfigCountry us = config.getProfitConfigCountry("US");
				ProfitConfigCountry uk = config.getProfitConfigCountry("UK");
				ProfitConfigCountry de = config.getProfitConfigCountry("DE");
				ProfitConfigCountry jp = config.getProfitConfigCountry("JP");
				us.setLabelService(false);
				uk.setLabelService(false);
				de.setLabelService(false);
				jp.setLabelService(false);
			}
			if (oldcfg!=null) {
				  profitCfgService.update(config);
			} else {
				  LambdaQueryWrapper<ProfitConfig> query=new LambdaQueryWrapper<ProfitConfig>();
				  query.eq(ProfitConfig::getShopId, user.getCompanyid());
				  query.eq(ProfitConfig::getName, config.getName());
				  query.eq(ProfitConfig::getIssystem, false);
				  List<ProfitConfig> list = this.profitCfgService.list(query);
				  if(list!=null&&list.size()>0) {
					  config.setId(list.get(0).getId());
					  profitCfgService.update(config);
				  }else {
					  profitCfgService.insert(config);
				  }
				 
			}
 
		return Result.success(config);
	}

	@ResponseBody
	@ApiOperation(value = "查找方案配置")
	@GetMapping(value = "/findConfig")
	public  Result<ProfitConfig> findConfigAction(String id) {
		ProfitConfig profitCfg = profitCfgService.findConfigAction(id);
		return Result.success(profitCfg);
	}

	@ResponseBody
	@ApiOperation(value = "设置默认")
	@SystemControllerLog( "设置默认")
	@GetMapping(value = "/setDefaultPlan")
	public Result<String> setDefaultPlanAction(String id)   {
		UserInfo user= UserInfoContext.get();
		ProfitConfig cfg = profitCfgService.getById(id);
		if(cfg==null) {
			throw new BizException("无法找到对应方案");
		}
		if(cfg.getShopId()==null||cfg.getIssystem()) {
			throw new BizException("系统预置方案无法修改");
		}
		profitCfgService.setNotDefault(user.getCompanyid());
		String msg = profitCfgService.setDefaultPlan(id);
		return Result.success(msg);
	}

	@ApiOperation(value = "删除方案")
	@SystemControllerLog( "删除方案")
	@GetMapping(value = "/setDeletePlan")
	public Result<String> setDeletePlanAction(String id)   {
		UserInfo user= UserInfoContext.get();
		ProfitConfig cfg = profitCfgService.getById(id);
		if(cfg==null) {
			throw new BizException("无法找到对应方案");
		}
		if(cfg.getShopId()==null||cfg.getIssystem()) {
			throw new BizException("系统预置方案无法修改");
		}
		if(cfg.getIsDefault()) {
			throw new BizException("默认方案无法删除");
		}
 
		 if(cfg!=null&&cfg.getIssystem()) {
			 throw new BizException("系统默认方案不能修改");
		 }
		cfg.setIsdelete(true);
		cfg.setOpttime(new Date());
		cfg.setOperator(new BigInteger(user.getId()));
		boolean result = profitCfgService.updateById(cfg);
		return Result.judge(result);
	}
	
	
	@ResponseBody
	@ApiOperation(value = "查找默认方案")
	@GetMapping(value = "/findDefaultPlan")
	public Result<String>  findDefaultPlan()  {
		UserInfo user= UserInfoContext.get();
		String id = profitCfgService.findDefaultPlanId(user.getCompanyid());
		return Result.success(id);
	}
	
	@ResponseBody
	@ApiOperation(value = "查找系统方案")
	@GetMapping(value = "/findSysPlan")
	public Result<ProfitConfig> findSysPlan(HttpServletRequest request) throws BizException {
		ProfitConfig plan = profitCfgService.getSystemProfitCfg();
		return Result.success(plan);
	}
	
	 
	@ApiOperation(value = "获取利润计算方案")
	@GetMapping("/showProfitDetial")
	public Result<List<ProfitConfig>> showProfitDetial()  {
		UserInfo user= UserInfoContext.get();
		String shopid=user.getCompanyid();
		List<ProfitConfig> profitCfgList =  profitCfgService.findProfitCfgName(shopid);
		return Result.success(profitCfgList);
	}

	@ResponseBody
	@ApiOperation(value = "查找系统方案")
	@GetMapping(value = "/findInplacefee/{country}")
	public Result<List<InplaceFee>> findInplacefee(@PathVariable String country) {
		     List<InplaceFee> result = profitCfgService.findInplacefee(country);
		   return Result.success(result);
	}
 
	@ApiOperation(value = "获取利润计算方案")
	@GetMapping("/findManualProcessingFee")
	public Result<List<ManualProcessingFee>> findManualProcessingFee() {
		  List<ManualProcessingFee> result = profitCfgService.findManualProcessingFee();
		return Result.success(result);
	}

}
