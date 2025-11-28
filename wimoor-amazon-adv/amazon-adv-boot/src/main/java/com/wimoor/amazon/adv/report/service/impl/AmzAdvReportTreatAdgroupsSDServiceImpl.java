package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAdgroupsSDAttributedAllMapper;
import com.wimoor.amazon.adv.sd.pojo.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAdGroupsSDMapper;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatAdgroupsSDService")
public class AmzAdvReportTreatAdgroupsSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAdGroupsSDMapper amzAdvReportAdGroupsSDMapper;
	@Resource
	AmzAdvReportAdgroupsSDAttributedAllMapper	amzAdvReportAdgroupsSDAttributedAllMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub

			final List<AmzAdvReportAdGroupsSD> list = new LinkedList<AmzAdvReportAdGroupsSD>();
			final List<AmzAdvReportAdgroupsSDAttributedAll> listAttributed = new LinkedList<AmzAdvReportAdgroupsSDAttributedAll>();

			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAdGroupsSD amzAdvReportAdGroupsSD = new AmzAdvReportAdGroupsSD();
					AmzAdvReportAdgroupsSDAttributedAll amzAdvReportAdGroupsSDAttributedall = new AmzAdvReportAdgroupsSDAttributedAll();

					amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
					amzAdvReportAdGroupsSD.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportAdGroupsSD.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportAdGroupsSD.setBydate(request.getStartDate());
					}else {
						amzAdvReportAdGroupsSD.setBydate(date);
					}

					amzAdvReportAdGroupsSD.setClicks(item.getInteger("clicks"));
					amzAdvReportAdGroupsSD.setImpressions(item.getInteger("impressions"));
					amzAdvReportAdGroupsSD.setCost(item.getBigDecimal("cost"));
					amzAdvReportAdGroupsSD.setOpttime(new Date());

					amzAdvReportAdGroupsSDAttributedall.setAdGroupId(amzAdvReportAdGroupsSD.getAdgroupid().toString());
					amzAdvReportAdGroupsSDAttributedall.setBydate(amzAdvReportAdGroupsSD.getBydate());
					// 设置购物车相关字段
					amzAdvReportAdGroupsSDAttributedall.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportAdGroupsSDAttributedall.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportAdGroupsSDAttributedall.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportAdGroupsSDAttributedall.setAddToCartViews(item.getInteger("addToCartViews"));

// 设置列表相关字段
					amzAdvReportAdGroupsSDAttributedall.setAddToList(item.getInteger("addToList"));
					amzAdvReportAdGroupsSDAttributedall.setAddToListFromClicks(item.getInteger("addToListFromClicks"));
					amzAdvReportAdGroupsSDAttributedall.setAddToListFromViews(item.getInteger("addToListFromViews"));

// 设置借阅相关字段
					amzAdvReportAdGroupsSDAttributedall.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportAdGroupsSDAttributedall.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportAdGroupsSDAttributedall.setQualifiedBorrowsFromViews(item.getInteger("qualifiedBorrowsFromViews"));
					amzAdvReportAdGroupsSDAttributedall.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportAdGroupsSDAttributedall.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));
					amzAdvReportAdGroupsSDAttributedall.setRoyaltyQualifiedBorrowsFromViews(item.getInteger("royaltyQualifiedBorrowsFromViews"));

// 设置品牌搜索相关字段
					amzAdvReportAdGroupsSDAttributedall.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportAdGroupsSDAttributedall.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));
					amzAdvReportAdGroupsSDAttributedall.setBrandedSearchesViews(item.getInteger("brandedSearchesViews"));
					amzAdvReportAdGroupsSDAttributedall.setBrandedSearchRate(item.getBigDecimal("brandedSearchRate"));

// 设置预算和详情页字段
					amzAdvReportAdGroupsSDAttributedall.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));
					amzAdvReportAdGroupsSDAttributedall.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportAdGroupsSDAttributedall.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));

// 设置成本相关字段
					amzAdvReportAdGroupsSDAttributedall.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));
					amzAdvReportAdGroupsSDAttributedall.setECPBrandSearch(item.getBigDecimal("eCPBrandSearch"));

// 设置新品牌相关字段
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportAdGroupsSDAttributedall.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));

// 设置购买和销售相关字段
					amzAdvReportAdGroupsSDAttributedall.setPurchases(item.getInteger("purchases"));
					amzAdvReportAdGroupsSDAttributedall.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportAdGroupsSDAttributedall.setPurchasesPromotedClicks(item.getInteger("purchasesPromotedClicks"));
					amzAdvReportAdGroupsSDAttributedall.setSales(item.getBigDecimal("sales"));
					amzAdvReportAdGroupsSDAttributedall.setSalesClicks(item.getBigDecimal("salesClicks"));
					amzAdvReportAdGroupsSDAttributedall.setSalesPromotedClicks(item.getBigDecimal("salesPromotedClicks"));
					amzAdvReportAdGroupsSDAttributedall.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportAdGroupsSDAttributedall.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));

// 设置视频相关字段
					amzAdvReportAdGroupsSDAttributedall.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportAdGroupsSDAttributedall.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportAdGroupsSDAttributedall.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportAdGroupsSDAttributedall.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportAdGroupsSDAttributedall.setVideoUnmutes(item.getInteger("videoUnmutes"));

// 设置可视性相关字段
					amzAdvReportAdGroupsSDAttributedall.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportAdGroupsSDAttributedall.setViewClickThroughRate(item.getBigDecimal("viewClickThroughRate"));

// 设置操作时间
					amzAdvReportAdGroupsSDAttributedall.setOpttime(new Date());





					list.add(amzAdvReportAdGroupsSD);
					if(!amzAdvReportAdGroupsSDAttributedall.isZero()) {
						listAttributed.add(amzAdvReportAdGroupsSDAttributedall);
					}
					if (list.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatch(list);
						list.clear();
					}

					if (listAttributed.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}

				}
				if (list.size() > 0) {
					amzAdvReportAdGroupsSDMapper.insertBatch(list);
				}
				if (listAttributed.size() >0) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
				}

			} finally {
				try {
					if (jsonReader != null) {
						jsonReader.close();
					}
				} catch (Exception e) {
				}
			}
		}

	 
}
