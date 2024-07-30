package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sb.dao.AmzAdvAdsHsaMapper;
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportAdsHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvAdsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdsHsa;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdsHsaAttributed;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdsHsaBrand;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportAdsHsaVideo;
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
			final List<AmzAdvReportAdsHsaAttributed> listAttributed = new LinkedList<AmzAdvReportAdsHsaAttributed>();
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
					AmzAdvReportAdsHsaAttributed amzAdvReportAdgroupsHsaAttributed=new AmzAdvReportAdsHsaAttributed();
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

					amzAdvReportAdgroupsHsaAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportAdgroupsHsaAttributed.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportAdgroupsHsaAttributed.setAdid(amzAdvReportHas.getAdid());
					amzAdvReportAdgroupsHsaAttributed.setBydate(amzAdvReportHas.getBydate());
					
					if(!amzAdvReportAdgroupsHsaAttributed.isZero()) {
						listAttributed.add(amzAdvReportAdgroupsHsaAttributed);
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
					if(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d() != null) {
						attributedsales = attributedsales.add(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d());
					}
					if(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d() != null) {
						attributedunitsordered = attributedunitsordered + 
								(amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d()!=null?amzAdvReportAdgroupsHsaAttributed.getAttributedsales14d().intValue():0);
					}
					if (amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d() != null) {
						attributedConversions = attributedConversions +
								(amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d()!=null?amzAdvReportAdgroupsHsaAttributed.getAttributedconversions14d().intValue():0);
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
