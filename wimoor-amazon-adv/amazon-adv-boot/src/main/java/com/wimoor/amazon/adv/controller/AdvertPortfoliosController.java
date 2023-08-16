package com.wimoor.amazon.adv.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.pojo.AmzAdvPortfolios;
import com.wimoor.amazon.adv.common.pojo.BaseException;
import com.wimoor.amazon.adv.common.service.IAmzAdvPortfoliosService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

@RestController 
@RequestMapping("/api/v1/AdvertPortfolios")
public class AdvertPortfoliosController {

 
	@Resource
	IAmzAdvPortfoliosService amzAdvPortfoliosService;
	
	@GetMapping("/findPortfoliosForProfileId")
	public Result<List<AmzAdvPortfolios>> findPortfoliosForProfileIdAction(String groupid,String profileid) {
		UserInfo user = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("profileid", profileid);
		param.put("marketplaceid", null);
		param.put("groupid", groupid);
		param.put("shopid", user.getCompanyid());
		return Result.success(amzAdvPortfoliosService.getPortfoliosForProfileId(param));
	}
	
	@ResponseBody
	@RequestMapping("/getPortfoliosById")
	public AmzAdvPortfolios getPortfoliosByIdAction(HttpServletRequest request, Model model) {
		String profileId = request.getParameter("profileid");
		String id = request.getParameter("id");
		Example example = new Example(AmzAdvPortfolios.class);
		Criteria cri = example.createCriteria();
		cri.andEqualTo("id", id);
		cri.andEqualTo("profileid", profileId);
		return amzAdvPortfoliosService.selectByExample(example).get(0);
	}
	
	@ResponseBody
	@RequestMapping("/createPortfolios")
	public List<AmzAdvPortfolios> createPortfoliosAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String jsonstr = request.getParameter("jsonstr");
		JSONObject portfolios = GeneralUtil.getJsonObject(jsonstr);
		BigInteger profileId = portfolios.getBigInteger("profileid");
		String name = portfolios.getString("name");
		String state = portfolios.getString("state");
		String policy = portfolios.getString("policy");
		String startDate = portfolios.getString("startDate");
		String endDate = portfolios.getString("endDate");
		BigDecimal amount = portfolios.getBigDecimal("amount");
		List<AmzAdvPortfolios> portfoliosList = new ArrayList<AmzAdvPortfolios>();
		AmzAdvPortfolios amzAdvPortfolios = new AmzAdvPortfolios();
		if(profileId == null) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(startDate != null || StringUtil.isNotEmpty(startDate)) {
			amzAdvPortfolios.setStartdate(GeneralUtil.getDatez(startDate));
		}
		if(endDate != null || StringUtil.isNotEmpty(endDate)) {
			amzAdvPortfolios.setEnddate(GeneralUtil.getDatez(endDate));
		}
		if(name != null || StringUtil.isNotEmpty(name)) {
			amzAdvPortfolios.setName(name);
		}
		if(state != null || StringUtil.isNotEmpty(state)) {
			amzAdvPortfolios.setState(state);
		}
		if(policy != null || StringUtil.isNotEmpty(policy)) {
			amzAdvPortfolios.setPolicy(policy);
		}
		if(amount != null) {
			amzAdvPortfolios.setAmount(amount);
		}
		portfoliosList.add(amzAdvPortfolios);
		return amzAdvPortfoliosService.amzCreatePortfolios(user, profileId, portfoliosList);
	}
	
	@ResponseBody
	@RequestMapping("/updatePortfolios")
	public List<AmzAdvPortfolios> updatePortfoliosAction(HttpServletRequest request, Model model) {
		UserInfo user = UserInfoContext.get();
		String portfolioid = request.getParameter("portfolioid");
		String profileId = request.getParameter("profileid");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String amount = request.getParameter("amount");
		List<AmzAdvPortfolios> portfoliosList = new ArrayList<AmzAdvPortfolios>();
		AmzAdvPortfolios amzAdvPortfolios = new AmzAdvPortfolios();
		if(profileId == null) {
			throw new BaseException("请选择正确的店铺站点！");
		}
		if(portfolioid == null) {
			throw new BaseException("请选择投资组合！");
		}
		if(startDate != null || StringUtil.isNotEmpty(startDate)) {
			amzAdvPortfolios.setStartdate(GeneralUtil.getDatez(startDate));
		}
		if(endDate != null || StringUtil.isNotEmpty(endDate)) {
			amzAdvPortfolios.setEnddate(GeneralUtil.getDatez(endDate));
		}
		if(name != null || StringUtil.isNotEmpty(name)) {
			amzAdvPortfolios.setName(name);
		}
		if(amount != null) {
			amzAdvPortfolios.setAmount(new BigDecimal(amount));
		}
		amzAdvPortfolios.setInbudget(true);
		amzAdvPortfolios.setId(new BigInteger(portfolioid));
		amzAdvPortfolios.setProfileid(new BigInteger(profileId));
		portfoliosList.add(amzAdvPortfolios);
		amzAdvPortfoliosService.amzUpdatePortfolios(user, new BigInteger(profileId), portfoliosList);
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/deletePortfolios")
	public List<AmzAdvPortfolios> deletePortfoliosAction(HttpServletRequest request, Model model) {
		//没有找到支持的亚马逊api
		return null;
	}
	
}
