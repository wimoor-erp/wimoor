package com.wimoor.amazon.adv.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.report.pojo.AmzAdvRequest;
import com.wimoor.amazon.adv.report.pojo.AmzAdvSumProductAds;
import com.wimoor.amazon.adv.report.service.IAmzAdvReportTreatService;
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportCampaignsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsSDAttributedView;
import com.wimoor.common.GeneralUtil;

import tk.mybatis.mapper.util.StringUtil;
@Service("amzAdvReportTreatCampaignsSDService")
public class AmzAdvReportTreatCampaignsSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportCampaignsSDMapper amzAdvReportCampaignsSDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub

			 List<AmzAdvReportCampaignsSD> list = new LinkedList<AmzAdvReportCampaignsSD>();
			 List<AmzAdvReportCampaignsSDAttributed> listAttributed = new LinkedList<AmzAdvReportCampaignsSDAttributed>();
			 List<AmzAdvReportCampaignsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportCampaignsSDAttributedView>();
			 List<AmzAdvReportCampaignsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportCampaignsSDAttributedNew>();
			 List<AmzAdvReportCampaignsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportCampaignsSDAttributedSame>();
				String currency = "";
		 
			try {
				Map<String,AmzAdvSumProductAds> mapsum=new HashMap<String,AmzAdvSumProductAds>();
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);

					AmzAdvReportCampaignsSD amzAdvReportCampaignsSD = new AmzAdvReportCampaignsSD();
					AmzAdvReportCampaignsSDAttributed amzAdvReportCampaignsSDAttributed = new AmzAdvReportCampaignsSDAttributed();
					AmzAdvReportCampaignsSDAttributedView amzAdvReportCampaignsSDAttributedView = new AmzAdvReportCampaignsSDAttributedView();
					AmzAdvReportCampaignsSDAttributedSame amzAdvReportCampaignsSDAttributedSame = new AmzAdvReportCampaignsSDAttributedSame();
					AmzAdvReportCampaignsSDAttributedNew amzAdvReportCampaignsSDAttributedNew = new AmzAdvReportCampaignsSDAttributedNew();
					amzAdvReportCampaignsSD.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						date=request.getStartDate();
					}
					amzAdvReportCampaignsSD.setBydate(date);
					amzAdvReportCampaignsSD.setCampaignid(item.getBigInteger("campaignId"));
					if (currency == null && StringUtil.isNotEmpty(item.getString("currency"))) {
						currency =item.getString("currency");
					}
					amzAdvReportCampaignsSD.setClicks(item.getInteger("clicks"));
					amzAdvReportCampaignsSD.setImpressions(item.getInteger("impressions"));
					amzAdvReportCampaignsSD.setCost(item.getBigDecimal("cost"));
					amzAdvReportCampaignsSD.setOpttime(new Date());
					amzAdvReportCampaignsSDAttributed.setBydate(amzAdvReportCampaignsSD.getBydate());
					amzAdvReportCampaignsSDAttributed.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
					amzAdvReportCampaignsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
					amzAdvReportCampaignsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
					amzAdvReportCampaignsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportCampaignsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));



					amzAdvReportCampaignsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
					amzAdvReportCampaignsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
					amzAdvReportCampaignsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
					amzAdvReportCampaignsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

					amzAdvReportCampaignsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
					amzAdvReportCampaignsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
					amzAdvReportCampaignsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportCampaignsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
					
					amzAdvReportCampaignsSDAttributedSame.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
					amzAdvReportCampaignsSDAttributedSame.setBydate(amzAdvReportCampaignsSD.getBydate());
					amzAdvReportCampaignsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
					

					amzAdvReportCampaignsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportCampaignsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
					
					
					amzAdvReportCampaignsSDAttributedNew.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
					amzAdvReportCampaignsSDAttributedNew.setBydate(amzAdvReportCampaignsSD.getBydate());
					amzAdvReportCampaignsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
					amzAdvReportCampaignsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
					amzAdvReportCampaignsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
					
			 
					
					amzAdvReportCampaignsSDAttributedView.setCampaignid(amzAdvReportCampaignsSD.getCampaignid());
					amzAdvReportCampaignsSDAttributedView.setBydate(amzAdvReportCampaignsSD.getBydate());
					amzAdvReportCampaignsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
					amzAdvReportCampaignsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
					amzAdvReportCampaignsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
					amzAdvReportCampaignsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
					
					
					
				 
					
					
					 addSummary( request,mapsum, date,"sd",amzAdvReportCampaignsSD.getClicks(),
							     currency,amzAdvReportCampaignsSD.getImpressions(),amzAdvReportCampaignsSD.getCost(),
							     amzAdvReportCampaignsSDAttributed.getAttributedsales7d(),
							     amzAdvReportCampaignsSDAttributed.getAttributedunitsordered7d(),
							     amzAdvReportCampaignsSDAttributed.getAttributedconversions7d());
					 
					if(!amzAdvReportCampaignsSDAttributed.isZero()) {
						listAttributed.add(amzAdvReportCampaignsSDAttributed);
					}
					if(!amzAdvReportCampaignsSDAttributedView.isZero()) {
						listAttributedView.add(amzAdvReportCampaignsSDAttributedView);
					}
					if(!amzAdvReportCampaignsSDAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportCampaignsSDAttributedSame);
					}
					if(!amzAdvReportCampaignsSDAttributedNew.isZero()) {
						listAttributedNew.add(amzAdvReportCampaignsSDAttributedNew);
					}
					if ((amzAdvReportCampaignsSD.getClicks() == null || amzAdvReportCampaignsSD.getClicks() == 0)
							&& (amzAdvReportCampaignsSD.getImpressions() == null || amzAdvReportCampaignsSD.getImpressions() == 0)
							&& (amzAdvReportCampaignsSD.getCost() == null || amzAdvReportCampaignsSD.getCost().compareTo(new BigDecimal("0")) == 0)
							) {
						continue;
					}
					list.add(amzAdvReportCampaignsSD);
			
					
					if (list.size() >= 2000) {
						amzAdvReportCampaignsSDMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportCampaignsSDMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedView.size() >= 2000) {
						amzAdvReportCampaignsSDMapper.insertBatchAttributedView(listAttributedView);
						listAttributedView.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportCampaignsSDMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
					if (listAttributedNew.size() >= 2000) {
						amzAdvReportCampaignsSDMapper.insertBatchAttributedNew(listAttributedNew);
						listAttributedNew.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportCampaignsSDMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedView.size() > 0) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedView(listAttributedView);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedSame(listAttributedSame);
				}
				if (listAttributedNew.size() > 0) {
					amzAdvReportCampaignsSDMapper.insertBatchAttributedNew(listAttributedNew);
				}
				for(Entry<String, AmzAdvSumProductAds> entry:mapsum.entrySet()) {
					AmzAdvSumProductAds record=entry.getValue();
					AmzAdvSumProductAds old = amzAdvSumProductAdsMapper.selectByKey(record);
					if (old != null) {
						amzAdvSumProductAdsMapper.updateByKey(record);
					} else {
						amzAdvSumProductAdsMapper.insert(record);
					}
				}
				
			}  catch(Exception e) {
				e.printStackTrace();
			}
		}


	 
}
