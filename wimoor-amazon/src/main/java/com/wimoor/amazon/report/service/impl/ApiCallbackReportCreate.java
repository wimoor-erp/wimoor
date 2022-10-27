package com.wimoor.amazon.report.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.reports.CreateReportResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.report.service.IReportService;

public class ApiCallbackReportCreate  implements ApiCallback<CreateReportResponse>{
	AmazonAuthority amazonAuthority=null;
	IReportService reportService=null;
	Date start=null;
	Date end=null;
	Marketplace market;
	/**
	 * 创建时需要带market对象是因为auth对象中得market对因为构造申请参数时得循环而变化，但是请求返回的先后次序不受控制
	 * 但是抓取订单的时候没有这个问题，是因为抓取订单的每一个auth都是单独构造的，一个请求record对应单独的auth对象和market对象，不会发生变化，所以也不受请求先后次序不受控的问题影响
	 * @param rpt
	 * @param auth
	 * @param market
	 * @param start
	 * @param end
	 */
	ApiCallbackReportCreate(IReportService rpt,AmazonAuthority auth,Marketplace market, Date start,Date end){
		 this.amazonAuthority=auth;
		 this.reportService=rpt;
		 this.start=start;
		 this.end=end;
		 this.market=market;
	}
	@Override
	public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		//System.out.println("ApiCallbackReportCreate----onDownloadProgress");
	}

	@Override
	public void onFailure(ApiException arg0, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		//System.out.println("ApiCallbackReportCreate----onFailure"+arg0.getResponseBody());
		//System.out.println("ApiCallbackReportCreate----onFailure"+arg2);
		amazonAuthority.setApiRateLimit(arg2,arg0);
	}

	@Override
	public void onSuccess(CreateReportResponse result, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		String token="";
		amazonAuthority.setApiRateLimit( arg2,token);
		if(amazonAuthority!=null&&result!=null) {
			if(result!=null) {
				reportService.createRecordRequest(amazonAuthority,result.getReportId(),market,start,end);
			}
		}
	}

	@Override
	public void onUploadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		//System.out.println("ApiCallbackReportCreate----onUploadProgress");
	}

}
