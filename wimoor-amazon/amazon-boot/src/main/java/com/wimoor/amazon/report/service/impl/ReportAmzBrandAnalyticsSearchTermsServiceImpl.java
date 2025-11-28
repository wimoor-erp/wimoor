package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.amazon.spapi.model.reports.ReportOptions;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;

 

@Service("reportAmzBrandAnalyticsSearchTermsService")
public class ReportAmzBrandAnalyticsSearchTermsServiceImpl extends ReportServiceImpl{

	@Resource
	private IMarketplaceService marketplaceService;
	
	public void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
          amazonAuthority.setUseApi("createReport");
          cstart.add(Calendar.DATE, -2);
          cend.add(Calendar.DATE, -2);
		  List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
		  for(Marketplace market:marketlist) {
			  CreateReportSpecification body=new CreateReportSpecification();
			  body.setReportType(myReportType());
			  body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
			  body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
			  ReportOptions reportOptions=getMyOptions();
			  if(reportOptions!=null) {
				body.setReportOptions(reportOptions);  
			  }
			  List<String> list=new ArrayList<String>();
			  list.add(market.getMarketplaceid());
			  amazonAuthority.setMarketPlace(market);
			  if(ignore==null||ignore==false) {
				  Map<String,Object> param=new HashMap<String,Object>();
				  param.put("sellerid", amazonAuthority.getSellerid());
				  param.put("reporttype", this.myReportType());
				  param.put("marketplacelist", list);
				  Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);  
				  if(lastupdate!=null&&GeneralUtil.distanceOfHour(lastupdate, new Date())<6) {
					  continue;
				  }
			  }
			  body.setMarketplaceIds(list);
			  callCreateAPI(this, body, amazonAuthority, market,cstart.getTime(),cend.getTime());
		  }
}

	//按区域申请的报表
	public String treatResponse(AmazonAuthority amazonAuthority,BufferedReader br)  {
		StringBuffer mlog = new StringBuffer();
		String line;
		Map<String,Integer> titleList= new HashMap<String,Integer>();
		try {
			try {
				while ((line = br.readLine()) != null) {
					 System.out.println(line);  
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				if(br!=null) {
				br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			titleList.clear();
			titleList=null;
	}
       return mlog.toString();
	}
	
	 
	@Override
	public String myReportType() {
		// TODO Auto-generated method stub
		return ReportType.BrandAnalyticsSearchTermsReport;
	}


	@Override
	public ReportOptions getMyOptions() {
		// TODO Auto-generated method stub
		ReportOptions reportOptions=new ReportOptions();
		reportOptions.put("reportPeriod", "DAY");
		return reportOptions;
	}

	
	
}
