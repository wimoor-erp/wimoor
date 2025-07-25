package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.sd.pojo.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductTargeSDMapper;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatProductTargetSDService")
public class AmzAdvReportTreatProductTargetSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductTargeSDMapper amzAdvReportProductTargeSDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			  List<AmzAdvReportProductTargetsSD> list = new LinkedList<AmzAdvReportProductTargetsSD>();
			  List<AmzAdvReportProductTargetsSDAttributedAll> listAttributed = new LinkedList<AmzAdvReportProductTargetsSDAttributedAll>();

			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductTargetsSD amzAdvReportProductTarge = new AmzAdvReportProductTargetsSD();
					AmzAdvReportProductTargetsSDAttributedAll amzAdvReportProductTargetsSDAttributed = new AmzAdvReportProductTargetsSDAttributedAll();
					amzAdvReportProductTarge.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportProductTarge.setTargetid(item.getBigInteger("targetingId"));
					amzAdvReportProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				 

					amzAdvReportProductTarge.setClicks(item.getInteger("clicks"));
					amzAdvReportProductTarge.setImpressions(item.getInteger("impressions"));
					amzAdvReportProductTarge.setCost(item.getBigDecimal("cost"));
					amzAdvReportProductTarge.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportProductTarge.setBydate(request.getStartDate());
					}else {
						amzAdvReportProductTarge.setBydate(date);
					}

					amzAdvReportProductTargetsSDAttributed.setTargetingId(amzAdvReportProductTarge.getTargetid().toString());
					amzAdvReportProductTargetsSDAttributed.setBydate(amzAdvReportProductTarge.getBydate());
					// 购物车相关字段
					amzAdvReportProductTargetsSDAttributed.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportProductTargetsSDAttributed.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportProductTargetsSDAttributed.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportProductTargetsSDAttributed.setAddToCartViews(item.getInteger("addToCartViews"));

// 列表相关字段
					amzAdvReportProductTargetsSDAttributed.setAddToList(item.getInteger("addToList"));
					amzAdvReportProductTargetsSDAttributed.setAddToListFromClicks(item.getInteger("addToListFromClicks"));
					amzAdvReportProductTargetsSDAttributed.setAddToListFromViews(item.getInteger("addToListFromViews"));

// 借阅相关字段
					amzAdvReportProductTargetsSDAttributed.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportProductTargetsSDAttributed.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportProductTargetsSDAttributed.setQualifiedBorrowsFromViews(item.getInteger("qualifiedBorrowsFromViews"));
					amzAdvReportProductTargetsSDAttributed.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportProductTargetsSDAttributed.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));
					amzAdvReportProductTargetsSDAttributed.setRoyaltyQualifiedBorrowsFromViews(item.getInteger("royaltyQualifiedBorrowsFromViews"));

// 品牌搜索相关字段
					amzAdvReportProductTargetsSDAttributed.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportProductTargetsSDAttributed.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));
					amzAdvReportProductTargetsSDAttributed.setBrandedSearchesViews(item.getInteger("brandedSearchesViews"));
					amzAdvReportProductTargetsSDAttributed.setBrandedSearchRate(item.getBigDecimal("brandedSearchRate"));

// 预算和货币代码
					amzAdvReportProductTargetsSDAttributed.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));

// 详情页相关字段
					amzAdvReportProductTargetsSDAttributed.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportProductTargetsSDAttributed.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));

// 成本相关字段
					amzAdvReportProductTargetsSDAttributed.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));
					amzAdvReportProductTargetsSDAttributed.setECPBrandSearch(item.getBigDecimal("eCPBrandSearch"));

// 展示相关字段
					amzAdvReportProductTargetsSDAttributed.setImpressionsViews(item.getInteger("impressionsViews"));

// 新品牌相关-详情页
					amzAdvReportProductTargetsSDAttributed.setNewToBrandDetailPageViewRate(item.getBigDecimal("newToBrandDetailPageViewRate"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandDetailPageViews(item.getInteger("newToBrandDetailPageViews"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandDetailPageViewsClicks(item.getInteger("newToBrandDetailPageViewsClicks"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandECPDetailPageView(item.getInteger("newToBrandECPDetailPageView"));

// 新品牌相关-购买
					amzAdvReportProductTargetsSDAttributed.setNewToBrandPurchasesPercentage(item.getBigDecimal("newToBrandPurchasesPercentage"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandPurchasesRate(item.getBigDecimal("newToBrandPurchasesRate"));

// 新品牌相关-销售
					amzAdvReportProductTargetsSDAttributed.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandSalesPercentage(item.getBigDecimal("newToBrandSalesPercentage"));

// 新品牌相关-销售单位
					amzAdvReportProductTargetsSDAttributed.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));
					amzAdvReportProductTargetsSDAttributed.setNewToBrandUnitsSoldPercentage(item.getBigDecimal("newToBrandUnitsSoldPercentage"));

// 购买相关字段
					amzAdvReportProductTargetsSDAttributed.setPurchases(item.getInteger("purchases"));
					amzAdvReportProductTargetsSDAttributed.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportProductTargetsSDAttributed.setPurchasesPromoted(item.getInteger("purchasesPromoted"));
					amzAdvReportProductTargetsSDAttributed.setPurchasesPromotedClicks(item.getInteger("purchasesPromotedClicks"));

// 销售相关字段
					amzAdvReportProductTargetsSDAttributed.setSales(item.getBigDecimal("sales"));
					amzAdvReportProductTargetsSDAttributed.setSalesClicks(item.getBigDecimal("salesClicks"));
					amzAdvReportProductTargetsSDAttributed.setSalesPromoted(item.getBigDecimal("salesPromoted"));
					amzAdvReportProductTargetsSDAttributed.setSalesPromotedClicks(item.getBigDecimal("salesPromotedClicks"));

// 销售单位相关字段
					amzAdvReportProductTargetsSDAttributed.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportProductTargetsSDAttributed.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));

// 视频相关字段
					amzAdvReportProductTargetsSDAttributed.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
					amzAdvReportProductTargetsSDAttributed.setVideo5SecondViews(item.getInteger("video5SecondViews"));
					amzAdvReportProductTargetsSDAttributed.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportProductTargetsSDAttributed.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportProductTargetsSDAttributed.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportProductTargetsSDAttributed.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportProductTargetsSDAttributed.setVideoUnmutes(item.getInteger("videoUnmutes"));

// 可视性相关字段
					amzAdvReportProductTargetsSDAttributed.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportProductTargetsSDAttributed.setViewClickThroughRate(item.getBigDecimal("viewClickThroughRate"));

// 操作时间
					amzAdvReportProductTargetsSDAttributed.setOpttime(new Date());



					if ((amzAdvReportProductTarge.getImpressions() == null || amzAdvReportProductTarge.getImpressions() == 0)) {
						continue;
					}
					
					if(!amzAdvReportProductTargetsSDAttributed.isZero()) {
						listAttributed.add(amzAdvReportProductTargetsSDAttributed);
					}

					list.add(amzAdvReportProductTarge);
					if (list.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}

				}
				if (list.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}


	 
}
