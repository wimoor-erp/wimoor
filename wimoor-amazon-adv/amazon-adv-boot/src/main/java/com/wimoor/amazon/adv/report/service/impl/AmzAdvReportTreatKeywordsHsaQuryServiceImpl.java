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
import com.wimoor.amazon.adv.sb.dao.AmzAdvReportKeywordsQueryHsaMapper;
import com.wimoor.amazon.adv.sb.pojo.AmzAdvReportKeywordsQueryHsa;
import com.wimoor.common.GeneralUtil;
@Service("amzAdvReportTreatKeywordsHsaQuryService")
public class AmzAdvReportTreatKeywordsHsaQuryServiceImpl extends AmzAdvReportTreatServiceImpl implements IAmzAdvReportTreatService{
	@Resource
	AmzAdvReportKeywordsQueryHsaMapper amzAdvReportKeywordsQueryHsaMapper;
	
	@Override
	public synchronized void treatReport(AmzAdvProfile profile,AmzAdvRequest request, JSONReader jsonReader) {
		// TODO Auto-generated method stub
			final List<AmzAdvReportKeywordsQueryHsa> list = new LinkedList<AmzAdvReportKeywordsQueryHsa>();
	 
			try {
				jsonReader.startArray();
				int line = 0;
				while (jsonReader.hasNext()) {
					String elem = jsonReader.readString();
					JSONObject item = GeneralUtil.getJsonObject(elem);
					AmzAdvReportKeywordsQueryHsa amzAdvReportKeywordsQueryHsa = new AmzAdvReportKeywordsQueryHsa();
			 
					amzAdvReportKeywordsQueryHsa.setProfileid(request.getProfileid());
					Date date = item.getDate("date");
					if(date==null) {
						amzAdvReportKeywordsQueryHsa.setBydate(request.getStartDate());
					}else {
						amzAdvReportKeywordsQueryHsa.setBydate(date);
					}
					amzAdvReportKeywordsQueryHsa.setCampaignid(item.getBigInteger("campaignId"));
					amzAdvReportKeywordsQueryHsa.setKeywordid(item.getBigInteger("keywordId"));
					amzAdvReportKeywordsQueryHsa.setAdgroupid(item.getBigInteger("adGroupId"));
//					amzAdvReportKeywordsQueryHsa.setKeywordtext(item.getString("keywordText"));
//					amzAdvReportKeywordsQueryHsa.setAdgroupname(item.getString("adGroupName"));
//					amzAdvReportKeywordsQueryHsa.setCampaignname(item.getString("campaignName"));
//					amzAdvReportKeywordsQueryHsa.setMatchtype(item.getString("matchType"));
					amzAdvReportKeywordsQueryHsa.setOpttime(new Date());
					String query = item.getString("query");
					AmzAdvRptQuery queryobj = this.loadQuery(query);
					if(queryobj==null) {
						continue;
					}
					amzAdvReportKeywordsQueryHsa.setQueryid(new BigInteger(queryobj.getId()));
					amzAdvReportKeywordsQueryHsa.setClicks(item.getInteger("clicks"));
					amzAdvReportKeywordsQueryHsa.setImpressions(item.getInteger("impressions"));
					amzAdvReportKeywordsQueryHsa.setCost(item.getBigDecimal("cost"));

					amzAdvReportKeywordsQueryHsa.setAttributedsales14d(item.getBigDecimal("attributedSales14d"));
					amzAdvReportKeywordsQueryHsa.setAttributedconversions14d(item.getInteger("attributedConversions14d"));
			        
					list.add(amzAdvReportKeywordsQueryHsa);
					if (list.size() >= 2000) {
						amzAdvReportKeywordsQueryHsaMapper.insertBatch(list); 
						list.clear();
					}
					 
				
					line++;
				}
				if (list.size() > 0) {
					amzAdvReportKeywordsQueryHsaMapper.insertBatch(list);
				}
			 
				saveAmzAdvSumAllType(line, request);
			} catch(Exception e) {
				e.printStackTrace();
			}  
		}
		

	 
}
