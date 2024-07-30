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
import com.wimoor.amazon.adv.report.pojo.AmzAdvRptQuery;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportKeywordsQueryMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportKeywordsQueryAttributedSame;
import com.wimoor.amazon.adv.utils.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatKeywordsQuryService")
public class AmzAdvReportTreatKeywordsQuryServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportKeywordsQueryMapper amzAdvReportKeywordsQuryMapper;
	/*
	 {"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["searchTerm"],
 "columns":["date","campaignId", "adGroupId", "keywordId", 
            "impressions", "clicks", "cost", "spend", 
            "purchases1d", "purchases7d", "purchases14d", "purchases30d", 
            "purchasesSameSku1d", "purchasesSameSku7d", "purchasesSameSku14d", "purchasesSameSku30d", 
            "unitsSoldClicks1d", "unitsSoldClicks7d", "unitsSoldClicks14d", "unitsSoldClicks30d", 
            "sales1d", "sales7d", "sales14d", "sales30d", 
            "attributedSalesSameSku1d", "attributedSalesSameSku7d", "attributedSalesSameSku14d", "attributedSalesSameSku30d", 
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
 "reportTypeId":"spSearchTerm",
 "timeUnit":"DAILY",
 "format":"GZIP_JSON"
 }
					 
 
	 */
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			  List<AmzAdvReportKeywordsQuery> list = new LinkedList<AmzAdvReportKeywordsQuery>();
			  List<AmzAdvReportKeywordsQueryAttributed> listAttributed = new LinkedList<AmzAdvReportKeywordsQueryAttributed>();
			  List<AmzAdvReportKeywordsQueryAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportKeywordsQueryAttributedSame>();
			try {
				jsonReader.startArray();
				int line = 0;
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportKeywordsQuery amzAdvReportKeywordsQury = new AmzAdvReportKeywordsQuery();
					AmzAdvReportKeywordsQueryAttributed amzAdvReportKeywordsQuryAttributed = new AmzAdvReportKeywordsQueryAttributed();
					AmzAdvReportKeywordsQueryAttributedSame amzAdvReportKeywordsQuryAttributedSame = new AmzAdvReportKeywordsQueryAttributedSame();
					String query=EmojiFilterUtils.filterEmoji(item.getString("searchTerm"));
					AmzAdvRptQuery queryobj = loadQuery(query);
					amzAdvReportKeywordsQury.setProfileid(request.getProfileid());
					if(queryobj==null) {
						continue;
					}
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportKeywordsQury.setBydate(request.getStartDate());
					}else {
						amzAdvReportKeywordsQury.setBydate(date);
					}
					amzAdvReportKeywordsQury.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportKeywordsQury.setKeywordid(item.getBigInteger("keywordId"));
					amzAdvReportKeywordsQury.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportKeywordsQury.setQueryid(new BigInteger(queryobj.getId()));
					
					amzAdvReportKeywordsQury.setClicks(item.getInteger("clicks"));
					amzAdvReportKeywordsQury.setImpressions(item.getInteger("impressions"));
					amzAdvReportKeywordsQury.setCost(item.getBigDecimal("cost"));

					amzAdvReportKeywordsQuryAttributed.setKeywordid(amzAdvReportKeywordsQury.getKeywordid());
					amzAdvReportKeywordsQuryAttributed.setBydate(amzAdvReportKeywordsQury.getBydate());
					amzAdvReportKeywordsQuryAttributed.setQueryid(amzAdvReportKeywordsQury.getQueryid());
					
					amzAdvReportKeywordsQuryAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));
				
					amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));
					
					amzAdvReportKeywordsQuryAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportKeywordsQuryAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));
					
					amzAdvReportKeywordsQuryAttributedSame.setKeywordid(amzAdvReportKeywordsQury.getKeywordid());
					amzAdvReportKeywordsQuryAttributedSame.setBydate(amzAdvReportKeywordsQury.getBydate());
					amzAdvReportKeywordsQuryAttributedSame.setQueryid(amzAdvReportKeywordsQury.getQueryid());
					
					amzAdvReportKeywordsQuryAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
					
					amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));

					amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportKeywordsQuryAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					
					list.add(amzAdvReportKeywordsQury);
					if(!amzAdvReportKeywordsQuryAttributed.isZero()) {
						listAttributed.add(amzAdvReportKeywordsQuryAttributed);
					}
					if(!amzAdvReportKeywordsQuryAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportKeywordsQuryAttributedSame);
					}
					if (list.size() >= 2000) {
							amzAdvReportKeywordsQuryMapper.insertBatch(list);
							list.clear();
					}
					if(listAttributed.size()>=2000) {
						amzAdvReportKeywordsQuryMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if(listAttributedSame.size()>=2000) {
						amzAdvReportKeywordsQuryMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
					line++;
				}
				if (list.size() > 0) {
						amzAdvReportKeywordsQuryMapper.insertBatch(list);
						list.clear();
				}
				if(listAttributed.size()>0) {
					amzAdvReportKeywordsQuryMapper.insertBatchAttributed(listAttributed);
					listAttributed.clear();
				}
				if(listAttributedSame.size()>0) {
					amzAdvReportKeywordsQuryMapper.insertBatchAttributedSame(listAttributedSame);
					listAttributed.clear();
				}
				saveAmzAdvSumAllType(line, request);
			}catch(Exception e) {
				e.printStackTrace();
			} 
		}

	 
}
