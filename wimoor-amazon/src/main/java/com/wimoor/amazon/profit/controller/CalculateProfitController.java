package com.wimoor.amazon.profit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.UserException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;


@Controller
@RequestMapping("/api/v1/profit/detail")
public class CalculateProfitController {
	@Resource
	IProfitService profitService;
	@Resource
	IExchangeRateService exchangeRateService;
	@Resource
	IProductInfoService productInfoService;
	@Resource
	IProfitCfgService profitCfgService;
	
	@ResponseBody
	@RequestMapping("/showProfitDetial")
	public Object showProfitDetial(HttpServletRequest request) throws UserException {
		String pid = request.getParameter("pid");
		if(StrUtil.isEmpty(request.getParameter("price")) || StrUtil.isEmpty(pid)) {
			return null;
		}
		UserInfo user= UserInfoContext.get();
		String shopid=user.getCompanyid();
		Map<String, Object> map = new HashMap<String, Object>();
		String price = request.getParameter("price").trim();
		price = price.substring(1, price.length()).trim();
		BigDecimal myprice = GeneralUtil.getBigDecimal(price);
		map.put("pid", pid);
		map.put("myprice", myprice);
		if(request.getParameter("productpricetype")!=null) {
			map.put("productpricetype", request.getParameter("productpricetype"));
		}
		List<ProfitConfig> profitCfgList =  profitCfgService.findProfitCfgName(shopid);
		//productInfoService.showProfitDetial(map);
		map.put("profitCfgList", profitCfgList);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/showDetialDailog")
	public String showDetialDailog(HttpServletRequest request) throws UserException {
		return "success";
	}

	@ResponseBody
	@RequestMapping("/showSLDetialDailog")
	public String showSLDetialDailog(HttpServletRequest request) throws UserException {
		return "success";
	}
	
	@ResponseBody
	@RequestMapping("/showProfit")
	public Map<String, Object> showProfit(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String typeId_ = request.getParameter("typeId");// 产品类型id
		int typeId = Integer.parseInt(typeId_);
		String referralrate_ = request.getParameter("referralrate");//印度佣金比率
		String cfgId=request.getParameter("cfgid");
		BigDecimal referralrate = null;
		if (referralrate_ != null && !"".equals(referralrate_.trim()) && !"undefined".equals(referralrate_)) {
			referralrate = new BigDecimal(referralrate_);
		}
		String isSmlAndLightStr = request.getParameter("isSmlAndLight");// 是否轻小
		boolean isSmlAndLight = false;
		if ("true".equals(isSmlAndLightStr)) {
			isSmlAndLight = true;
		}

		List<String> countryList = new ArrayList<String>();
		if (isSmlAndLight) {
			countryList = Arrays.asList(ProfitServiceImpl.smlAndLightCountry);
		} else {
			countryList = this.profitService.findCountryList();// 得到需要计算的国家列表
		}
		ProfitConfig profitcfg = profitCfgService.findConfigAction(cfgId);
		for (int i = 0; i < countryList.size(); i++) {
			String price_ = request.getParameter("price_" + countryList.get(i));// 从页面获取用户输入售价
			String costDetail_ = request.getParameter("costDetail_" + countryList.get(i));
			if (costDetail_ != null) {
				Map<String, String> costDetailMap = this.profitService.jsonToMap(costDetail_);
				CostDetail costDetail = new CostDetail(costDetailMap);
				if (StrUtil.isNotEmpty(price_)) {
					costDetail.setSellingPrice(new BigDecimal(price_));
				}
				costDetail = this.profitService.getCostDetail(costDetail, typeId, referralrate, isSmlAndLight,profitcfg);
				resultMap.put("costDetail_" + countryList.get(i), costDetail);
			} else {
				resultMap.put("costDetail_" + countryList.get(i), null);
			}
		}

		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping("/getCurrencyRate")
	public List<ExchangeRate> getCurrencyRateAction(HttpServletRequest request) throws UserException {
		return exchangeRateService.getExchangeRateLimit();
	}
	
	@ResponseBody
	@RequestMapping("/updateExchangeRate")
	public String updateExchangeRateAction(HttpServletRequest request) throws UserException {
		exchangeRateService.updateExchangeRate();
		return "success";
	}


}
