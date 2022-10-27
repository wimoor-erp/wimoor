package com.wimoor.amazon.orders.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.orders.GetOrderItemsResponse;
import com.amazon.spapi.model.orders.OrderItemsList;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.orders.pojo.entity.AmzOrderMain;
import com.wimoor.amazon.orders.service.IAmzOrderItemService;

public class ApiCallbackGetOrderItems implements ApiCallback<GetOrderItemsResponse>{
	 IAmzOrderItemService handlerService;
	 AmazonAuthority auth;
	 AmzOrderMain order;

	public ApiCallbackGetOrderItems(IAmzOrderItemService handlerService, AmazonAuthority auth, AmzOrderMain order) {
		// TODO Auto-generated constructor stub
		this.handlerService=handlerService;
		this.auth=auth;
		this.order=order;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		  auth.setApiRateLimit(responseHeaders, e);
	}

	@Override
	public void onSuccess(GetOrderItemsResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		  OrderItemsList items = result.getPayload();
		  String nexttoken=null;
		  if(items.getNextToken()!=null) {
			  nexttoken=items.getAmazonOrderId()+":"+items.getNextToken();
		  } 
		  auth.setApiRateLimit(responseHeaders,nexttoken);
		  handlerService.handlerOrderItemListResponse(auth,order,items);
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
