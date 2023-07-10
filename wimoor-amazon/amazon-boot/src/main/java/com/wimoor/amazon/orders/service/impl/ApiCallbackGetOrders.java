package com.wimoor.amazon.orders.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.GetOrdersResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmzAuthApiTimelimit;
import com.wimoor.amazon.orders.service.IAmzOrderMainService;

public class ApiCallbackGetOrders implements ApiCallback<GetOrdersResponse> {
	 IAmzOrderMainService handlerService;
	 AmazonAuthority auth;
	 AmzAuthApiTimelimit apilimit;
	 Date startDate;
	public ApiCallbackGetOrders(IAmzOrderMainService amazonOrdersService, AmazonAuthority auth,
			AmzAuthApiTimelimit apilimit,   Date startDate) {
		// TODO Auto-generated constructor stub
		this.handlerService=amazonOrdersService;
		this.auth=auth;
		this.startDate=startDate;
		this.apilimit=apilimit;
		
	}

	@Override
	public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
		//System.out.println("onDownloadProgress");
	}

	@Override
	public void onFailure(ApiException arg0, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		auth.setApiRateLimit(arg2, arg0);
	}

	@Override
	public void onSuccess(GetOrdersResponse res, int arg1, Map<String, List<String>> arg2) {
		// TODO Auto-generated method stub
		//System.out.println("onSuccess");
		auth.setApiRateLimit(arg2,startDate,res.getPayload().getNextToken());
		handlerService.handlerOrderResponse(auth,apilimit,res);
	}

	@Override
	public void onUploadProgress(long arg0, long arg1, boolean arg2) {
		// TODO Auto-generated method stub
             //System.out.println("onUploadProgress");
	}

}
