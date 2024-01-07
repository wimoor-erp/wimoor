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
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductTargeSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductTargetsSDAttributedView;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatProductTargetSDService")
public class AmzAdvReportTreatProductTargetSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductTargeSDMapper amzAdvReportProductTargeSDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			  List<AmzAdvReportProductTargetsSD> list = new LinkedList<AmzAdvReportProductTargetsSD>();
			  List<AmzAdvReportProductTargetsSDAttributed> listAttributed = new LinkedList<AmzAdvReportProductTargetsSDAttributed>();
			  List<AmzAdvReportProductTargetsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportProductTargetsSDAttributedView>();
			  List<AmzAdvReportProductTargetsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductTargetsSDAttributedSame>();
			  List<AmzAdvReportProductTargetsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportProductTargetsSDAttributedNew>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductTargetsSD amzAdvReportProductTarge = new AmzAdvReportProductTargetsSD();
					AmzAdvReportProductTargetsSDAttributed amzAdvReportProductTargetsSDAttributed = new AmzAdvReportProductTargetsSDAttributed();
					AmzAdvReportProductTargetsSDAttributedView amzAdvReportProductTargetsSDAttributedView = new AmzAdvReportProductTargetsSDAttributedView();
					AmzAdvReportProductTargetsSDAttributedSame amzAdvReportProductTargetsSDAttributedSame = new AmzAdvReportProductTargetsSDAttributedSame();
					AmzAdvReportProductTargetsSDAttributedNew amzAdvReportProductTargetsSDAttributedNew = new AmzAdvReportProductTargetsSDAttributedNew();
					
					amzAdvReportProductTarge.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportProductTarge.setTargetid(item.getBigInteger("targetId"));
					amzAdvReportProductTarge.setAdgroupid(item.getBigInteger("adGroupId"));
				 

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
					amzAdvReportProductTargetsSDAttributed.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsSDAttributed.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsSDAttributed.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));


					amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

					amzAdvReportProductTargetsSDAttributed.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					amzAdvReportProductTargetsSDAttributed.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


					amzAdvReportProductTargetsSDAttributedSame.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsSDAttributedSame.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsSDAttributedSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
					
					amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					amzAdvReportProductTargetsSDAttributedSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));

					
					
					amzAdvReportProductTargetsSDAttributedNew.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsSDAttributedNew.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsSDAttributedNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
					amzAdvReportProductTargetsSDAttributedNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
					amzAdvReportProductTargetsSDAttributedNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
					
					amzAdvReportProductTargetsSDAttributedView.setTargetid(amzAdvReportProductTarge.getTargetid());
					amzAdvReportProductTargetsSDAttributedView.setBydate(amzAdvReportProductTarge.getBydate());
					amzAdvReportProductTargetsSDAttributedView.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
					amzAdvReportProductTargetsSDAttributedView.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
					amzAdvReportProductTargetsSDAttributedView.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
					amzAdvReportProductTargetsSDAttributedView.setViewImpressions(item.getInteger("viewImpressions"));
					if(!amzAdvReportProductTargetsSDAttributedView.isZero()) {
						listAttributedView.add(amzAdvReportProductTargetsSDAttributedView);
					}
					if ((amzAdvReportProductTarge.getImpressions() == null || amzAdvReportProductTarge.getImpressions() == 0)) {
						continue;
					}
					
					if(!amzAdvReportProductTargetsSDAttributed.isZero()) {
						listAttributed.add(amzAdvReportProductTargetsSDAttributed);
					}
					if(!amzAdvReportProductTargetsSDAttributedSame.isZero()) {
						listAttributedSame.add(amzAdvReportProductTargetsSDAttributedSame);
					}
					if(!amzAdvReportProductTargetsSDAttributedNew.isZero()) {
						listAttributedNew.add(amzAdvReportProductTargetsSDAttributedNew);
					}
					list.add(amzAdvReportProductTarge);
					if (list.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatch(list);
						list.clear();
					}
					if (listAttributed.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
						listAttributed.clear();
					}
					if (listAttributedView.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatchAttributedView(listAttributedView);
						listAttributedView.clear();
					}
					if (listAttributedSame.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatchAttributedSame(listAttributedSame);
						listAttributedSame.clear();
					}
					if (listAttributedNew.size() >= 2000) {
						amzAdvReportProductTargeSDMapper.insertBatchAttributedNew(listAttributedNew);
						listAttributedNew.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedView.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedView(listAttributedView);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedSame(listAttributedSame);
				}
				if (listAttributedNew.size() > 0) {
					amzAdvReportProductTargeSDMapper.insertBatchAttributedNew(listAttributedNew);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}


	 
}
