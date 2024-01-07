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
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportProductTargeMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargets;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargetsAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportProductTargetsAttributedSame;
import com.wimoor.amazon.adv.utils.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatProductTargetService")
public class AmzAdvReportTreatProductTargetServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductTargeMapper amzAdvReportProductTargeMapper;
	/*
	 {"adProduct":"SPONSORED_PRODUCTS",
 "groupBy":["searchTerm"],
 "columns":["date","campaignId", "adGroupId", "keywordId", "searchTerm",
            "impressions", "clicks", "cost", "spend", 
            "purchases1d", "purchases7d", "purchases14d", "purchases30d", 
            "purchasesSameSku1d", "purchasesSameSku7d", "purchasesSameSku14d", "purchasesSameSku30d", 
            "unitsSoldClicks1d", "unitsSoldClicks7d", "unitsSoldClicks14d", "unitsSoldClicks30d", 
            "sales1d", "sales7d", "sales14d", "sales30d", 
            "attributedSalesSameSku1d", "attributedSalesSameSku7d", "attributedSalesSameSku14d", "attributedSalesSameSku30d", 
            "unitsSoldSameSku1d", "unitsSoldSameSku7d", "unitsSoldSameSku14d", "unitsSoldSameSku30d", "unitsSoldOtherSku7d", 
            "kindleEditionNormalizedPagesRead14d", "kindleEditionNormalizedPagesRoyalties14d", 
            "acosClicks7d", "acosClicks14d", "roasClicks7d", "roasClicks14d"],
 "filters": [
            {
                "field": "keywordType",
                "values": [
                    "TARGETING_EXPRESSION",
                    "TARGETING_EXPRESSION_PREDEFINED"
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
			  List<AmzAdvReportProductTargets> list = new LinkedList<AmzAdvReportProductTargets>();
			  List<AmzAdvReportProductTargetsAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsAttributed>();
			  List<AmzAdvReportProductTargetsAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductTargetsAttributedSame>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductTargets amzAdvReportProductTarge = new AmzAdvReportProductTargets();
					AmzAdvReportProductTargetsAttributed amzAdvReportProductTargetsAttributed=new AmzAdvReportProductTargetsAttributed();
					AmzAdvReportProductTargetsAttributedSame amzAdvReportProductTargetsAttributedSame=new AmzAdvReportProductTargetsAttributedSame();
					amzAdvReportProductTarge.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportProductTarge.setTargetid(item.getBigInteger("keywordId"));
					amzAdvReportProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
					String query=EmojiFilterUtils.filterEmoji(item.getString("searchTerm"));
					System.out.println(query);
					amzAdvReportProductTarge.setClicks(item.getInteger("clicks"));
					amzAdvReportProductTarge.setImpressions(item.getInteger("impressions"));
					amzAdvReportProductTarge.setCost(item.getBigDecimal("cost"));
					amzAdvReportProductTarge.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportProductTarge.setBydate(request.getStartDate());
					}else {
						amzAdvReportProductTarge.setBydate(date);
					}
					amzAdvReportProductTarge.setOpttime(new Date());
					amzAdvReportProductTargetsAttributed.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsAttributed.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportProductTargetsAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportProductTargetsAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportProductTargetsAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));


					amzAdvReportProductTargetsAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportProductTargetsAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportProductTargetsAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportProductTargetsAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

					amzAdvReportProductTargetsAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportProductTargetsAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportProductTargetsAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportProductTargetsAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));


					amzAdvReportProductTargetsAttributedSame.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsAttributedSame.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
					
					amzAdvReportProductTargetsAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));

					amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportProductTargetsAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					
					if ((amzAdvReportProductTarge.getImpressions() == null || amzAdvReportProductTarge.getImpressions() == 0)) {
						continue;
					}
					list.add(amzAdvReportProductTarge);
					if(!amzAdvReportProductTargetsAttributed.isZero()) {
						listAttributed.add(amzAdvReportProductTargetsAttributed);
					}
					if(!amzAdvReportProductTargetsAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportProductTargetsAttributedSame);
					}
					if (list.size() >= 2000) {
						amzAdvReportProductTargeMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportProductTargeMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportProductTargeMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportProductTargeMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductTargeMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportProductTargeMapper.insertBatchAttributedSame(listAttributedSame);
				}
			} catch(Exception e){
				e.printStackTrace();
			} 
		}

	 
}
