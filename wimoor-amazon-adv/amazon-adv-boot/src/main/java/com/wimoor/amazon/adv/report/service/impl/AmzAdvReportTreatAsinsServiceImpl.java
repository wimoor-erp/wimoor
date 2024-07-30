package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigInteger;
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
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAsinsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAsins;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatAsinsService")
public class AmzAdvReportTreatAsinsServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAsinsMapper amzAdvReportAsinsMapper;
	/*
{"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["asin"],
 "columns":["date", "campaignId", "adGroupId", "keywordId", "advertisedAsin", "purchasedAsin", "advertisedSku", 
            "unitsSoldClicks1d", "unitsSoldClicks7d", "unitsSoldClicks14d", "unitsSoldClicks30d", 
            "sales1d", "sales7d", "sales14d", "sales30d", 
            "purchases1d", "purchases7d", "purchases14d", "purchases30d", 
            "unitsSoldOtherSku1d", "unitsSoldOtherSku7d", "unitsSoldOtherSku14d", "unitsSoldOtherSku30d", 
            "salesOtherSku1d", "salesOtherSku7d", "salesOtherSku14d", "salesOtherSku30d", 
            "purchasesOtherSku1d", "purchasesOtherSku7d", "purchasesOtherSku14d", "purchasesOtherSku30d", 
            "kindleEditionNormalizedPagesRead14d", "kindleEditionNormalizedPagesRoyalties14d"],
 "reportTypeId":"spPurchasedProduct",
 "timeUnit":"DAILY",
 "format":"GZIP_JSON"
 }
	
	 */
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportAsins> list = new LinkedList<AmzAdvReportAsins>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAsins amzAdvReportAsins = new AmzAdvReportAsins();
					amzAdvReportAsins.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportAsins.setBydate(request.getStartDate());
					}else {
						amzAdvReportAsins.setBydate(date);
					}
					amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
					BigInteger keywordId = item.getBigInteger("keywordId");
					if(keywordId.compareTo(new BigInteger("0"))==-1){
						keywordId = keywordId.multiply(new BigInteger("-1"));
					}
					amzAdvReportAsins.setKeywordid(keywordId);
					amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
					String asin = item.getString("advertisedAsin");
					if(asin==null){
						continue;
					}
					amzAdvReportAsins.setAdvertisedAsin(asin);
					amzAdvReportAsins.setSku(item.getString("advertisedSku"));
					amzAdvReportAsins.setPurchasedAsin(item.getString("purchasedAsin"));
					amzAdvReportAsins.setOpttime(new Date());
					 

					amzAdvReportAsins.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportAsins.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportAsins.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportAsins.setAttributedsales30d(item.getBigDecimal("sales30d"));

					amzAdvReportAsins.setAttributedsales1dsamesku(item.getBigDecimal("salesOtherSku1d"));
					amzAdvReportAsins.setAttributedsales7dsamesku(item.getBigDecimal("salesOtherSku7d"));
					amzAdvReportAsins.setAttributedsales14dsamesku(item.getBigDecimal("salesOtherSku14d"));
					amzAdvReportAsins.setAttributedsales30dsamesku(item.getBigDecimal("salesOtherSku30d"));
					
					amzAdvReportAsins.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportAsins.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportAsins.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportAsins.setAttributedconversions30d(item.getInteger("purchases30d"));
					
					
					amzAdvReportAsins.setAttributedconversions1dsamesku(item.getInteger("purchasesOtherSku1d"));
					amzAdvReportAsins.setAttributedconversions7dsamesku(item.getInteger("purchasesOtherSku7d"));
					amzAdvReportAsins.setAttributedconversions14dsamesku(item.getInteger("purchasesOtherSku14d"));
					amzAdvReportAsins.setAttributedconversions30dsamesku(item.getInteger("purchasesOtherSku30d"));
					
					amzAdvReportAsins.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportAsins.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportAsins.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportAsins.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));
					
					amzAdvReportAsins.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldOtherSku1d"));
					amzAdvReportAsins.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldOtherSku7d"));
					amzAdvReportAsins.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldOtherSku14d"));
					amzAdvReportAsins.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldOtherSku30d"));
	                if(!amzAdvReportAsins.isZero()) {
	                	list.add(amzAdvReportAsins);
	                }
					if (list.size() >= 2000) {
						amzAdvReportAsinsMapper.insertBatch(list);
						list.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportAsinsMapper.insertBatch(list);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
