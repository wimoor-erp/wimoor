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
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportCampaignsT0001SDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportCampaignsT0001SD;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatCampaignsT00001SDService")
public class AmzAdvReportTreatCampaignsT00001SDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportCampaignsT0001SDMapper amzAdvReportCampaignsT0001SDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportCampaignsT0001SD> list = new LinkedList<AmzAdvReportCampaignsT0001SD>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportCampaignsT0001SD amzAdvReportCampaigns = new AmzAdvReportCampaignsT0001SD();
					amzAdvReportCampaigns.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportCampaigns.setBydate(request.getStartDate());
					}else {
						amzAdvReportCampaigns.setBydate(date);
					}
					amzAdvReportCampaigns.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportCampaigns.setClicks(item.getInteger("clicks"));
					amzAdvReportCampaigns.setCost(item.getBigDecimal("cost"));
					amzAdvReportCampaigns.setImpressions(item.getInteger("impressions"));
					amzAdvReportCampaigns.setAttributeddpv14d(item.getInteger("attributedDPV14d"));
					amzAdvReportCampaigns.setAttributedsales14d(item.getInteger("attributedSales14d"));
					amzAdvReportCampaigns.setAttributedunitssold14d(item.getInteger("attributedUnitsSold14d"));
					
					list.add(amzAdvReportCampaigns);
					if (list.size() >= 2000) {
						amzAdvReportCampaignsT0001SDMapper.insertBatch(list);
						list.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportCampaignsT0001SDMapper.insertBatch(list);
				}
			} finally {
				if (jsonReader != null) {
					try {
						jsonReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	 
}
