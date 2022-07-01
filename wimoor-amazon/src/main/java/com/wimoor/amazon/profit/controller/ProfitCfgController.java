package com.wimoor.amazon.profit.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
 

@SystemControllerLog( "利润计算方案模块")
@Controller
@RequestMapping("/api/v1/profit/profitCfg")
public class ProfitCfgController {
	@Resource
	IProfitCfgService profitCfgService;

	@ResponseBody
	@SystemControllerLog( "编辑")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public Map<String, String> setConfigAction(HttpServletRequest request, ProfitConfig config) throws UserException {
		Map<String, String> msgMap = new HashMap<String, String>();
		String msg = null;
		String operate = request.getParameter("operate");
		UserInfo user= UserInfoContext.get();
		if (user == null) {
			msg = "请先登录！";
		} else {
			if (config.getSalesChannel().equals("amz_byself")) {
				config.getUs().setLabelService(false);
				config.getUk().setLabelService(false);
				config.getDe().setLabelService(false);
				config.getJp().setLabelService(false);
			}
			config.setOpttime(new Date());
			config.setIssystem(false);
			config.setShopId(user.getCompanyid());
			if ("update".equals(operate)) {
				  profitCfgService.updateById(config);
			} else if ("addnew".equals(operate)) {
				  String shopId =user.getCompanyid();
				  this.profitCfgService.getProfitPlanCountByShopId(shopId);
				  profitCfgService.save(config);
			}
		}
		msgMap.put("id", config.getId());
		msgMap.put("message", msg);
		return msgMap;
	}

	@ResponseBody
	@RequestMapping(value = "/findConfig", method = RequestMethod.POST)
	public ProfitConfig findConfigAction(String id) {
		ProfitConfig profitCfg = profitCfgService.findConfigAction(id);
		return profitCfg;
	}

	@ResponseBody
	@RequestMapping(value = "/updateAuhtority", method = RequestMethod.POST)
	public boolean updateAuhtorityAction() {
		return true;
	}

	@ResponseBody
	@RequestMapping(value = "/insertAuhtority", method = RequestMethod.POST)
	public boolean insertAuhtorityAction() {
		return true;
	}

	@ResponseBody
	@SystemControllerLog( "设置默认")
	@RequestMapping(value = "/setDefaultPlan")
	public Map<String, String> setDefaultPlanAction(HttpServletRequest request, String id) throws UserException {
		Map<String, String> msgMap = new HashMap<String, String>();
		UserInfo user= UserInfoContext.get();
		profitCfgService.setNotDefault(user.getCompanyid());
		String msg = profitCfgService.setDefaultPlan(id);
		msgMap.put("msg", msg);
		return msgMap;
	}

	@ResponseBody
	@RequestMapping(value = "/findDefaultPlan")
	public Map<String, String> findDefaultPlan(HttpServletRequest request) throws UserException {
		Map<String, String> resultMap = new HashMap<String, String>();
		UserInfo user= UserInfoContext.get();
		String id = profitCfgService.findDefaultPlanId(user.getCompanyid());
		resultMap.put("id", id);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/findSysPlan")
	public ProfitConfig findSysPlan(HttpServletRequest request) throws UserException {
		ProfitConfig plan = profitCfgService.getSystemProfitCfg();
		return plan;
	}

}
