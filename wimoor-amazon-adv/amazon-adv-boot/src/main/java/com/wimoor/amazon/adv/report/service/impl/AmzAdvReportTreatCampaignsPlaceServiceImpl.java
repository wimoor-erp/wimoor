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
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportCompaignsPlaceMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlace;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportCompaignsPlaceAttributedSame;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportPlacement;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatCampaignsPlaceService")
public class AmzAdvReportTreatCampaignsPlaceServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportCompaignsPlaceMapper amzAdvReportCompaignsPlaceMapper;
	@Resource
	IAmzAdvReportPlacementService amzAdvReportPlacementService;

/*
{"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["campaign","campaignPlacement"],
 "columns":["campaignId","placementClassification",
    	    "impressions","clicks", "cost", "spend","date",
    	    "purchases1d", "purchases7d", "purchases14d", "purchases30d", 
    	    "purchasesSameSku1d", "purchasesSameSku7d", "purchasesSameSku14d", "purchasesSameSku30d", 
    	    "unitsSoldClicks1d", "unitsSoldClicks7d", "unitsSoldClicks14d", "unitsSoldClicks30d", 
    	    "sales1d", "sales7d", "sales14d", "sales30d", 
    	    "attributedSalesSameSku1d", "attributedSalesSameSku7d", "attributedSalesSameSku14d", "attributedSalesSameSku30d", 
    	    "unitsSoldSameSku1d", "unitsSoldSameSku7d", "unitsSoldSameSku14d", "unitsSoldSameSku30d", 
    	    "kindleEditionNormalizedPagesRead14d", "kindleEditionNormalizedPagesRoyalties14d"],
 "reportTypeId":"spCampaigns",
 "timeUnit":"DAILY",
 "format":"GZIP_JSON"
 }
*/
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			  List<AmzAdvReportCompaignsPlace> list = new LinkedList<AmzAdvReportCompaignsPlace>();
			  List<AmzAdvReportCompaignsPlaceAttributed> listAttributed = new LinkedList<AmzAdvReportCompaignsPlaceAttributed>();
			  List<AmzAdvReportCompaignsPlaceAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportCompaignsPlaceAttributedSame>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportCompaignsPlace amzAdvReportCompaigns = new AmzAdvReportCompaignsPlace();
					AmzAdvReportCompaignsPlaceAttributed amzAdvReportCompaignsAttributed = new AmzAdvReportCompaignsPlaceAttributed();
					AmzAdvReportCompaignsPlaceAttributedSame amzAdvReportCompaignsAttributedSame = new AmzAdvReportCompaignsPlaceAttributedSame();
					amzAdvReportCompaigns.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportCompaigns.setBydate(request.getStartDate());
					}else {
						amzAdvReportCompaigns.setBydate(date);
					}
					amzAdvReportCompaigns.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportCompaigns.setCampaignbudget(item.getBigDecimal("campaignBudget"));
					String placement = item.getString("placementClassification")!=null?item.getString("placementClassification"):item.getString("placement");
					if(placement==null){
						continue;
					}
					AmzAdvReportPlacement objplacement = amzAdvReportPlacementService.loadIDByPlacementName(placement); 
					amzAdvReportCompaigns.setPlacementid(objplacement.getId());
					amzAdvReportCompaigns.setClicks(item.getInteger("clicks"));
					amzAdvReportCompaigns.setImpressions(item.getInteger("impressions"));
					amzAdvReportCompaigns.setCost(item.getBigDecimal("cost"));
					amzAdvReportCompaigns.setOpttime(new Date());
					
					amzAdvReportCompaignsAttributed.setCampaignid(amzAdvReportCompaigns.getCampaignid());
					amzAdvReportCompaignsAttributed.setBydate(amzAdvReportCompaigns.getBydate());
					amzAdvReportCompaignsAttributed.setPlacementid(amzAdvReportCompaigns.getPlacementid());
					amzAdvReportCompaignsAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportCompaignsAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportCompaignsAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportCompaignsAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));


					amzAdvReportCompaignsAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportCompaignsAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportCompaignsAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportCompaignsAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

					amzAdvReportCompaignsAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportCompaignsAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportCompaignsAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportCompaignsAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));

					
					
					amzAdvReportCompaignsAttributedSame.setCampaignid(amzAdvReportCompaigns.getCampaignid());
					amzAdvReportCompaignsAttributedSame.setBydate(amzAdvReportCompaigns.getBydate());
					amzAdvReportCompaignsAttributedSame.setPlacementid(amzAdvReportCompaigns.getPlacementid());
					amzAdvReportCompaignsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportCompaignsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportCompaignsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportCompaignsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
					
					amzAdvReportCompaignsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportCompaignsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportCompaignsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportCompaignsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));

					amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportCompaignsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					
					if(!amzAdvReportCompaignsAttributed.isZero()) {
						listAttributed.add(amzAdvReportCompaignsAttributed);
					}
					if(!amzAdvReportCompaignsAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportCompaignsAttributedSame);
					}
					list.add(amzAdvReportCompaigns);
					if (list.size() >= 2000) {
						amzAdvReportCompaignsPlaceMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportCompaignsPlaceMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportCompaignsPlaceMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportCompaignsPlaceMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportCompaignsPlaceMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportCompaignsPlaceMapper.insertBatchAttributedSame(listAttributedSame);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	 
}
