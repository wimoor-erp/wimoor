package com.wimoor.amazon.inbound.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;

public class ApiCallbackGetShipments implements ApiCallback<GetShipmentsResponse> {
    
	 IFulfillmentInboundService iFulfillmentInboundService;
	 AmazonAuthority auth;
	 Marketplace market;
	
	public ApiCallbackGetShipments(IFulfillmentInboundService iFulfillmentInboundService, AmazonAuthority auth,
			Marketplace market) {
		// TODO Auto-generated constructor stub
		this.iFulfillmentInboundService=iFulfillmentInboundService;
		this.auth=auth;
		this.market=market;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		auth.setApiRateLimit(responseHeaders, e);
	}

	@Override
	public void onSuccess(GetShipmentsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		String nexttoken=null;
		if(result!=null&&result.getPayload()!=null) {
			nexttoken=result.getPayload().getNextToken();
		}
		auth.setApiRateLimit( responseHeaders, nexttoken);
		iFulfillmentInboundService.handlerResult(auth,market,result);
	}

	@Override
	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
		// TODO Auto-generated method stub

	}

}
