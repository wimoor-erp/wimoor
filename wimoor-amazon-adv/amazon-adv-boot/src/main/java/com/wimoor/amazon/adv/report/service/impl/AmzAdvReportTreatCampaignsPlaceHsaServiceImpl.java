package com.wimoor.amazon.adv.report.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportPlacementService;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportCampaignsPlaceHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportCampaignsPlaceHsaVideo;
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
			final List<AmzAdvReportCampaignsPlaceHsaAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsPlaceHsaAttributed>();
			final List<AmzAdvReportCampaignsPlaceHsaBrand> listBrand = new LinkedList<AmzAdvReportCampaignsPlaceHsaBrand>();
			final List<AmzAdvReportCampaignsPlaceHsaVideo> listVideo = new LinkedList<AmzAdvReportCampaignsPlaceHsaVideo>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportCampaignsPlaceHsa amzAdvReportHasCampaignPlace = new AmzAdvReportCampaignsPlaceHsa();
					AmzAdvReportCampaignsPlaceHsaAttributed amzAdvReportCampaignsPlaceHsaAttributed=new AmzAdvReportCampaignsPlaceHsaAttributed();
					AmzAdvReportCampaignsPlaceHsaBrand amzAdvReportCampaignsPlaceHsaBrand=null;
					AmzAdvReportCampaignsPlaceHsaVideo amzAdvReportCampaignsPlaceHsaVideo=null;
					amzAdvReportHasCampaignPlace.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportHasCampaignPlace.setCampaignbudget(item.getBigDecimal("campaignBudget"));
					String placement = item.getString("placement");
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

					amzAdvReportCampaignsPlaceHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportCampaignsPlaceHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportCampaignsPlaceHsaAttributed.setPlacementid(objplacement.getId());
					amzAdvReportCampaignsPlaceHsaAttributed.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
					amzAdvReportCampaignsPlaceHsaAttributed.setBydate(amzAdvReportHasCampaignPlace.getBydate());
					amzAdvReportCampaignsPlaceHsaAttributed.setOpttime(new Date());
					
					if(!amzAdvReportCampaignsPlaceHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportCampaignsPlaceHsaAttributed);
					}
					
			        if(request.getCreativeType()!=null&&"video".equals(request.getCreativeType())) {
			        	amzAdvReportCampaignsPlaceHsaVideo=	new AmzAdvReportCampaignsPlaceHsaVideo();
			        	amzAdvReportCampaignsPlaceHsaVideo.setPlacementid(objplacement.getId());
			        	amzAdvReportCampaignsPlaceHsaVideo.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
			        	amzAdvReportCampaignsPlaceHsaVideo.setBydate(amzAdvReportHasCampaignPlace.getBydate());
			        	amzAdvReportCampaignsPlaceHsaVideo.setOpttime(new Date());
			        	amzAdvReportCampaignsPlaceHsaVideo.setViewableImpressions(item.getInteger("viewableImpressions"));
			        	amzAdvReportCampaignsPlaceHsaVideo.setVideoFirstQuartileViews(item.getInteger("videoFirstQuartileViews"));
			        	amzAdvReportCampaignsPlaceHsaVideo.setVideoMidpointViews(item.getInteger("videoMidpointViews"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVideoThirdQuartileViews(item.getInteger("videoThirdQuartileViews"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVideoCompleteViews(item.getInteger("videoCompleteViews"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVideo5SecondViews(item.getInteger("video5SecondViews"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVideo5SecondViewRate(item.getBigDecimal("video5SecondViewRate"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVideoUnmutes(item.getInteger("videoUnmutes"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVtr(item.getBigDecimal("vtr"));
	                	amzAdvReportCampaignsPlaceHsaVideo.setVctr(item.getBigDecimal("vctr"));
	                	if(!amzAdvReportCampaignsPlaceHsaVideo.iszero()) {
	                		listVideo.add(amzAdvReportCampaignsPlaceHsaVideo);
	                	}
	                }else {
	                	amzAdvReportCampaignsPlaceHsaBrand=	new AmzAdvReportCampaignsPlaceHsaBrand();
	                	amzAdvReportCampaignsPlaceHsaBrand.setPlacementid(objplacement.getId());
	                	amzAdvReportCampaignsPlaceHsaBrand.setCampaignid(amzAdvReportHasCampaignPlace.getCampaignid());
	                	amzAdvReportCampaignsPlaceHsaBrand.setBydate(amzAdvReportHasCampaignPlace.getBydate());
	                	amzAdvReportCampaignsPlaceHsaBrand.setOpttime(new Date());
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedDetailPageViewsClicks14d(item.getInteger("attributedDetailPageViewsClicks14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrdersNewToBrandPercentage14d(item.getBigDecimal("attributedOrdersNewToBrandPercentage14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedOrderRateNewToBrand14d(item.getInteger("attributedOrderRateNewToBrand14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedSalesNewToBrand14d(item.getInteger("attributedSalesNewToBrand14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedSalesNewToBrandPercentage14d(item.getBigDecimal("attributedSalesNewToBrandPercentage14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setAttributedUnitsOrderedNewToBrandPercentage14d(item.getBigDecimal("attributedUnitsOrderedNewToBrandPercentage14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setUnitsSold14d(item.getInteger("unitsSold14d"));
	                	amzAdvReportCampaignsPlaceHsaBrand.setDpv14d(item.getInteger("dpv14d"));
	                	if(!amzAdvReportCampaignsPlaceHsaBrand.iszero()) {
	                		listBrand.add(amzAdvReportCampaignsPlaceHsaBrand);
	                	}
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
					if (listVideo.size() >= 2000) {
						amzAdvReportCampaignsPlaceHsaMapper.insertBatchVideo(listVideo);
						listVideo.clear();
					}
					if (listBrand.size() >= 2000) {
						amzAdvReportCampaignsPlaceHsaMapper.insertBatchBrand(listBrand);
						listBrand.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchAttributed(listAttributed);
				}
				if (listVideo.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchVideo(listVideo);
				}
				if (listBrand.size() > 0) {
					amzAdvReportCampaignsPlaceHsaMapper.insertBatchBrand(listBrand);
				}
			} catch(Exception e){
				e.printStackTrace();
			} 
		}


	 
}
