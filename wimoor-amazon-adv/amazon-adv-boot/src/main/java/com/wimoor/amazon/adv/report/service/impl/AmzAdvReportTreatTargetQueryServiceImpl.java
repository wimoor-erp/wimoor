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
import com.wimoor.amazon.adv.sp.dao.AmzAdvReportTargetQueryMapper;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQuery;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributed;
import com.wimoor.amazon.adv.sp.pojo.AmzAdvReportTargetQueryAttributedSame;
import com.wimoor.amazon.adv.utils.EmojiFilterUtils;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatTargetQueryService")
public class AmzAdvReportTreatTargetQueryServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportTargetQueryMapper amzAdvReportTargetQueryMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportTargetQuery> list = new LinkedList<AmzAdvReportTargetQuery>();
			final List<AmzAdvReportTargetQueryAttributed> listAttributed = new LinkedList<AmzAdvReportTargetQueryAttributed>();
			final List<AmzAdvReportTargetQueryAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportTargetQueryAttributedSame>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportTargetQuery amzAdvReportTargetQuery = new AmzAdvReportTargetQuery();
					AmzAdvReportTargetQueryAttributed amzAdvReportTargetQueryAttributed = new AmzAdvReportTargetQueryAttributed();
					AmzAdvReportTargetQueryAttributedSame amzAdvReportTargetQueryAttributedSame = new AmzAdvReportTargetQueryAttributedSame();
					amzAdvReportTargetQuery.setCampaignid(item.getBigInteger("campaignId"));
					BigInteger target=item.getBigInteger("targetId")==null?item.getBigInteger("keywordId"):item.getBigInteger("targetId");
					amzAdvReportTargetQuery.setTargetid(target);
					amzAdvReportTargetQuery.setAdgroupid(item.getBigInteger("adGroupId"));
					String searchTerm=item.getString("searchTerm");
					String query=EmojiFilterUtils.filterEmoji(searchTerm);
			        AmzAdvRptQuery queryobj = loadQuery(query);
			        if(queryobj!=null&&queryobj.getId()!=null) {
			        	amzAdvReportTargetQuery.setQueryid(new BigInteger(queryobj.getId()));
			        }else {
			        	continue;
			        }
					amzAdvReportTargetQuery.setClicks(item.getInteger("clicks"));
					amzAdvReportTargetQuery.setImpressions(item.getInteger("impressions"));
					amzAdvReportTargetQuery.setCost(item.getBigDecimal("cost"));
					amzAdvReportTargetQuery.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportTargetQuery.setBydate(request.getStartDate());
					}else {
						amzAdvReportTargetQuery.setBydate(date);
					}
					amzAdvReportTargetQuery.setOpttime(new Date());
					amzAdvReportTargetQueryAttributed.setBydate(amzAdvReportTargetQuery.getBydate());
					amzAdvReportTargetQueryAttributed.setQueryid(amzAdvReportTargetQuery.getQueryid());
					amzAdvReportTargetQueryAttributed.setTargetid(amzAdvReportTargetQuery.getTargetid());
					amzAdvReportTargetQueryAttributed.setAttributedsales1d(item.getBigDecimal("sales1d"));
					amzAdvReportTargetQueryAttributed.setAttributedsales7d(item.getBigDecimal("sales7d"));
					amzAdvReportTargetQueryAttributed.setAttributedsales14d(item.getBigDecimal("sales14d"));
					amzAdvReportTargetQueryAttributed.setAttributedsales30d(item.getBigDecimal("sales30d"));


					amzAdvReportTargetQueryAttributed.setAttributedunitsordered1d(item.getInteger("unitsSoldClicks1d"));
					amzAdvReportTargetQueryAttributed.setAttributedunitsordered7d(item.getInteger("unitsSoldClicks7d"));
					amzAdvReportTargetQueryAttributed.setAttributedunitsordered14d(item.getInteger("unitsSoldClicks14d"));
					amzAdvReportTargetQueryAttributed.setAttributedunitsordered30d(item.getInteger("unitsSoldClicks30d"));

					amzAdvReportTargetQueryAttributed.setAttributedconversions1d(item.getInteger("purchases1d"));
					amzAdvReportTargetQueryAttributed.setAttributedconversions7d(item.getInteger("purchases7d"));
					amzAdvReportTargetQueryAttributed.setAttributedconversions14d(item.getInteger("purchases14d"));
					amzAdvReportTargetQueryAttributed.setAttributedconversions30d(item.getInteger("purchases30d"));


					amzAdvReportTargetQueryAttributedSame.setBydate(amzAdvReportTargetQuery.getBydate());
					amzAdvReportTargetQueryAttributedSame.setQueryid(amzAdvReportTargetQuery.getQueryid());
					amzAdvReportTargetQueryAttributedSame.setTargetid(amzAdvReportTargetQuery.getTargetid());
					
					amzAdvReportTargetQueryAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSalesSameSku1d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSalesSameSku7d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSalesSameSku14d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSalesSameSku30d"));
					
					amzAdvReportTargetQueryAttributedSame.setAttributedconversions1dsamesku(item.getInteger("purchasesSameSku1d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedconversions7dsamesku(item.getInteger("purchasesSameSku7d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedconversions14dsamesku(item.getInteger("purchasesSameSku14d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedconversions30dsamesku(item.getInteger("purchasesSameSku30d"));

					amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered1dSameSKU(item.getInteger("unitsSoldSameSku1d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered7dSameSKU(item.getInteger("unitsSoldSameSku7d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered14dSameSKU(item.getInteger("unitsSoldSameSku14d"));
					amzAdvReportTargetQueryAttributedSame.setAttributedUnitsOrdered30dSameSKU(item.getInteger("unitsSoldSameSku30d"));
					if ((amzAdvReportTargetQuery.getImpressions() == null || amzAdvReportTargetQuery.getImpressions() == 0)) {
						continue;
					}
					if(!amzAdvReportTargetQueryAttributed.isZero()) {
						listAttributed.add(amzAdvReportTargetQueryAttributed);
					}
					if(!amzAdvReportTargetQueryAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportTargetQueryAttributedSame);
					}
					
					list.add(amzAdvReportTargetQuery);
					if (list.size() >= 2000) {
						amzAdvReportTargetQueryMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportTargetQueryMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportTargetQueryMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
					
				}
				if (list.size() > 0) {
					amzAdvReportTargetQueryMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportTargetQueryMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportTargetQueryMapper.insertBatchAttributedSame(listAttributedSame);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}  
		}

	 
}
