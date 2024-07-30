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
import com.wimoor.amazon.adv.sd.dao.AmzAdvReportAsinsSDMapper;
import com.wimoor.amazon.adv.sd.pojo.AmzAdvReportAsinsSD;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;
@Service("amzAdvReportTreatAsinSDService")
public class AmzAdvReportTreatAsinSDServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportAsinsSDMapper amzAdvReportAsinsSDMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportAsinsSD> list = new LinkedList<AmzAdvReportAsinsSD>();
			try {
				jsonReader.startArray();
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportAsinsSD amzAdvReportAsins = new AmzAdvReportAsinsSD();
					amzAdvReportAsins.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportAsins.setBydate(request.getStartDate());
					}else {
						amzAdvReportAsins.setBydate(date);
					}
					amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
					String asin = item.getString("asin");
					if(StrUtil.isEmpty(asin)){
						continue;
					}

					amzAdvReportAsins.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportAsins.setAdgroupid(item.getBigInteger("adGroupId"));
					amzAdvReportAsins.setAsin(asin);
					amzAdvReportAsins.setOtherasin(item.getString("otherAsin"));
					amzAdvReportAsins.setSku(item.getString("sku"));
		 
					amzAdvReportAsins.setProfileid(request.getProfileid());
					amzAdvReportAsins.setAttributedSales14dOtherSKU(item.getBigDecimal("attributedSales14dOtherSKU"));
					amzAdvReportAsins.setAttributedSales1dOtherSKU(item.getBigDecimal("attributedSales1dOtherSKU"));
					amzAdvReportAsins.setAttributedSales30dOtherSKU(item.getBigDecimal("attributedSales30dOtherSKU"));
					amzAdvReportAsins.setAttributedSales7dOtherSKU(item.getBigDecimal("attributedSales7dOtherSKU"));

					amzAdvReportAsins.setAttributedUnitsOrdered14dOtherSKU(item.getInteger("attributedUnitsOrdered14dOtherSKU"));
					amzAdvReportAsins.setAttributedUnitsOrdered1dOtherSKU(item.getInteger("attributedUnitsOrdered1dOtherSKU"));
					amzAdvReportAsins.setAttributedUnitsOrdered30dOtherSKU(item.getInteger("attributedUnitsOrdered30dOtherSKU"));
					amzAdvReportAsins.setAttributedUnitsOrdered7dOtherSKU(item.getInteger("attributedUnitsOrdered7dOtherSKU"));
					
					list.add(amzAdvReportAsins);
					if (list.size() >= 2000) {
						amzAdvReportAsinsSDMapper.insertBatch(list);
						list.clear();
					}
				}
				if (list.size() > 0) {
					amzAdvReportAsinsSDMapper.insertBatch(list);
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
