package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.sb.pojo.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportPlacementService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsPlaceHsaMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatCampaignsPlaceHsaService")
public class AmzAdvReportTreatCampaignsPlaceHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportCampaignsPlaceHsaMapper amzAdvReportCampaignsPlaceHsaMapper;
	@Resource
	IAmzAdvReportPlacementService amzAdvReportPlacementService;
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportCampaignsPlaceHsa> list = new LinkedList<AmzAdvReportCampaignsPlaceHsa>();
			final List<AmzAdvReportCampaignsPlaceHsaAttributedAll> listAttributed = new LinkedList<AmzAdvReportCampaignsPlaceHsaAttributedAll>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportCampaignsPlaceHsa amzAdvReportHasCampaignPlace = new AmzAdvReportCampaignsPlaceHsa();
					AmzAdvReportCampaignsPlaceHsaAttributedAll amzAdvReportCampaignsPlaceHsaAttributed=new AmzAdvReportCampaignsPlaceHsaAttributedAll();

					amzAdvReportHasCampaignPlace.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportHasCampaignPlace.setCampaignbudget(item.getBigDecimal("campaignBudgetAmount"));
					String placement = item.getString("placementClassification");
					if(placement==null){
						continue;
					}
					AmzAdvReportPlacement objplacement = amzAdvReportPlacementService.loadIDByPlacementName(placement);
					amzAdvReportHasCampaignPlace.setPlacementid(objplacement.getId());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportHasCampaignPlace.setBydate(request.getStartDate());
					}else {
						amzAdvReportHasCampaignPlace.setBydate(date);
					}
					amzAdvReportHasCampaignPlace.setProfileid(request.getProfileid());
					amzAdvReportHasCampaignPlace.setOpttime(new Date());
					amzAdvReportHasCampaignPlace.setClicks(item.getInteger("clicks"));
					amzAdvReportHasCampaignPlace.setImpressions(item.getInteger("impressions"));
					amzAdvReportHasCampaignPlace.setCost(item.getBigDecimal("cost"));

					amzAdvReportCampaignsPlaceHsaAttributed.setCampaignId(amzAdvReportHasCampaignPlace.getCampaignid().toString());
					amzAdvReportCampaignsPlaceHsaAttributed.setBydate(amzAdvReportHasCampaignPlace.getBydate());
					amzAdvReportCampaignsPlaceHsaAttributed.setOpttime(new Date());
					amzAdvReportCampaignsPlaceHsaAttributed.setPlacementid(objplacement.getId());
					amzAdvReportCampaignsPlaceHsaAttributed.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAddToList(item.getInteger("addToList"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAddToListFromClicks(item.getInteger("addToListFromClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportCampaignsPlaceHsaAttributed.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportCampaignsPlaceHsaAttributed.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportCampaignsPlaceHsaAttributed.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setCampaignBudgetAmount(item.getBigDecimal("campaignBudgetAmount"));
					amzAdvReportCampaignsPlaceHsaAttributed.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));
					amzAdvReportCampaignsPlaceHsaAttributed.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandDetailPageViewRate(item.getBigDecimal("newToBrandDetailPageViewRate"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandDetailPageViews(item.getInteger("newToBrandDetailPageViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandDetailPageViewsClicks(item.getInteger("newToBrandDetailPageViewsClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandECPDetailPageView(item.getBigDecimal("newToBrandECPDetailPageView"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandPurchasesPercentage(item.getBigDecimal("newToBrandPurchasesPercentage"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandPurchasesRate(item.getBigDecimal("newToBrandPurchasesRate"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandSalesPercentage(item.getBigDecimal("newToBrandSalesPercentage"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setNewToBrandUnitsSoldPercentage(item.getBigDecimal("newToBrandUnitsSoldPercentage"));
					amzAdvReportCampaignsPlaceHsaAttributed.setPurchases(item.getInteger("purchases"));
					amzAdvReportCampaignsPlaceHsaAttributed.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setPurchasesPromoted(item.getInteger("purchasesPromoted"));
					amzAdvReportCampaignsPlaceHsaAttributed.setSales(item.getBigDecimal("sales"));
					amzAdvReportCampaignsPlaceHsaAttributed.setSalesClicks(item.getInteger("salesClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setSalesPromoted(item.getBigDecimal("salesPromoted"));
					amzAdvReportCampaignsPlaceHsaAttributed.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportCampaignsPlaceHsaAttributed.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideo5SecondViews(item.getInteger("video5SecondViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportCampaignsPlaceHsaAttributed.setVideoUnmutes(item.getInteger("videoUnmutes"));
					amzAdvReportCampaignsPlaceHsaAttributed.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportCampaignsPlaceHsaAttributed.setViewableImpressions(item.getInteger("viewableImpressions"));
					amzAdvReportCampaignsPlaceHsaAttributed.setViewClickThroughRate(item.getBigDecimal("viewClickThroughRate"));

					if(!amzAdvReportCampaignsPlaceHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportCampaignsPlaceHsaAttributed);
					}

					list.add(amzAdvReportHasCampaignPlace);
					if (list.size() >= 2000) {
						amzAdvReportCampaignsPlaceHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportCampaignsPlaceHsaMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}

				}
				if (list.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchAttributed(listAttributed);
				}
			} catch(Exception e){
				e.printStackTrace();
			} 
		}


	 
}
