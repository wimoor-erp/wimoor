package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.wimoor.amazon.adv.sb.pojo.*;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.dao.AmzAdvReportRequestTypeMapper;
import com.wimoor.amazon.adv.report.pojo.AmzAdvReportRequestType;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumProductAds;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvCampaignsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsHsaMapper;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatCampaignsHsaService")
public class AmzAdvReportTreatCampaignsHsaServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportCampaignsHsaMapper amzAdvReportCampaignsHsaMapper;
	@Resource 
	AmzAdvCampaignsHsaMapper amzAdvCampaignsHsaMapper;
	@Resource
	AmzAdvReportRequestTypeMapper amzAdvReportRequestTypeMapper;
	 public void runRequestReportV2(AmzAdvProfile profile,JSONObject param,String url,Date startDate,Date endDate,AmzAdvReportRequestType type,List<AmzAdvRequest> list) {
			String metrics=param.get("metrics").toString();
			double distday = GeneralUtil.distanceOfDay(startDate, new Date());
			String log="";
			if(type.getId()==222&&distday>=2&&distday<3) {
				metrics=metrics+",campaignBudgetType,campaignName,campaignId,campaignStatus";
				param.put("metrics",metrics);
				log="hasName";
			}			
			   String response  = apiBuildService.amzAdvPost(profile, url, param.toJSONString());
			if (StringUtil.isNotEmpty(response)) {
				JSONObject item = GeneralUtil.getJsonObject(response);
				String reportId = item.getString("reportId");
				String recordType = item.getString("recordType")!=null?item.getString("recordType"):type.getReponsetype();
				String status = item.getString("status");
				String statusDetails = item.getString("statusDetails");
				AmzAdvRequest record = new AmzAdvRequest();
				record.setReportid(reportId);
				record.setRecordtype(recordType);
				record.setSegment(type.getSegment());
				record.setCampaigntype(type.getCampaigntype());
				record.setStatus(status);
				record.setStatusdetails(statusDetails);
				record.setProfileid(profile.getId());
				record.setCreativeType(type.getActivetype());
				record.setRequesttime(new Date());
				record.setOpttime(new Date());
				record.setStartDate(startDate);
				record.setEndDate(endDate);
				record.setReportType(type.getId());
				record.setIsrun(false);
				record.setLog(log);
				if(list!=null&&list.size()>0) {
					 for(AmzAdvRequest  req:list) {
			    		   amzAdvRequestMapper.delete(req);
			        }
				}
				
				amzAdvRequestMapper.insert(record);
				Map<String, Object> myparam = new HashMap<String, Object>();
				myparam.put("amzsnap", record);
				myparam.put("advProfile", profile);
			}	

}
	 @Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		 AmzAdvReportRequestType type = amzAdvReportRequestTypeMapper.selectByPrimaryKey(request.getReportType());
//		 if(type.getMetrics().contains("reportTypeId"))
			 treatReportV3(profile,request,jsonReader);

	 }
	
//	public void treatReportV2(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
//		// TODO Auto-generated method stub
//			final List<AmzAdvReportCampaignsHsa> list = new LinkedList<AmzAdvReportCampaignsHsa>();
//			final List<AmzAdvReportCampaignsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsHsaAttributed>();
//			final List<AmzAdvReportCampaignsHsaBrand> listBrand = new LinkedList<AmzAdvReportCampaignsHsaBrand>();
//			final List<AmzAdvReportCampaignsHsaVideo> listVideo = new LinkedList<AmzAdvReportCampaignsHsaVideo>();
//			try {
//				jsonReader.startArray();
//				String currency = "";
//				Map<String,AmzAdvSumProductAds> mapsum=new HashMap<String,AmzAdvSumProductAds>();
//				while (jsonReader.hasNext()) {
//					String elem = jsonReader.readString();
//					JSONObject item = GeneralUtil.getJsonObject(elem);
//					AmzAdvReportCampaignsHsa amzAdvReportHasCampaign = new AmzAdvReportCampaignsHsa();
//					AmzAdvReportCampaignsHsaAttributed amzAdvReportCampaignsHsaAttributed=new AmzAdvReportCampaignsHsaAttributed();
//					AmzAdvReportCampaignsHsaVideo amzAdvReportHasCampaignVideo = null;
//					AmzAdvReportCampaignsHsaBrand amzAdvReportHasCampaignBrand = null;
//					amzAdvReportHasCampaign.setCampaignid(item.getBigInteger("campaignId"));
//					amzAdvReportHasCampaign.setCampaignbudget(item.getBigDecimal("campaignBudget"));
//	               if(request.getLog()!=null&&request.getLog().equals("hasName")) {
//	            	   AmzAdvCampaignsHsa campaign = amzAdvCampaignsHsaMapper.selectByPrimaryKey(amzAdvReportHasCampaign.getCampaignid());
//	            	   if(campaign==null) {
//	            		   campaign= new AmzAdvCampaignsHsa();
//	            		   campaign.setProfileid(request.getProfileid());
//	            		   campaign.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
//	            		   campaign.setName(item.getString("campaignName"));
//	            		   campaign.setState(item.getString("campaignStatus"));
//	            		   campaign.setBudget(item.getBigDecimal("campaignBudget"));
//	            		   campaign.setBudgetType(item.getString("campaignBudgetType"));
//	            		   if(StrUtil.isNotBlank(campaign.getName())) {
//	            			   amzAdvCampaignsHsaMapper.insert(campaign);
//	            		   }
//	            	   }
//	               }
//					Date date = item.getDate("date");
//					if(date==null) {
//						date=request.getStartDate();
//					}
//					amzAdvReportHasCampaign.setBydate(date);
//					amzAdvReportHasCampaign.setProfileid(request.getProfileid());
//					amzAdvReportHasCampaign.setOpttime(new Date());
//					amzAdvReportHasCampaign.setClicks(item.getInteger("clicks"));
//					amzAdvReportHasCampaign.setImpressions(item.getInteger("impressions"));
//					amzAdvReportHasCampaign.setCost(item.getBigDecimal("cost"));
//
//					amzAdvReportCampaignsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
//					amzAdvReportCampaignsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
//					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
//					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
//					amzAdvReportCampaignsHsaAttributed.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
//					amzAdvReportCampaignsHsaAttributed.setBydate(amzAdvReportHasCampaign.getBydate());
//
//					if(!amzAdvReportCampaignsHsaAttributed.isZero()) {
//						listAttributed.add(amzAdvReportCampaignsHsaAttributed);
//	            	}
//
//	                if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
//	                	amzAdvReportHasCampaignVideo=	new AmzAdvReportCampaignsHsaVideo();
//	                	amzAdvReportHasCampaignVideo.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
//	                	amzAdvReportHasCampaignVideo.setBydate(amzAdvReportHasCampaign.getBydate());
//	                	amzAdvReportHasCampaignVideo.setOpttime(amzAdvReportHasCampaign.getOpttime());
//	                	amzAdvReportHasCampaignVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
//	                	amzAdvReportHasCampaignVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
//	                	amzAdvReportHasCampaignVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
//	                	amzAdvReportHasCampaignVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
//	                	amzAdvReportHasCampaignVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
//	                	amzAdvReportHasCampaignVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
//	                	amzAdvReportHasCampaignVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
//	                	amzAdvReportHasCampaignVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
//	                	amzAdvReportHasCampaignVideo.setVtr(item.getBigDecimal("vtr"));
//	                	amzAdvReportHasCampaignVideo.setVctr(item.getBigDecimal("vctr"));
//	                	if(!amzAdvReportHasCampaignVideo.iszero()) {
//	                		listVideo.add(amzAdvReportHasCampaignVideo);
//	                	}
//	                }else {
//	                	amzAdvReportHasCampaignBrand=	new AmzAdvReportCampaignsHsaBrand();
//	                	amzAdvReportHasCampaignBrand.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
//	                	amzAdvReportHasCampaignBrand.setBydate(amzAdvReportHasCampaign.getBydate());
//	                	amzAdvReportHasCampaignBrand.setOpttime(amzAdvReportHasCampaign.getOpttime());
//	                	amzAdvReportHasCampaignBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
//	                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
//	                	amzAdvReportHasCampaignBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
//	                	amzAdvReportHasCampaignBrand.setDpv14d(item.getInteger("dpv14d"));
//	                	if(!amzAdvReportHasCampaignBrand.iszero()) {
//	                		listBrand.add(amzAdvReportHasCampaignBrand);
//	                	}
//	                }
//
//
//					list.add(amzAdvReportHasCampaign);
//
//					if (currency == null && StringUtil.isNotEmpty(profile.getCurrencycode())) {
//						currency = profile.getCurrencycode();
//					}
//
//					if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
//						addSummary( request,mapsum, date,"sv",amzAdvReportHasCampaign.getClicks(),
//							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
//							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
//							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
//							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
//		            }else {
//		            	addSummary( request,mapsum, date,"sb",amzAdvReportHasCampaign.getClicks(),
//							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
//							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
//							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
//							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
//		            }
//
//
//					if (list.size() >= 2000) {
//						amzAdvReportCampaignsHsaMapper.insertBatch(list);
//						list.clear();
//					}
//					if (listAttributed.size() >= 2000) {
//						//amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
//						listBrand.clear();
//					}
//					if (listVideo.size() >= 2000) {
//						amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
//						listVideo.clear();
//					}
//					if (listBrand.size() >= 2000) {
//						amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
//						listBrand.clear();
//					}
//
//				}
//				if (list.size() > 0) {
//					amzAdvReportCampaignsHsaMapper.insertBatch(list);
//				}
//				if (listAttributed.size() > 0) {
//					amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
//				}
//				if (listVideo.size() > 0) {
//					amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
//				}
//				if (listBrand.size() > 0) {
//					amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
//				}
//				for(Entry<String, AmzAdvSumProductAds> entry:mapsum.entrySet()) {
//					AmzAdvSumProductAds record=entry.getValue();
//					AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
//					if (old != null) {
//						amzAdvSumProductAdsMapper.updateByKey(record);
//					} else {
//						amzAdvSumProductAdsMapper.insert(record);
//					}
//				}
//			}catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
	public void treatReportV3(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportCampaignsHsa> list = new LinkedList<AmzAdvReportCampaignsHsa>();
			final List<AmzAdvReportCampaignsHsaAttributedAll> listAttributed = new LinkedList<AmzAdvReportCampaignsHsaAttributedAll>();
			final List<AmzAdvReportCampaignsHsaBrand> listBrand = new LinkedList<AmzAdvReportCampaignsHsaBrand>();
			final List<AmzAdvReportCampaignsHsaVideo> listVideo = new LinkedList<AmzAdvReportCampaignsHsaVideo>();
			try {
				jsonReader.startArray();
				String currency = "";
				Map<String,AmzAdvSumProductAds> mapsum=new HashMap<String,AmzAdvSumProductAds>();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportCampaignsHsa amzAdvReportHasCampaign = new AmzAdvReportCampaignsHsa();
					AmzAdvReportCampaignsHsaAttributedAll amzAdvReportCampaignsHsaAttributedall=new AmzAdvReportCampaignsHsaAttributedAll();
					AmzAdvReportCampaignsHsaVideo amzAdvReportHasCampaignVideo = null;
					AmzAdvReportCampaignsHsaBrand amzAdvReportHasCampaignBrand = null;
					amzAdvReportHasCampaign.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportHasCampaign.setCampaignbudget(item.getBigDecimal("campaignBudget"));
	               if(request.getLog()!=null&&request.getLog().equals("hasName")) {
	            	   AmzAdvCampaignsHsa campaign = amzAdvCampaignsHsaMapper.selectByPrimaryKey(amzAdvReportHasCampaign.getCampaignid());
	            	   if(campaign==null) {
	            		   campaign= new AmzAdvCampaignsHsa();
	            		   campaign.setProfileid(request.getProfileid());
	            		   campaign.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
	            		   campaign.setName(item.getString("campaignName"));
	            		   campaign.setState(item.getString("campaignStatus"));
	            		   campaign.setBudget(item.getBigDecimal("campaignBudget"));
	            		   campaign.setBudgetType(item.getString("campaignBudgetType"));
	            		   if(StrUtil.isNotBlank(campaign.getName())) {
	            			   amzAdvCampaignsHsaMapper.insert(campaign);
	            		   }
	            	   }
	               }
					Date date = item.getDate("date");
					if(date==null) {
						date=request.getStartDate();
					}
					amzAdvReportHasCampaign.setBydate(date);
					amzAdvReportHasCampaign.setProfileid(request.getProfileid());
					amzAdvReportHasCampaign.setOpttime(new Date());
					amzAdvReportHasCampaign.setClicks(item.getInteger("clicks"));
					amzAdvReportHasCampaign.setImpressions(item.getInteger("impressions"));
					amzAdvReportHasCampaign.setCost(item.getBigDecimal("cost"));

					amzAdvReportCampaignsHsaAttributedall.setCampaignId(amzAdvReportHasCampaign.getCampaignid().toString());
					amzAdvReportCampaignsHsaAttributedall.setBydate(amzAdvReportHasCampaign.getBydate());

					// 购物车相关字段
					amzAdvReportCampaignsHsaAttributedall.setAddToCart(item.getInteger("addToCart"));
					amzAdvReportCampaignsHsaAttributedall.setAddToCartClicks(item.getInteger("addToCartClicks"));
					amzAdvReportCampaignsHsaAttributedall.setAddToCartRate(item.getBigDecimal("addToCartRate"));
					amzAdvReportCampaignsHsaAttributedall.setAddToList(item.getInteger("addToList"));
					amzAdvReportCampaignsHsaAttributedall.setAddToListFromClicks(item.getInteger("addToListFromClicks"));

// 借阅相关字段
					amzAdvReportCampaignsHsaAttributedall.setQualifiedBorrows(item.getInteger("qualifiedBorrows"));
					amzAdvReportCampaignsHsaAttributedall.setQualifiedBorrowsFromClicks(item.getInteger("qualifiedBorrowsFromClicks"));
					amzAdvReportCampaignsHsaAttributedall.setRoyaltyQualifiedBorrows(item.getInteger("royaltyQualifiedBorrows"));
					amzAdvReportCampaignsHsaAttributedall.setRoyaltyQualifiedBorrowsFromClicks(item.getInteger("royaltyQualifiedBorrowsFromClicks"));

// 品牌搜索相关字段
					amzAdvReportCampaignsHsaAttributedall.setBrandedSearches(item.getInteger("brandedSearches"));
					amzAdvReportCampaignsHsaAttributedall.setBrandedSearchesClicks(item.getInteger("brandedSearchesClicks"));

// 预算相关字段
					amzAdvReportCampaignsHsaAttributedall.setCampaignBudgetAmount(item.getBigDecimal("campaignBudgetAmount"));
					amzAdvReportCampaignsHsaAttributedall.setCampaignBudgetCurrencyCode(item.getString("campaignBudgetCurrencyCode"));

// 详情页相关字段
					amzAdvReportCampaignsHsaAttributedall.setDetailPageViews(item.getInteger("detailPageViews"));
					amzAdvReportCampaignsHsaAttributedall.setDetailPageViewsClicks(item.getInteger("detailPageViewsClicks"));
					amzAdvReportCampaignsHsaAttributedall.setECPAddToCart(item.getBigDecimal("eCPAddToCart"));

// 新品牌相关-详情页
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandDetailPageViewRate(item.getBigDecimal("newToBrandDetailPageViewRate"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandDetailPageViews(item.getInteger("newToBrandDetailPageViews"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandDetailPageViewsClicks(item.getInteger("newToBrandDetailPageViewsClicks"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandECPDetailPageView(item.getBigDecimal("newToBrandECPDetailPageView"));

// 新品牌相关-购买
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandPurchases(item.getInteger("newToBrandPurchases"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandPurchasesClicks(item.getInteger("newToBrandPurchasesClicks"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandPurchasesPercentage(item.getBigDecimal("newToBrandPurchasesPercentage"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandPurchasesRate(item.getBigDecimal("newToBrandPurchasesRate"));

// 新品牌相关-销售
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandSales(item.getBigDecimal("newToBrandSales"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandSalesClicks(item.getInteger("newToBrandSalesClicks"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandSalesPercentage(item.getBigDecimal("newToBrandSalesPercentage"));

// 新品牌相关-销售单位
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandUnitsSold(item.getInteger("newToBrandUnitsSold"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandUnitsSoldClicks(item.getInteger("newToBrandUnitsSoldClicks"));
					amzAdvReportCampaignsHsaAttributedall.setNewToBrandUnitsSoldPercentage(item.getBigDecimal("newToBrandUnitsSoldPercentage"));

// 购买相关字段
					amzAdvReportCampaignsHsaAttributedall.setPurchases(item.getInteger("purchases"));
					amzAdvReportCampaignsHsaAttributedall.setPurchasesClicks(item.getInteger("purchasesClicks"));
					amzAdvReportCampaignsHsaAttributedall.setPurchasesPromoted(item.getInteger("purchasesPromoted"));

// 销售相关字段
					amzAdvReportCampaignsHsaAttributedall.setSales(item.getBigDecimal("sales"));
					amzAdvReportCampaignsHsaAttributedall.setSalesClicks(item.getInteger("salesClicks"));
					amzAdvReportCampaignsHsaAttributedall.setSalesPromoted(item.getBigDecimal("salesPromoted"));

// 搜索展示份额
					amzAdvReportCampaignsHsaAttributedall.setTopOfSearchImpressionShare(item.getBigDecimal("topOfSearchImpressionShare"));

// 销售单位相关字段
					amzAdvReportCampaignsHsaAttributedall.setUnitsSold(item.getInteger("unitsSold"));
					amzAdvReportCampaignsHsaAttributedall.setUnitsSoldClicks(item.getInteger("unitsSoldClicks"));

// 视频相关字段
					amzAdvReportCampaignsHsaAttributedall.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
					amzAdvReportCampaignsHsaAttributedall.setVideo5SecondViews(item.getInteger("video5SecondViews"));
					amzAdvReportCampaignsHsaAttributedall.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
					amzAdvReportCampaignsHsaAttributedall.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
					amzAdvReportCampaignsHsaAttributedall.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
					amzAdvReportCampaignsHsaAttributedall.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
					amzAdvReportCampaignsHsaAttributedall.setVideoUnmutes(item.getInteger("videoUnmutes"));

// 可视性相关字段
					amzAdvReportCampaignsHsaAttributedall.setViewabilityRate(item.getBigDecimal("viewabilityRate"));
					amzAdvReportCampaignsHsaAttributedall.setViewableImpressions(item.getInteger("viewableImpressions"));
					amzAdvReportCampaignsHsaAttributedall.setViewClickThroughRate(item.getBigDecimal("viewClickThroughRate"));

// 操作时间
					amzAdvReportCampaignsHsaAttributedall.setOipttime(new Date());

					
					if(!amzAdvReportCampaignsHsaAttributedall.isZero()) {
						listAttributed.add(amzAdvReportCampaignsHsaAttributedall);
	            	}
					
	                if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
	                	amzAdvReportHasCampaignVideo=	new AmzAdvReportCampaignsHsaVideo();
	                	amzAdvReportHasCampaignVideo.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
	                	amzAdvReportHasCampaignVideo.setBydate(amzAdvReportHasCampaign.getBydate());
	                	amzAdvReportHasCampaignVideo.setOpttime(amzAdvReportHasCampaign.getOpttime());
	                	amzAdvReportHasCampaignVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
	                	amzAdvReportHasCampaignVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
	                	amzAdvReportHasCampaignVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
	                	amzAdvReportHasCampaignVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
	                	amzAdvReportHasCampaignVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
	                	amzAdvReportHasCampaignVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
	                	amzAdvReportHasCampaignVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
	                	amzAdvReportHasCampaignVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
	                	amzAdvReportHasCampaignVideo.setVtr(item.getBigDecimal("vtr"));
	                	amzAdvReportHasCampaignVideo.setVctr(item.getBigDecimal("vctr"));
	                	if(!amzAdvReportHasCampaignVideo.iszero()) {
	                		listVideo.add(amzAdvReportHasCampaignVideo);
	                	}
	                }else {
	                	amzAdvReportHasCampaignBrand=	new AmzAdvReportCampaignsHsaBrand();
	                	amzAdvReportHasCampaignBrand.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
	                	amzAdvReportHasCampaignBrand.setBydate(amzAdvReportHasCampaign.getBydate());
	                	amzAdvReportHasCampaignBrand.setOpttime(amzAdvReportHasCampaign.getOpttime());
	                	amzAdvReportHasCampaignBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
	                	amzAdvReportHasCampaignBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
	                	amzAdvReportHasCampaignBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
	                	amzAdvReportHasCampaignBrand.setDpv14d(item.getInteger("dpv14d"));
	                	if(!amzAdvReportHasCampaignBrand.iszero()) {
	                		listBrand.add(amzAdvReportHasCampaignBrand);
	                	}
	                }
					
					
					list.add(amzAdvReportHasCampaign);
					 
					if (currency == null && StringUtil.isNotEmpty(profile.getCurrencycode())) {
						currency = profile.getCurrencycode();
					}
				 
					if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
						addSummary( request,mapsum, date,"sv",amzAdvReportHasCampaign.getClicks(),
							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
							     amzAdvReportCampaignsHsaAttributedall.getSales(),
								amzAdvReportCampaignsHsaAttributedall.getSales()!=null?amzAdvReportCampaignsHsaAttributedall.getSales().intValue():0,
								amzAdvReportCampaignsHsaAttributedall.getPurchases());
		            }else {
		            	addSummary( request,mapsum, date,"sb",amzAdvReportHasCampaign.getClicks(),
							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
								amzAdvReportCampaignsHsaAttributedall.getSales(),
								amzAdvReportCampaignsHsaAttributedall.getSales()!=null?amzAdvReportCampaignsHsaAttributedall.getSales().intValue():0,
								amzAdvReportCampaignsHsaAttributedall.getPurchases());
		            }
					
					
					if (list.size() >= 2000) {
						amzAdvReportCampaignsHsaMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
						listBrand.clear();
					}
					if (listVideo.size() >= 2000) {
						amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
				
				}
				if (list.size() > 0) {
					amzAdvReportCampaignsHsaMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportCampaignsHsaMapper.insertBatchAttributed(listAttributed);
				}
				if (listVideo.size() > 0) {
					amzAdvReportCampaignsHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportCampaignsHsaMapper.insertBatchBrand(listBrand);
				}
				for(Entry<String, AmzAdvSumProductAds> entry:mapsum.entrySet()) {
					AmzAdvSumProductAds record=entry.getValue();
					AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
					if (old != null) {
						amzAdvSumProductAdsMapper.updateByKey(record);
					} else {
						amzAdvSumProductAdsMapper.insert(record);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}
	 
}
