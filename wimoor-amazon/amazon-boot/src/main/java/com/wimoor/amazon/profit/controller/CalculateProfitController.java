package com.wimoor.amazon.profit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.dto.ProfitQuery;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "利润计算")
@RestController
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
	
	
 
	@GetMapping("/showDetialDailog")
	public String showDetialDailog() {
		return "success";
	}

	@GetMapping("/showSLDetialDailog")
	public Result<?> showSLDetialDailog(){
		return Result.success();
	}
	
	 
	@ApiOperation(value = "获取利润计算详情")
	@PostMapping("/showProfit")
	public Result<Map<String, CostDetail>> showProfit(@RequestBody ProfitQuery query) {
 
		int typeId = query.getTypeId();
		String cfgId=query.getProfitCfgId();
		String referralrate_ = query.getReferralrate();//印度佣金比率
		BigDecimal referralrate = null;
		if (referralrate_ != null && !"".equals(referralrate_.trim()) && !"undefined".equals(referralrate_)) {
			referralrate = new BigDecimal(referralrate_);
		}
	 
		String isSmlAndLightStr = query.getIsSmlAndLightStr();//是否轻小
		boolean isSmlAndLight = false;
		if ("true".equals(isSmlAndLightStr)) {
			isSmlAndLight=true;
		}
	   
		List<String> countryList = new ArrayList<String>();
		if (isSmlAndLight) {
			countryList = Arrays.asList(ProfitServiceImpl.smlAndLightCountry);
		} else {
			countryList = this.profitService.findCountryList();// 得到需要计算的国家列表
		}
		ProfitConfig profitcfg = profitCfgService.findConfigAction(cfgId);
		Map<String, CostDetail> cuntryDetail = query.getCountry();
		for (int i = 0; i < countryList.size(); i++) {
			try {
				CostDetail costDetail=cuntryDetail.get(countryList.get(i));
			     if(costDetail!=null) {
				    costDetail = this.profitService.getCostDetail(query,costDetail, typeId, referralrate, isSmlAndLight,profitcfg);
				    cuntryDetail.put(countryList.get(i), costDetail);
				} else {
					cuntryDetail.put(countryList.get(i), null);
				}
			}catch(Exception e) {
				e.printStackTrace();
				throw new BizException("计算异常请联系管理员");
			}
			     
		}
		return Result.success(cuntryDetail);
	}
	
	@ApiOperation(value = "获取汇率")
	@GetMapping("/getCurrencyRate")
	public  Result<List<ExchangeRate>> getCurrencyRateAction(HttpServletRequest request) throws BizException {
		return Result.success( exchangeRateService.getExchangeRateLimit());
	}
	
	@ApiOperation(value = "更新汇率")
	@GetMapping("/updateExchangeRate")
	public Result<String> updateExchangeRateAction() {
		exchangeRateService.updateExchangeRate();
		return Result.success("success");
	}

 
	@GetMapping("/showProfitDetial")
	public Result<Map<String, Object>> showProfitDetial(String pid,String price,String productpricetype){
		UserInfo user= UserInfoContext.get();
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal myprice = GeneralUtil.getBigDecimal(price);
		map.put("pid", pid);
		map.put("myprice", myprice);
		if(StrUtil.isNotBlank(productpricetype)) {
			map.put("productpricetype", productpricetype);
		}
		List<ProfitConfig> profitCfgList =  profitCfgService.findProfitCfgName(user.getCompanyid());
		productInfoService.showProfitDetial(map);
		map.put("profitCfgList", profitCfgList);
		return Result.success(map);
	}
}
