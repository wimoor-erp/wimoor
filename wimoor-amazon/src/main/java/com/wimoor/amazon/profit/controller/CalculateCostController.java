package com.wimoor.amazon.profit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
 

 
@Controller
@RequestMapping("/api/v1/profit/calculateCost")
public class CalculateCostController {
	@Resource
	private IProfitService profitService;
	@Resource
	IProfitCfgService profitCfgService;
    @Resource
    IMarketplaceService marketplaceService;
    @Resource
    IExchangeRateHandlerService exchangeRateHandlerService;
	@ResponseBody
	@RequestMapping("/showCost")
	public Map<String, Object> showCost(HttpServletRequest request, HttpServletResponse response) {
		String profittype=request.getParameter("profittype");
		if(profittype!=null&&profittype.equals("single")) {
			String profitCfgId = request.getParameter("profitCfg");// 利润计算方案
			String price = request.getParameter("price");// 高
			String shipment = request.getParameter("shipment");// 高
			String type = request.getParameter("ftype"); 
			String cost_ = request.getParameter("cost");// 高
	        String marketplaceid=request.getParameter("marketplaceid");// 高
	        String sku = request.getParameter("sku");// 高
	        String costcurrency = request.getParameter("costcurrency"); 
	        String shipmentcurrency = request.getParameter("shipmentcurrency"); 
			Marketplace marketplace = marketplaceService.selectByPKey(marketplaceid);
			price=price.replace(GeneralUtil.formatCurrency(marketplace.getCurrency()), "");
			price=price.replace("undefined", "");
			price=price.replace("￥", "");
			price=price.replace(",", "");
			price=price.trim();
			if("".equals(price)) {
				price="0";
			}
			BigDecimal cost = new BigDecimal("0");
			if (cost_ != null && !"".equals(cost_)) {
				cost = new BigDecimal(cost_);
			}
			if("RMB".equals(costcurrency)) {
				cost=exchangeRateHandlerService.changeCurrencyByLocal("CNY", marketplace.getCurrency(), cost);
			}
			BigDecimal mshipment = new BigDecimal("0");
			if (shipment != null && !"".equals(shipment)) {
				mshipment = new BigDecimal(shipment);
			}
			if("RMB".equals(shipmentcurrency)&&!StrUtil.isEmpty(shipment)) {
				mshipment=exchangeRateHandlerService.changeCurrencyByLocal("CNY", marketplace.getCurrency(), mshipment);
			}
			UserInfo user= UserInfoContext.get();
			
			return this.profitService.calculateAmazonCostDetail(user,marketplaceid,
																sku,profitCfgId,cost,type,
																new BigDecimal(price),mshipment
																);
		}else {
			//calculateAmazonCostDetail
		    Map<String, Object> resultMap = new HashMap<String, Object>();
			// 获取用户输入信息
			String profitCfgId = request.getParameter("profitCfg");// 利润计算方案
			ProfitConfig profitCfgAll = profitCfgService.findConfigAction(profitCfgId);
			String cost_ = request.getParameter("cost");// 采购成本
			String currency = request.getParameter("currency");// 采购成本单位：RMB，USD，GBP，EUR
			String weight_ = request.getParameter("weight");// 重量
			String WUnit = request.getParameter("WUnit");// 重量单位
			String length_ = request.getParameter("length");// 长
			String LUnit = request.getParameter("LUnit");// 长度单位
			String width_ = request.getParameter("width");// 宽
			String height_ = request.getParameter("height");// 高
			if(StrUtil.isEmpty(cost_) || StrUtil.isEmpty(weight_) || StrUtil.isEmpty(length_) || StrUtil.isEmpty(width_)
					|| StrUtil.isEmpty(height_)) {
				throw new BizException("请输入正确的长宽高！");
			}
			BigDecimal cost = new BigDecimal(cost_);
			BigDecimal weight = new BigDecimal(weight_);
			BigDecimal length = new BigDecimal(length_);
			BigDecimal width = new BigDecimal(width_);
			BigDecimal height = new BigDecimal(height_);
			String type_ = request.getParameter("type");// 产品类型，传过来的是id
			int typeId = Integer.parseInt(type_);
			String type = this.profitService.findTypeById(typeId);// 产品类型名称
			String isMedia = this.profitService.isMedia(typeId);// 是否为媒介
			String shipment_ = request.getParameter("shipment");// 手动输入运费,单位与采购成本单位一致
			BigDecimal shipment = null;
			if (shipment_ != null && !"".equals(shipment_)&&"manually".equals(profitCfgAll.getShipmentstyle())) {
				shipment = new BigDecimal(shipment_);
			}
			String categories = request.getParameter("category");// 计算prep service fee
			
			String shipmentType = request.getParameter("shipmentType");//计算印度利润，local,regional,national
			String declaredValue_ = request.getParameter("declaredValue");//申报价值
			BigDecimal declaredValue = null;
			if (declaredValue_ != null && !"".equals(declaredValue_) && !"undefined".equals(declaredValue_)) {
				declaredValue = new BigDecimal(declaredValue_);
			}
			String declaredValueCur = request.getParameter("declaredValueCurrency");//申报价值单位
			String taxrate_ = request.getParameter("taxrate");//印度进口税率
			BigDecimal taxrate = null;
			if (taxrate_ != null && !"".equals(taxrate_) && !"undefined".equals(taxrate_)) {
				taxrate = new BigDecimal(taxrate_);
			}
			String gstrate_ = request.getParameter("gstrate");//印度进口GST税率
			BigDecimal gstrate = null;
			if (gstrate_ != null && !"".equals(gstrate_) && !"undefined".equals(gstrate_)) {
				gstrate = new BigDecimal(gstrate_);
			}
			String sellingGSTRate_ = request.getParameter("sellingGSTRate");//印度销售GST税率
			BigDecimal sellingGSTRate = null;
			if (sellingGSTRate_ != null && !"".equals(sellingGSTRate_) && !"undefined".equals(sellingGSTRate_)) {
				sellingGSTRate = new BigDecimal(sellingGSTRate_);
			}
			String referralrate_ = request.getParameter("referralrate");//印度佣金比率
			BigDecimal referralrate = null;
			if (referralrate_ != null && !"".equals(referralrate_.trim()) && !"undefined".equals(referralrate_)) {
				referralrate = new BigDecimal(referralrate_);
			}
			
			String isSmlAndLightStr = request.getParameter("isSmlAndLight");//是否轻小
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
			for (int i = 0; i < countryList.size(); i++) {
				InputDimensions inputDimension = new InputDimensions(length, width, height, LUnit, weight, WUnit);
				CostDetail costDetail = this.profitService.initCostDetail(countryList.get(i), profitCfgAll, inputDimension,
						isMedia, type, typeId, cost, shipment, currency, categories, shipmentType, declaredValue,
						declaredValueCur, taxrate, gstrate, sellingGSTRate, referralrate, isSmlAndLight);
				resultMap.put("costDetail_" + countryList.get(i), costDetail);
			}

			List<BigDecimal> marginList = this.profitService.findMarginList();// 固定利率列表
			resultMap.put("marginList", marginList);
			resultMap.put("profitCfgAll", profitCfgAll);
			return resultMap;
		}
		
	}

 
	
 

}
