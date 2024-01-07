package com.wimoor.amazon.profit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.profit.pojo.dto.ProfitQuery;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.pojo.vo.InputDimensions;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.impl.ProfitServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 

@Api(tags = "利润计算详情")
@RestController
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
    
	@ApiOperation(value = "计算")
	@PostMapping("/showCost")
	public Result<ProfitQuery> showCost(@RequestBody ProfitQuery query) {
		//calculateAmazonCostDetail
		// 获取用户输入信息
		String profitCfgId = query.getProfitCfgId();// 利润计算方案
		ProfitConfig profitCfgAll = profitCfgService.findConfigAction(profitCfgId);
		String cost_ = query.getCost();// 采购成本
		String currency =query.getCurrency();// 采购成本单位：RMB，USD，GBP，EUR
		String weight_ = query.getWeight();// 重量
		String WUnit = query.getWunit();// 重量单位
		String length_ =query.getLength();// 长
		String LUnit =query.getLunit();// 长度单位
		String width_ = query.getWidth();// 宽
		String height_ = query.getHeight();// 高
		if(StrUtil.isEmpty(cost_) || StrUtil.isEmpty(weight_) || StrUtil.isEmpty(length_) || StrUtil.isEmpty(width_) || StrUtil.isEmpty(height_)) {
			if(StrUtil.isEmpty(cost_)) {
				throw new BizException("请输入采购成本！");
			}
			if( StrUtil.isEmpty(weight_)) {
				throw new BizException("请输入重量！");
			}
			if(StrUtil.isEmpty(length_) || StrUtil.isEmpty(width_)|| StrUtil.isEmpty(height_)) {
				throw new BizException("请输入正确的长宽高！");
			}
		}
	
		BigDecimal cost = new BigDecimal(cost_);
		BigDecimal weight = new BigDecimal(weight_);
		BigDecimal length = new BigDecimal(length_);
		BigDecimal width = new BigDecimal(width_);
		BigDecimal height = new BigDecimal(height_);
		 
		int typeId =query.getTypeId();
		String type = this.profitService.findTypeById(typeId);// 产品类型名称
		String isMedia = this.profitService.isMedia(typeId);// 是否为媒介
		String shipment_ =query.getShipment();// 手动输入运费,单位与采购成本单位一致
		BigDecimal shipment = null;
		if (shipment_ != null && !"".equals(shipment_)&&"manually".equals(profitCfgAll.getShipmentstyle())) {
			shipment = new BigDecimal(shipment_);
		}
		String categories = query.getCategories();// 计算prep service fee
		
		String shipmentType = query.getShipmentType();//计算印度利润，local,regional,national
		String declaredValue_ = query.getDeclaredValue();//申报价值
		BigDecimal declaredValue = null;
		if (declaredValue_ != null && !"".equals(declaredValue_) && !"undefined".equals(declaredValue_)) {
			declaredValue = new BigDecimal(declaredValue_);
		}
		String declaredValueCur = query.getDeclaredValueCur();//申报价值单位
		String taxrate_ =query.getTaxrate();//印度进口税率
		BigDecimal taxrate = null;
		if (taxrate_ != null && !"".equals(taxrate_) && !"undefined".equals(taxrate_)) {
			taxrate = new BigDecimal(taxrate_);
		}
		String gstrate_ = query.getGstrate();//印度进口GST税率
		BigDecimal gstrate = null;
		if (gstrate_ != null && !"".equals(gstrate_) && !"undefined".equals(gstrate_)) {
			gstrate = new BigDecimal(gstrate_);
		}
		String sellingGSTRate_ = query.getSellingGSTRate();//印度销售GST税率
		BigDecimal sellingGSTRate = null;
		if (sellingGSTRate_ != null && !"".equals(sellingGSTRate_) && !"undefined".equals(sellingGSTRate_)) {
			sellingGSTRate = new BigDecimal(sellingGSTRate_);
		}
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
		Map<String, CostDetail> country = query.getCountry();
		for (int i = 0; i < countryList.size(); i++) {
			InputDimensions inputDimension = new InputDimensions(length, width, height, LUnit, weight, WUnit);
			try {
				CostDetail costDetail = this.profitService.initCostDetail(countryList.get(i), profitCfgAll, inputDimension,
						isMedia, type, typeId, cost, shipment, currency, categories, shipmentType, declaredValue,
						declaredValueCur, taxrate, gstrate, sellingGSTRate, referralrate, isSmlAndLight);
				CostDetail old = country.get( countryList.get(i));
				if(costDetail!=null) {
					if(old!=null) {
						costDetail.setSellingPrice(old.getSellingPrice());
					}
					country.put( countryList.get(i), costDetail);
				}
			}catch(Exception e) {
				e.printStackTrace();
				throw new BizException("计算异常请联系管理员");
			}
		}
		query.setProfitCfgAll( profitCfgAll);
		return Result.success(query);
	}
}
