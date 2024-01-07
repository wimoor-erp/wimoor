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
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportProductAdsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSD;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributed;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedNew;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedSame;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportProductAdsSDAttributedView;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatProductSDService")
public class AmzAdvReportTreatProductSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportProductAdsSDMapper amzAdvReportProductAdsSDMapper;
	@Resource
	protected IAmazonReportAdvSummaryService amazonReportAdvSummaryService;
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
			List<AmzAdvReportProductAdsSD> list = new LinkedList<AmzAdvReportProductAdsSD>();
			List<AmzAdvReportProductAdsSDAttributed> listAttributed = new LinkedList<AmzAdvReportProductAdsSDAttributed>();
			List<AmzAdvReportProductAdsSDAttributedView> listAttributedView = new LinkedList<AmzAdvReportProductAdsSDAttributedView>();
			List<AmzAdvReportProductAdsSDAttributedNew> listAttributedNew = new LinkedList<AmzAdvReportProductAdsSDAttributedNew>();
			List<AmzAdvReportProductAdsSDAttributedSame> listAttributedSame = new LinkedList<AmzAdvReportProductAdsSDAttributedSame>();
			try {
				jsonReader.startArray();

				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportProductAdsSD padv = new AmzAdvReportProductAdsSD();
					AmzAdvReportProductAdsSDAttributed     padvAttr =     new AmzAdvReportProductAdsSDAttributed();
					AmzAdvReportProductAdsSDAttributedSame padvAttrSame = new AmzAdvReportProductAdsSDAttributedSame();
					AmzAdvReportProductAdsSDAttributedNew  padvAttrNew =  new AmzAdvReportProductAdsSDAttributedNew();
					AmzAdvReportProductAdsSDAttributedView padvAttrview=  new AmzAdvReportProductAdsSDAttributedView();
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
					padv.setOpttime(new Date());
					padvAttr.setBydate(padv.getBydate());
					padvAttr.setAdid(padv.getAdid());
					padvAttr.setAttributedconversions1d(item.getInteger("attributedConversions1d"));
					padvAttr.setAttributedconversions7d(item.getInteger("attributedConversions7d"));
					padvAttr.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
					padvAttr.setAttributedconversions30d(item.getInteger("attributedConversions30d"));


					padvAttr.setAttributedunitsordered1d(item.getInteger("attributedUnitsOrdered1d"));
					padvAttr.setAttributedunitsordered7d(item.getInteger("attributedUnitsOrdered7d"));
					padvAttr.setAttributedunitsordered14d(item.getInteger("attributedUnitsOrdered14d"));
					padvAttr.setAttributedunitsordered30d(item.getInteger("attributedUnitsOrdered30d"));

					padvAttr.setAttributedsales1d(item.getBigDecimal("attributedSales1d"));
					padvAttr.setAttributedsales7d(item.getBigDecimal("attributedSales7d"));
					padvAttr.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					padvAttr.setAttributedsales30d(item.getBigDecimal("attributedSales30d"));
					
					padvAttrSame.setBydate(padv.getBydate());
					padvAttrSame.setAdid(padv.getAdid());
					padvAttrSame.setAttributedconversions1dsamesku(item.getInteger("attributedConversions1dSameSKU"));
					padvAttrSame.setAttributedconversions7dsamesku(item.getInteger("attributedConversions7dSameSKU"));
					padvAttrSame.setAttributedconversions14dsamesku(item.getInteger("attributedConversions14dSameSKU"));
					padvAttrSame.setAttributedconversions30dsamesku(item.getInteger("attributedConversions30dSameSKU"));
					
					padvAttrSame.setAttributedsales1dsamesku(item.getBigDecimal("attributedSales1dSameSKU"));
					padvAttrSame.setAttributedsales7dsamesku(item.getBigDecimal("attributedSales7dSameSKU"));
					padvAttrSame.setAttributedsales14dsamesku(item.getBigDecimal("attributedSales14dSameSKU"));
					padvAttrSame.setAttributedsales30dsamesku(item.getBigDecimal("attributedSales30dSameSKU"));
					
					padvAttrNew.setBydate(padv.getBydate());
					padvAttrNew.setAdid(padv.getAdid());
					padvAttrNew.setAttributedUnitsOrderedNewToBrand14d(item.getInteger("attributedUnitsOrderedNewToBrand14d"));
					padvAttrNew.setAttributedOrdersNewToBrand14d(item.getInteger("attributedOrdersNewToBrand14d"));
					padvAttrNew.setAttributedSalesNewToBrand14d(item.getBigDecimal("attributedSalesNewToBrand14d"));
					
					padvAttrview.setBydate(padv.getBydate());
					padvAttrview.setAdid(padv.getAdid());
					padvAttrview.setViewAttributedConversions14d(item.getInteger("viewAttributedConversions14d"));
					padvAttrview.setViewAttributedSales14d(item.getBigDecimal("viewAttributedSales14d"));
					padvAttrview.setViewAttributedUnitsOrdered14d(item.getInteger("viewAttributedUnitsOrdered14d"));
					padvAttrview.setViewImpressions(item.getInteger("viewImpressions"));
					if(padv.getAdid()==null) {
						continue;
					}
					if(!padvAttr.isZero()) {
						listAttributed.add(padvAttr);
					}
					
					if(!padvAttrview.isZero()) {
						listAttributedView.add(padvAttrview);
					}
					if(!padvAttrSame.isZero()) {
						listAttributedSame.add(padvAttrSame);
					}
					if(!padvAttrNew.isZero()) {
						listAttributedNew.add(padvAttrNew);
					}
					if ((padv.getClicks() == null || padv.getClicks() == 0)
							&& (padv.getImpressions() == null || padv.getImpressions() == 0)
							&& (padv.getCost() == null || padv.getCost().compareTo(new BigDecimal("0")) == 0)
							 ) {
						continue;
					}
					list.add(padv);

				}
				if (list.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatch(list);
				}
				if (listAttributed.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatchAttributed(listAttributed);
				}
				if (listAttributedView.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatchAttributedView(listAttributedView);
				}
				if (listAttributedSame.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatchAttributedSame(listAttributedSame);
				}
				if (listAttributedNew.size() > 0) {
					amzAdvReportProductAdsSDMapper.insertBatchAttributedNew(listAttributedNew);
				}
			} catch(Exception e){
				e.printStackTrace();
			} 
		}

}
