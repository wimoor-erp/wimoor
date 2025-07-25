package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.sd.pojo.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductAdsSDMapper;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatProductSDService")
public class AmzAdvReportTreatProductSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductAdsSDMapper amzAdvReportProductAdsSDMapper;
	@Resource
	protected IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
			List<AmzAdvReportProductAdsSD> list = new LinkedList<AmzAdvReportProductAdsSD>();
			List<AmzAdvReportProductadsSDAttributedAll> listAttributed = new LinkedList<AmzAdvReportProductadsSDAttributedAll>();
			try {
				jsonReader.startArray();

				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductAdsSD padv = new AmzAdvReportProductAdsSD();
					AmzAdvReportProductadsSDAttributedAll     amzAdvReportProductadsSDAttributedAll =     new AmzAdvReportProductadsSDAttributedAll();
					Date date = item.getDate("date");
					if(date==null) {
						padv.setBydate(request.getStartDate());
					}else {
						padv.setBydate(date);
					}
					padv.setAdid(item.getBigInteger("adId"));
					padv.setProfileid(request.getProfileid());
					padv.setCampaignid(item.getBigInteger("campaignId"));
					padv.setAdgroupid(item.getBigInteger("adGroupId"));
					padv.setImpressions(item.getInteger("impressions"));
					padv.setClicks(item.getInteger("clicks"));
					padv.setCost(item.getBigDecimal("cost"));
					padv.setOpttime(new Date());

					if(padv.getAdid()==null) {
						continue;
					}
					amzAdvReportProductadsSDAttributedAll.setAdId(padv.getAdid().toString());
					amzAdvReportProductadsSDAttributedAll.setBydate(padv.getBydate());
					// 购物车相关字段
					amzAdvReportProductadsSDAttributedAll.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportProductadsSDAttributedAll.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportProductadsSDAttributedAll.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportProductadsSDAttributedAll.setAddToCartViews(item.getInteger("addToCartViews"));

// 列表相关字段
					amzAdvReportProductadsSDAttributedAll.setAddToList(item.getInteger("addToList"));
					amzAdvReportProductadsSDAttributedAll.setAddToListFromClicks(item.getInteger("addToListFromClicks"));

// 借阅相关字段
					amzAdvReportProductadsSDAttributedAll.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportProductadsSDAttributedAll.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportProductadsSDAttributedAll.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportProductadsSDAttributedAll.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));

// 竞价优化和品牌搜索
					amzAdvReportProductadsSDAttributedAll.setBidOptimization(item.getString("bidOptimization"));
					amzAdvReportProductadsSDAttributedAll.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportProductadsSDAttributedAll.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));

// 预算相关
					amzAdvReportProductadsSDAttributedAll.setCampaignBudgetAmount(item.getBigDecimal("campaignBudgetAmount"));
					amzAdvReportProductadsSDAttributedAll.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));

// 详情页相关
					amzAdvReportProductadsSDAttributedAll.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportProductadsSDAttributedAll.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));
					amzAdvReportProductadsSDAttributedAll.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));

// 新品牌相关-详情页
					amzAdvReportProductadsSDAttributedAll.setNewToBrandDetailPageViewRate(item.getBigDecimal("newToBrandDetailPageViewRate"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandDetailPageViews(item.getInteger("newToBrandDetailPageViews"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandDetailPageViewsClicks(item.getInteger("newToBrandDetailPageViewsClicks"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandECPDetailPageView(item.getInteger("newToBrandECPDetailPageView"));

// 新品牌相关-购买
					amzAdvReportProductadsSDAttributedAll.setNewToBrandPurchasesPercentage(item.getBigDecimal("newToBrandPurchasesPercentage"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandPurchasesRate(item.getBigDecimal("newToBrandPurchasesRate"));

// 新品牌相关-销售
					amzAdvReportProductadsSDAttributedAll.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandSalesPercentage(item.getBigDecimal("newToBrandSalesPercentage"));

// 新品牌相关-销售单位
					amzAdvReportProductadsSDAttributedAll.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));
					amzAdvReportProductadsSDAttributedAll.setNewToBrandUnitsSoldPercentage(item.getBigDecimal("newToBrandUnitsSoldPercentage"));

// 购买相关
					amzAdvReportProductadsSDAttributedAll.setPurchases(item.getInteger("purchases"));
					amzAdvReportProductadsSDAttributedAll.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportProductadsSDAttributedAll.setPurchasesPromoted(item.getInteger("purchasesPromoted"));

// 销售相关
					amzAdvReportProductadsSDAttributedAll.setSales(item.getBigDecimal("sales"));
					amzAdvReportProductadsSDAttributedAll.setSalesClicks(item.getBigDecimal("salesClicks"));
					amzAdvReportProductadsSDAttributedAll.setSalesPromoted(item.getBigDecimal("salesPromoted"));

// 销售单位
					amzAdvReportProductadsSDAttributedAll.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportProductadsSDAttributedAll.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));

// 视频相关
					amzAdvReportProductadsSDAttributedAll.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
					amzAdvReportProductadsSDAttributedAll.setVideo5SecondViews(item.getInteger("video5SecondViews"));
					amzAdvReportProductadsSDAttributedAll.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportProductadsSDAttributedAll.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportProductadsSDAttributedAll.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportProductadsSDAttributedAll.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportProductadsSDAttributedAll.setVideoUnmutes(item.getInteger("videoUnmutes"));

// 可视性相关
					amzAdvReportProductadsSDAttributedAll.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportProductadsSDAttributedAll.setViewableImpressions(item.getInteger("viewableImpressions"));

// 操作时间
					amzAdvReportProductadsSDAttributedAll.setOpttime(new Date());

					if(!amzAdvReportProductadsSDAttributedAll.isZero()) {
						listAttributed.add(amzAdvReportProductadsSDAttributedAll);
					}

					if ((padv.getClicks() == null || padv.getClicks() == 0)
							&& (padv.getImpressions() == null || padv.getImpressions() == 0)
							&& (padv.getCost() == null || padv.getCost().compareTo(new BigDecimal("0")) == 0)
							 ) {
						continue;
					}
					list.add(padv);

				}
				if (list.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatchAttributed(listAttributed);
				}

			} catch(Exception e){
				e.printStackTrace();
			} 
		}

}
