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
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportAdgroupsMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroups;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportAdgroupsAttributedSame;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatAdgroupsService")
public class AmzAdvReportTreatAdgroupsServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAdgroupsMapper amzAdvReportAdgroupsMapper;
	/*
	 {"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["campaign","adGroup"],
 "columns":["campaignId","adGroupId",
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
			final List<AmzAdvReportAdgroups> list = new LinkedList<AmzAdvReportAdgroups>();
			final List<AmzAdvReportAdgroupsAttributed> listAttributed = new LinkedList<AmzAdvReportAdgroupsAttributed>();
			final List<AmzAdvReportAdgroupsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportAdgroupsAttributedSame>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAdgroups amzAdvReportGroup = new AmzAdvReportAdgroups();
					AmzAdvReportAdgroupsAttributed amzAdvReportAdgroupsAttributed=new AmzAdvReportAdgroupsAttributed();
					AmzAdvReportAdgroupsAttributedSame amzAdvReportAdgroupsAttributedSame=new AmzAdvReportAdgroupsAttributedSame();
					amzAdvReportGroup.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportGroup.setBydate(request.getStartDate());
					}else {
						amzAdvReportGroup.setBydate(date);
					}
					amzAdvReportGroup.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportGroup.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportGroup.setProfileid(request.getProfileid());

					amzAdvReportGroup.setClicks(item.getInteger("clicks"));
					amzAdvReportGroup.setImpressions(item.getInteger("impressions"));
					amzAdvReportGroup.setCost(item.getBigDecimal("cost"));
					amzAdvReportGroup.setOpttime(new Date());
					amzAdvReportAdgroupsAttributed.setBydate(amzAdvReportGroup.getBydate());
					amzAdvReportAdgroupsAttributed.setAdgroupid(amzAdvReportGroup.getAdgroupid());
					amzAdvReportAdgroupsAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportAdgroupsAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportAdgroupsAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportAdgroupsAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));



					amzAdvReportAdgroupsAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportAdgroupsAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportAdgroupsAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportAdgroupsAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

					amzAdvReportAdgroupsAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportAdgroupsAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportAdgroupsAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportAdgroupsAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));
					
					
					
					amzAdvReportAdgroupsAttributedSame.setBydate(amzAdvReportGroup.getBydate());
					amzAdvReportAdgroupsAttributedSame.setAdgroupid(amzAdvReportGroup.getAdgroupid());
					amzAdvReportAdgroupsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));

					amzAdvReportAdgroupsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));
					
					amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportAdgroupsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					
					if(!amzAdvReportAdgroupsAttributed.isZero()) {
						listAttributed.add(amzAdvReportAdgroupsAttributed);
					}
					if(!amzAdvReportAdgroupsAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportAdgroupsAttributedSame);
					}
					if ((amzAdvReportGroup.getClicks() == null || amzAdvReportGroup.getClicks() == 0)
							&& (amzAdvReportGroup.getImpressions() == null || amzAdvReportGroup.getImpressions() == 0)
							&& (amzAdvReportGroup.getCost() == null || amzAdvReportGroup.getCost().compareTo(new BigDecimal("0")) == 0)
							) {
						continue;
					}
					list.add(amzAdvReportGroup);
		
					if (list.size() >= 2000) {
						amzAdvReportAdgroupsMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportAdgroupsMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportAdgroupsMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportAdgroupsMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportAdgroupsMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportAdgroupsMapper.insertBatchAttributedSame(listAttributedSame);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
