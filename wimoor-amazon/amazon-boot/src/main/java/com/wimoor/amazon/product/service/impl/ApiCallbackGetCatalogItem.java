package com.wimoor.amazon.product.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IProductCatalogItemService;

public class ApiCallbackGetCatalogItem implements ApiCallback<Item> {
	
	IProductCatalogItemService iProductCaptureCatalogItemService;
	AmazonAuthority auth;
	AmzProductRefresh skuRefresh;
	 
	
	public ApiCallbackGetCatalogItem(IProductCatalogItemService iProductCaptureCatalogItemService, AmazonAuthority auth,AmzProductRefresh skuRefresh) {
		// TODO Auto-generated constructor stub
		this.iProductCaptureCatalogItemService=iProductCaptureCatalogItemService;
		this.auth=auth;
		this.skuRefresh=skuRefresh;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		//Forbidden 被禁用
  	  auth.setApiRateLimit(responseHeaders, e);
  	 this.iProductCaptureCatalogItemService.handlerFailure(this.auth,skuRefresh,e);
	}

	@Override
	public void onSuccess(Item result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
	  	auth.setApiRateLimit(responseHeaders, "");
        this.iProductCaptureCatalogItemService.handlerResult(this.auth,skuRefresh,result);
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
