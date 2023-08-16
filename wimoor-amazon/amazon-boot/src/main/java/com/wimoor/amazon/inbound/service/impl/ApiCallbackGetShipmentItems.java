package com.wimoor.amazon.inbound.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.fulfillmentinbound.GetShipmentItemsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.service.IFulfillmentInboundService;

public class ApiCallbackGetShipmentItems implements ApiCallback<GetShipmentItemsResponse> {
	 IFulfillmentInboundService iFulfillmentInboundService;
	 AmazonAuthority auth;
	 Marketplace market;
	 ShipInboundShipment shipment; 
	public ApiCallbackGetShipmentItems(IFulfillmentInboundService iFulfillmentInboundService,
			AmazonAuthority auth, Marketplace market, ShipInboundShipment shipment) {
		// TODO Auto-generated constructor stub
		this.iFulfillmentInboundService=iFulfillmentInboundService;
		this.auth=auth;
		this.market=market;
		this.shipment=shipment;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		auth.setApiRateLimit(responseHeaders, e);
	}

	@Override
	public void onSuccess(GetShipmentItemsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		String nexttoken=null;
		if(result!=null&&result.getPayload()!=null) {
			nexttoken=result.getPayload().getNextToken();
		}
		auth.setApiRateLimit(responseHeaders, nexttoken);
		boolean needshipqty=false;
		if(this.shipment!=null) {
			needshipqty=true;
		}
		iFulfillmentInboundService.handlerItemResult(auth,market,result,shipment,needshipqty);
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
