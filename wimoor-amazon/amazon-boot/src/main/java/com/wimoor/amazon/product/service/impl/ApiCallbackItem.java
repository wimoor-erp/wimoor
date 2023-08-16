package com.wimoor.amazon.product.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.listings.Item;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IProductListingsItemService;

public class ApiCallbackItem implements ApiCallback<Item> {
	IProductListingsItemService productCaptureService;
	AmazonAuthority amazonAuthority;
	AmzProductRefresh amzProductRefresh;
	public ApiCallbackItem(IProductListingsItemService productCaptureService, AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh) {
		// TODO Auto-generated constructor stub
		this.productCaptureService=productCaptureService;
		this.amazonAuthority=amazonAuthority;
		this.amzProductRefresh=amzProductRefresh;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
           amazonAuthority.setApiRateLimit(responseHeaders, e);
           productCaptureService.handlerFailure(amazonAuthority,amzProductRefresh,e);
	}

	@Override
	public void onSuccess(Item result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
	    amazonAuthority.setApiRateLimit(responseHeaders, "");
		productCaptureService.handlerResult(result,amazonAuthority,this.amzProductRefresh);
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
