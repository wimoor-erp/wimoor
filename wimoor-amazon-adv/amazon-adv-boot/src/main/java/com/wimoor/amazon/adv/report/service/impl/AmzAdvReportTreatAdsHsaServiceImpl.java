package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
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
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportAdsHsaMapper;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatAdHsaService")
public class AmzAdvReportTreatAdsHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAdsHsaMapper amzAdvReportAdsHsaMapper;
	
	
/*
{
        "adProduct": "SPONSORED_BRANDS",
        "groupBy": ["ads"],
        "columns": [
					"addToCart","addToCartClicks","addToCartRate",
					"adGroupId","adId","brandedSearches","brandedSearchesClicks",
					"campaignId","adStatus",
					"clicks","cost","impressions","costType","date","detailPageViews","detailPageViewsClicks","eCPAddToCart",
					"newToBrandDetailPageViewRate","newToBrandDetailPageViews","newToBrandDetailPageViewsClicks",
					"newToBrandECPDetailPageView",
					"newToBrandPurchases","newToBrandPurchasesClicks","newToBrandPurchasesPercentage","newToBrandPurchasesRate",
					"newToBrandSales","newToBrandSalesClicks","newToBrandSalesPercentage",
					"newToBrandUnitsSold","newToBrandUnitsSoldClicks","newToBrandUnitsSoldPercentage",
					"purchases","purchasesClicks","purchasesPromoted",
					"sales","salesClicks","salesPromoted",
					"unitsSold","unitsSoldClicks","video5SecondViewRate","video5SecondViews","videoCompleteViews",
					"videoFirstQuartileViews","videoMidpointViews","videoThirdQuartileViews",
					"videoUnmutes","viewabilityRate","viewableImpressions"
        ],
        "reportTypeId": "sbAds",
        "timeUnit": "DAILY",
        "format": "GZIP_JSON"
    }	
 */
	@Resource
	AmzAdvAdsHsaMapper amzAdvAdsHsaMapper;
	  
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportAdsHsa> list = new LinkedList<AmzAdvReportAdsHsa>();
			final List<AmzAdvReportAdsHsaAttributedAll> listAttributed = new LinkedList<AmzAdvReportAdsHsaAttributedAll>();
			final List<AmzAdvReportAdsHsaBrand> listBrand = new LinkedList<AmzAdvReportAdsHsaBrand>();
			final List<AmzAdvReportAdsHsaVideo> listVideo = new LinkedList<AmzAdvReportAdsHsaVideo>();
			try {
				jsonReader.startArray();
				Integer clicks = 0;
				String currency = "";
				Integer impressions = 0;
				BigDecimal cost = new BigDecimal("0");
				BigDecimal attributedsales = new BigDecimal("0");
				Integer attributedunitsordered = 0;
				Integer attributedConversions = 0;
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAdsHsa amzAdvReportHas = new AmzAdvReportAdsHsa();
					AmzAdvReportAdsHsaAttributedAll amzAdvReportAdsHsaAttributed=new AmzAdvReportAdsHsaAttributedAll();
					AmzAdvReportAdsHsaVideo amzAdvReportHasVideo = null;
					AmzAdvReportAdsHsaBrand amzAdvReportHasBrand = null;
					amzAdvReportHas.setAdid(item.getBigInteger("adId"));
					amzAdvReportHas.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportHas.setCampaignid(item.getBigInteger("campaignId"));
            	   AmzAdvAdsHsa adgroup = amzAdvAdsHsaMapper.selectByPrimaryKey(amzAdvReportHas.getAdgroupid());
            	   if(adgroup==null) {
            		   adgroup= new AmzAdvAdsHsa();
            		   adgroup.setProfileid(request.getProfileid());
            		   adgroup.setCampaignid(amzAdvReportHas.getCampaignid());
            		   adgroup.setAdgroupid(amzAdvReportHas.getAdgroupid());
            		   adgroup.setAdid(item.getBigInteger("adId"));
            		   adgroup.setState(item.getString("adStatus"));
            		   if(StrUtil.isNotBlank(adgroup.getName())) {
            			   amzAdvAdsHsaMapper.insert(adgroup);
            		   }
            	   }
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportHas.setBydate(request.getStartDate());
					}else {
						amzAdvReportHas.setBydate(date);
					}
					
					if(amzAdvReportHas.getAdid()==null) {
						amzAdvReportHas.setAdid(amzAdvReportHas.getAdgroupid());
					}
					if(amzAdvReportHas.getAdid()==null) {
						amzAdvReportHas.setAdid(amzAdvReportHas.getCampaignid());
					}
					amzAdvReportHas.setProfileid(request.getProfileid());
					amzAdvReportHas.setOpttime(new Date());
					amzAdvReportHas.setClicks(item.getInteger("clicks"));
					amzAdvReportHas.setImpressions(item.getInteger("impressions"));
					amzAdvReportHas.setCost(item.getBigDecimal("cost"));

					amzAdvReportAdsHsaAttributed.setAdId(amzAdvReportHas.getAdid().toString());
					amzAdvReportAdsHsaAttributed.setBydate(amzAdvReportHas.getBydate());
					// 购物车相关字段
					amzAdvReportAdsHsaAttributed.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportAdsHsaAttributed.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportAdsHsaAttributed.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportAdsHsaAttributed.setAddToList(item.getInteger("addToList"));
					amzAdvReportAdsHsaAttributed.setAddToListFromClicks(item.getInteger("addToListFromClicks"));

// 借阅相关字段
					amzAdvReportAdsHsaAttributed.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportAdsHsaAttributed.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportAdsHsaAttributed.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportAdsHsaAttributed.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));

// 品牌搜索相关字段
					amzAdvReportAdsHsaAttributed.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportAdsHsaAttributed.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));

// 预算相关字段
					amzAdvReportAdsHsaAttributed.setCampaignBudgetAmount(item.getBigDecimal("campaignBudgetAmount"));
					amzAdvReportAdsHsaAttributed.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));

// 详情页相关字段
					amzAdvReportAdsHsaAttributed.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportAdsHsaAttributed.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));
					amzAdvReportAdsHsaAttributed.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));

// 新品牌相关-详情页
					amzAdvReportAdsHsaAttributed.setNewToBrandDetailPageViewRate(item.getBigDecimal("newToBrandDetailPageViewRate"));
					amzAdvReportAdsHsaAttributed.setNewToBrandDetailPageViews(item.getInteger("newToBrandDetailPageViews"));
					amzAdvReportAdsHsaAttributed.setNewToBrandDetailPageViewsClicks(item.getInteger("newToBrandDetailPageViewsClicks"));
					amzAdvReportAdsHsaAttributed.setNewToBrandECPDetailPageView(item.getBigDecimal("newToBrandECPDetailPageView"));

// 新品牌相关-购买
					amzAdvReportAdsHsaAttributed.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportAdsHsaAttributed.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportAdsHsaAttributed.setNewToBrandPurchasesPercentage(item.getBigDecimal("newToBrandPurchasesPercentage"));
					amzAdvReportAdsHsaAttributed.setNewToBrandPurchasesRate(item.getBigDecimal("newToBrandPurchasesRate"));

// 新品牌相关-销售
					amzAdvReportAdsHsaAttributed.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportAdsHsaAttributed.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportAdsHsaAttributed.setNewToBrandSalesPercentage(item.getBigDecimal("newToBrandSalesPercentage"));

// 新品牌相关-销售单位
					amzAdvReportAdsHsaAttributed.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportAdsHsaAttributed.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));
					amzAdvReportAdsHsaAttributed.setNewToBrandUnitsSoldPercentage(item.getBigDecimal("newToBrandUnitsSoldPercentage"));

// 购买相关字段
					amzAdvReportAdsHsaAttributed.setPurchases(item.getInteger("purchases"));
					amzAdvReportAdsHsaAttributed.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportAdsHsaAttributed.setPurchasesPromoted(item.getInteger("purchasesPromoted"));

// 销售相关字段
					amzAdvReportAdsHsaAttributed.setSales(item.getBigDecimal("sales"));
					amzAdvReportAdsHsaAttributed.setSalesClicks(item.getInteger("salesClicks"));
					amzAdvReportAdsHsaAttributed.setSalesPromoted(item.getBigDecimal("salesPromoted"));

// 销售单位相关字段
					amzAdvReportAdsHsaAttributed.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportAdsHsaAttributed.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));

// 视频相关字段
					amzAdvReportAdsHsaAttributed.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
					amzAdvReportAdsHsaAttributed.setVideo5SecondViews(item.getInteger("video5SecondViews"));
					amzAdvReportAdsHsaAttributed.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportAdsHsaAttributed.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportAdsHsaAttributed.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportAdsHsaAttributed.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportAdsHsaAttributed.setVideoUnmutes(item.getInteger("videoUnmutes"));

// 可视性相关字段
					amzAdvReportAdsHsaAttributed.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportAdsHsaAttributed.setViewableImpressions(item.getInteger("viewableImpressions"));

// 操作时间
					amzAdvReportAdsHsaAttributed.setOpttime(new Date());
					if(!amzAdvReportAdsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportAdsHsaAttributed);
	            	}
			 
                	amzAdvReportHasVideo=	new AmzAdvReportAdsHsaVideo();
                	amzAdvReportHasVideo.setAdid(amzAdvReportHas.getAdid());
                	amzAdvReportHasVideo.setBydate(amzAdvReportHas.getBydate());
                	amzAdvReportHasVideo.setOpttime(amzAdvReportHas.getOpttime());
                	amzAdvReportHasVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
                	amzAdvReportHasVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
                	amzAdvReportHasVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
                	amzAdvReportHasVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
                	amzAdvReportHasVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
                	amzAdvReportHasVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
                	amzAdvReportHasVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
                	amzAdvReportHasVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
                	amzAdvReportHasVideo.setVtr(item.getBigDecimal("vtr"));
                	amzAdvReportHasVideo.setVctr(item.getBigDecimal("vctr"));
                	if(!amzAdvReportHasVideo.iszero()) {
                		listVideo.add(amzAdvReportHasVideo);
                	}
 
                	
                	amzAdvReportHasBrand=	new AmzAdvReportAdsHsaBrand();
                	amzAdvReportHasBrand.setAdid(amzAdvReportHas.getAdid());
                	amzAdvReportHasBrand.setBydate(amzAdvReportHas.getBydate());
                	amzAdvReportHasBrand.setOpttime(amzAdvReportHas.getOpttime());
                	amzAdvReportHasBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
                	amzAdvReportHasBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
                	amzAdvReportHasBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
                	amzAdvReportHasBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
                	amzAdvReportHasBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
                	amzAdvReportHasBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
                	amzAdvReportHasBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
                	amzAdvReportHasBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
                	amzAdvReportHasBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
                	amzAdvReportHasBrand.setDpv14d(item.getInteger("dpv14d"));
                	if(!amzAdvReportHasBrand.iszero()) {
                		listBrand.add(amzAdvReportHasBrand);
                	}
					
					list.add(amzAdvReportHas);
					clicks = clicks + amzAdvReportHas.getClicks();
					cost = cost.add(amzAdvReportHas.getCost());
					
					if (currency == null && StringUtil.isNotEmpty(profile.getCurrencycode())) {
						currency = profile.getCurrencycode();
					}
					impressions = impressions + amzAdvReportHas.getImpressions();
					if(amzAdvReportAdsHsaAttributed.getSales() != null) {
						attributedsales = attributedsales.add(amzAdvReportAdsHsaAttributed.getSales());
					}
					if(amzAdvReportAdsHsaAttributed.getSales() != null) {
						attributedunitsordered = attributedunitsordered + 
								(amzAdvReportAdsHsaAttributed.getSales()!=null?amzAdvReportAdsHsaAttributed.getSales().intValue():0);
					}
					if (amzAdvReportAdsHsaAttributed.getPurchases() != null) {
						attributedConversions = attributedConversions +
								(amzAdvReportAdsHsaAttributed.getPurchases()!=null?amzAdvReportAdsHsaAttributed.getPurchases().intValue():0);
					}
					if (list.size() >= 2000) {
						amzAdvReportAdsHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportAdsHsaMapper.insertBatchAttributed(listAttributed);
						listBrand.clear();
					}
					if (listVideo.size() >= 2000) {
						amzAdvReportAdsHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportAdsHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
				
				}
				if (list.size() > 0) {
					amzAdvReportAdsHsaMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportAdsHsaMapper.insertBatchAttributed(listAttributed);
				}
				if (listVideo.size() > 0) {
					amzAdvReportAdsHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportAdsHsaMapper.insertBatchBrand(listBrand);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
