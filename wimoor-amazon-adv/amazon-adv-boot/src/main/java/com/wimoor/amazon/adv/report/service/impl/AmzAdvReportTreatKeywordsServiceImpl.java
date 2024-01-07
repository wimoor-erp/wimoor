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
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywords;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsAttributedSame;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatKeywordsService")
public class AmzAdvReportTreatKeywordsServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportKeywordsMapper amzAdvReportKeywordsMapper;
	/*
	 {"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["targeting"],
 "columns":["date","campaignId", "adGroupId", "keywordId", 
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
 "filters":[
                {
                    "field": "keywordType",
                    "values": [
                    "BROAD",
                    "PHRASE",
                    "EXACT"
                    ]
                }
            ],
 "reportTypeId":"spTargeting",
 "timeUnit":"DAILY",
 "format":"GZIP_JSON"
 }
					 
 
	 */
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
			  List<AmzAdvReportKeywords> list = new LinkedList<AmzAdvReportKeywords>();
			  List<AmzAdvReportKeywordsAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsAttributed>();
			  List<AmzAdvReportKeywordsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportKeywordsAttributedSame>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportKeywords amzAdvReportKeyword = new AmzAdvReportKeywords();
					AmzAdvReportKeywordsAttributed amzAdvReportKeywordAttributed = new AmzAdvReportKeywordsAttributed();
					AmzAdvReportKeywordsAttributedSame amzAdvReportKeywordAttributedSame = new AmzAdvReportKeywordsAttributedSame();
					amzAdvReportKeyword.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportKeyword.setBydate(request.getStartDate());
					}else {
						amzAdvReportKeyword.setBydate(date);
					}
					amzAdvReportKeyword.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportKeyword.setKeywordid(item.getBigInteger("keywordId"));
					amzAdvReportKeyword.setAdgroupid(item.getBigInteger("adGroupId"));

					amzAdvReportKeyword.setClicks(item.getInteger("clicks"));
					amzAdvReportKeyword.setImpressions(item.getInteger("impressions"));
					amzAdvReportKeyword.setCost(item.getBigDecimal("cost"));
					
					amzAdvReportKeywordAttributed.setBydate(amzAdvReportKeyword.getBydate());
					amzAdvReportKeywordAttributed.setKeywordid(amzAdvReportKeyword.getKeywordid());
					amzAdvReportKeywordAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportKeywordAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportKeywordAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportKeywordAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));


					amzAdvReportKeywordAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportKeywordAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportKeywordAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportKeywordAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

					amzAdvReportKeywordAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportKeywordAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportKeywordAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportKeywordAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));

					
					amzAdvReportKeywordAttributedSame.setBydate(amzAdvReportKeyword.getBydate());
					amzAdvReportKeywordAttributedSame.setKeywordid(amzAdvReportKeyword.getKeywordid());
					amzAdvReportKeywordAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportKeywordAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportKeywordAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportKeywordAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
					
					amzAdvReportKeywordAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportKeywordAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportKeywordAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportKeywordAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));
					
					amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportKeywordAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					
					if(!amzAdvReportKeywordAttributed.isZero()) {
						listAttributed.add(amzAdvReportKeywordAttributed);
					}
					if(!amzAdvReportKeywordAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportKeywordAttributedSame);
					}
					if ((amzAdvReportKeyword.getClicks() == null || amzAdvReportKeyword.getClicks() == 0)
							&& (amzAdvReportKeyword.getImpressions() == null || amzAdvReportKeyword.getImpressions() == 0)
							&& (amzAdvReportKeyword.getCost() == null || amzAdvReportKeyword.getCost().compareTo(new BigDecimal("0")) == 0)
							) {
						continue;
					}
					list.add(amzAdvReportKeyword);

					if (list.size() >= 2000) {
						amzAdvReportKeywordsMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportKeywordsMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportKeywordsMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportKeywordsMapper.insertBatch(list);
				}
				if (listAttributed.size() >0) {
					amzAdvReportKeywordsMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedSame.size() >0) {
					amzAdvReportKeywordsMapper.insertBatchAttributedSame(listAttributedSame);
				}
			}catch(Exception e) {
				e.printStackTrace();
			} 
		}

	 
}
