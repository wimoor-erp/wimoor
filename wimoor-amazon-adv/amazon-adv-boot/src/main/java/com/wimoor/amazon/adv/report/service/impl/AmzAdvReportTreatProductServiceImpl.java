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
import com.wimoor.amazon.adv.common.service.IAmazonReportAdvSummaryService;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductAdsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAds;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductAdsAttributedSame;
import com.wimoor.common.GeneralUtil;

@Service("amzAdvReportTreatProductService")
public class AmzAdvReportTreatProductServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductAdsMapper amzAdvReportProductAdsMapper;
	@Resource
	protected IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	/*
	 {"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["advertiser"],
 "columns":["date","campaignId", "adGroupId", "adId", 
            "impressions", "clicks", "cost", "spend",
            "purchases1d", "purchases7d", "purchases14d", "purchases30d", 
            "purchasesSameSku1d", "purchasesSameSku7d", "purchasesSameSku14d", "purchasesSameSku30d", 
            "unitsSoldClicks1d", "unitsSoldClicks7d", "unitsSoldClicks14d", "unitsSoldClicks30d", 
            "sales1d", "sales7d", "sales14d", "sales30d", 
            "attributedSalesSameSku1d", "attributedSalesSameSku7d", "attributedSalesSameSku14d", "attributedSalesSameSku30d", 
            "salesOtherSku7d", 
            "unitsSoldSameSku1d", "unitsSoldSameSku7d", "unitsSoldSameSku14d", "unitsSoldSameSku30d", "unitsSoldOtherSku7d", 
            "kindleEditionNormalizedPagesRead14d", "kindleEditionNormalizedPagesRoyalties14d", 
            "acosClicks7d", "acosClicks14d", "roasClicks7d", "roasClicks14d"],
 "reportTypeId":"spAdvertisedProduct",
 "timeUnit":"DAILY",
 "format":"GZIP_JSON"
 }
					 
 
	 */
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
		List<AmzAdvReportProductAds> list = new LinkedList<AmzAdvReportProductAds>();
		List<AmzAdvReportProductAdsAttributed> listAttributed = new LinkedList<AmzAdvReportProductAdsAttributed>();
		List<AmzAdvReportProductAdsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductAdsAttributedSame>();
		try {
			jsonReader.startArray();
			while (jsonReader.hasNext()) {
				String elem = jsonReader.readString();
				JSONObject item = GeneralUtil.getJsonObject(elem);
				AmzAdvReportProductAds padv = new AmzAdvReportProductAds();
				AmzAdvReportProductAdsAttributed padvattr = new AmzAdvReportProductAdsAttributed();
				AmzAdvReportProductAdsAttributedSame padvattrsame=new AmzAdvReportProductAdsAttributedSame();
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
				
				
				padvattr.setBydate(padv.getBydate());
				padvattr.setAdid(padv.getAdid());
				padvattr.setAttributedconversions1d(item.getInteger("purchases1d"));
				padvattr.setAttributedconversions7d(item.getInteger("purchases7d"));
				padvattr.setAttributedconversions14d(item.getInteger("purchases14d"));
				padvattr.setAttributedconversions30d(item.getInteger("purchases30d"));

				padvattr.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
				padvattr.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
				padvattr.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
				padvattr.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

				padvattr.setAttributedsales1d(item.getBigDecimal("sales1d"));
				padvattr.setAttributedsales7d(item.getBigDecimal("sales7d"));
				padvattr.setAttributedsales14d(item.getBigDecimal("sales14d"));
				padvattr.setAttributedsales30d(item.getBigDecimal("sales30d"));

				padvattrsame.setBydate(padv.getBydate());
				padvattrsame.setAdid(padv.getAdid());
				padvattrsame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
				padvattrsame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
				padvattrsame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
				padvattrsame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));
				
				padvattrsame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
				padvattrsame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
				padvattrsame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
				padvattrsame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
				
				padvattrsame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
				padvattrsame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
				padvattrsame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
				padvattrsame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
				
				if(!padvattr.isZero()) {
					listAttributed.add(padvattr);
				}
				if(!padvattrsame.isZero()) {
					listAttributedSame.add(padvattrsame);
				}
				if ((padv.getClicks() == null || padv.getClicks() == 0)
						&& (padv.getImpressions() == null || padv.getImpressions() == 0)
						&& (padv.getCost() == null || padv.getCost().compareTo(new BigDecimal("0")) == 0)
                      ) {
					continue;
				}
				 
		
				list.add(padv);

		 
				if (list.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatch(list);
					list.clear();
				}
				if (listAttributed.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if (listAttributedSame.size() >= 2000) {
					amzAdvReportProductAdsMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributedSame.clear();
				}
			}
			if (list.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatch(list);
			}
			if (listAttributed.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatchAttributed(listAttributed);
			}
			if (listAttributedSame.size() > 0) {
				amzAdvReportProductAdsMapper.insertBatchAttributedSame(listAttributedSame);
			}
		} catch(Exception e){
			e.printStackTrace();
		} 
	}

	 
}
