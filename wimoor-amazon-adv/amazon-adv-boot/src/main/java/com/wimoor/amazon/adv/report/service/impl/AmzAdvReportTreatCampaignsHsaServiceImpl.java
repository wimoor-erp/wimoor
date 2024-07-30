package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

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
import com.wimoor.amazon.adv.sb.pojo.AmzAdvCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsHsaVideo;
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
		 if(type.getMetrics().contains("reportTypeId")) {
			 treatReportV3(profile,request,jsonReader);
		 }else {
			 treatReportV2(profile,request,jsonReader);
		 }
	 }
	
	public void treatReportV2(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportCampaignsHsa> list = new LinkedList<AmzAdvReportCampaignsHsa>();
			final List<AmzAdvReportCampaignsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsHsaAttributed>();
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
					AmzAdvReportCampaignsHsaAttributed amzAdvReportCampaignsHsaAttributed=new AmzAdvReportCampaignsHsaAttributed();
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

					amzAdvReportCampaignsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportCampaignsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportCampaignsHsaAttributed.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
					amzAdvReportCampaignsHsaAttributed.setBydate(amzAdvReportHasCampaign.getBydate());
					
					if(!amzAdvReportCampaignsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportCampaignsHsaAttributed);
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
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
		            }else {
		            	addSummary( request,mapsum, date,"sb",amzAdvReportHasCampaign.getClicks(),
							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
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
	public void treatReportV3(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportCampaignsHsa> list = new LinkedList<AmzAdvReportCampaignsHsa>();
			final List<AmzAdvReportCampaignsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsHsaAttributed>();
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
					AmzAdvReportCampaignsHsaAttributed amzAdvReportCampaignsHsaAttributed=new AmzAdvReportCampaignsHsaAttributed();
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

					amzAdvReportCampaignsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportCampaignsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportCampaignsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportCampaignsHsaAttributed.setCampaignid(amzAdvReportHasCampaign.getCampaignid());
					amzAdvReportCampaignsHsaAttributed.setBydate(amzAdvReportHasCampaign.getBydate());
					
					if(!amzAdvReportCampaignsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportCampaignsHsaAttributed);
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
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
		            }else {
		            	addSummary( request,mapsum, date,"sb",amzAdvReportHasCampaign.getClicks(),
							     currency,amzAdvReportHasCampaign.getImpressions(),amzAdvReportHasCampaign.getCost(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d(),
							     amzAdvReportCampaignsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportCampaignsHsaAttributed.getAttributedsales14d().intValue():0,
							     amzAdvReportCampaignsHsaAttributed.getAttributedconversions14d());
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
