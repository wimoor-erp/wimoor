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
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAdGroupsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAdGroupsSDAttributedView;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatAdgroupsSDService")
public class AmzAdvReportTreatAdgroupsSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAdGroupsSDMapper amzAdvReportAdGroupsSDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub

			final List<AmzAdvReportAdGroupsSD> list = new LinkedList<AmzAdvReportAdGroupsSD>();
			final List<AmzAdvReportAdGroupsSDAttributed> listAttributed = new LinkedList<AmzAdvReportAdGroupsSDAttributed>();
			final List<AmzAdvReportAdGroupsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportAdGroupsSDAttributedView>();
			final List<AmzAdvReportAdGroupsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportAdGroupsSDAttributedSame>();
			final List<AmzAdvReportAdGroupsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportAdGroupsSDAttributedNew>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAdGroupsSD amzAdvReportAdGroupsSD = new AmzAdvReportAdGroupsSD();
					AmzAdvReportAdGroupsSDAttributed amzAdvReportAdGroupsSDAttributed = new AmzAdvReportAdGroupsSDAttributed();
					AmzAdvReportAdGroupsSDAttributedView amzAdvReportAdGroupsSDAttributedView = new AmzAdvReportAdGroupsSDAttributedView();
					AmzAdvReportAdGroupsSDAttributedSame amzAdvReportAdGroupsSDAttributedSame = new AmzAdvReportAdGroupsSDAttributedSame();
					AmzAdvReportAdGroupsSDAttributedNew amzAdvReportAdGroupsSDAttributedNew = new AmzAdvReportAdGroupsSDAttributedNew();
					amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
					amzAdvReportAdGroupsSD.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportAdGroupsSD.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportAdGroupsSD.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportAdGroupsSD.setBydate(request.getStartDate());
					}else {
						amzAdvReportAdGroupsSD.setBydate(date);
					}

					amzAdvReportAdGroupsSD.setClicks(item.getInteger("clicks"));
					amzAdvReportAdGroupsSD.setImpressions(item.getInteger("impressions"));
					amzAdvReportAdGroupsSD.setCost(item.getBigDecimal("cost"));
					amzAdvReportAdGroupsSD.setOpttime(new Date());
					
					amzAdvReportAdGroupsSDAttributed.setBydate(amzAdvReportAdGroupsSD.getBydate());
					amzAdvReportAdGroupsSDAttributed.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
					amzAdvReportAdGroupsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));



					amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

					amzAdvReportAdGroupsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportAdGroupsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));
					
					
					amzAdvReportAdGroupsSDAttributedSame.setBydate(amzAdvReportAdGroupsSD.getBydate());
					amzAdvReportAdGroupsSDAttributedSame.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
					
					amzAdvReportAdGroupsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));

					amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportAdGroupsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
				 
					amzAdvReportAdGroupsSDAttributedNew.setBydate(amzAdvReportAdGroupsSD.getBydate());
					amzAdvReportAdGroupsSDAttributedNew.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
					
					amzAdvReportAdGroupsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
					amzAdvReportAdGroupsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
					amzAdvReportAdGroupsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
					
					amzAdvReportAdGroupsSDAttributedView.setAdgroupid(amzAdvReportAdGroupsSD.getAdgroupid());
					amzAdvReportAdGroupsSDAttributedView.setBydate(amzAdvReportAdGroupsSD.getBydate());
					amzAdvReportAdGroupsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
					amzAdvReportAdGroupsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
					amzAdvReportAdGroupsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
					amzAdvReportAdGroupsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
					
					list.add(amzAdvReportAdGroupsSD);
					if(!amzAdvReportAdGroupsSDAttributed.isZero()) {
						listAttributed.add(amzAdvReportAdGroupsSDAttributed);
					}
					if(!amzAdvReportAdGroupsSDAttributedView.isZero()) {
						listAttributedView.add(amzAdvReportAdGroupsSDAttributedView);
					}
					if(!amzAdvReportAdGroupsSDAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportAdGroupsSDAttributedSame);
					}
					if(!amzAdvReportAdGroupsSDAttributedNew.isZero()) {
						listAttributedNew.add(amzAdvReportAdGroupsSDAttributedNew);
					}
					if (list.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatch(list);
						list.clear();
					}
					
					if (listAttributed.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedView.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatchAttributedView(listAttributedView);
						listAttributedView.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
					if (listAttributedNew.size() >= 2000) {
						amzAdvReportAdGroupsSDMapper.insertBatchAttributedNew(listAttributedNew);
						listAttributedNew.clear();
					}
					
				}
				if (list.size() > 0) {
					amzAdvReportAdGroupsSDMapper.insertBatch(list);
				}
				if (listAttributed.size() >0) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributed.size() >0) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedView(listAttributedView);
				}
				if (listAttributedSame.size() >0) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedSame(listAttributedSame);
				}
				if (listAttributedNew.size() >0) {
					amzAdvReportAdGroupsSDMapper.insertBatchAttributedNew(listAttributedNew);
				}
			} finally {
				try {
					if (jsonReader != null) {
						jsonReader.close();
					}
				} catch (Exception e) {
				}
			}
		}

	 
}
