package com.wimoor.amazon.product.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IProductCaptureCatalogItemService;

public class ApiCallbackGetCatalogItem implements ApiCallback<Item> {
	
	IProductCaptureCatalogItemService iProductCaptureCatalogItemService;
	AmazonAuthority auth;
	AmzProductRefresh skuRefresh;
	 
	
	public ApiCallbackGetCatalogItem(IProductCaptureCatalogItemService iProductCaptureCatalogItemService, AmazonAuthority auth,AmzProductRefresh skuRefresh) {
		// TODO Auto-generated constructor stub
		this.iProductCaptureCatalogItemService=iProductCaptureCatalogItemService;
		this.auth=auth;
		this.skuRefresh=skuRefresh;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		//Forbidden 被禁用
      e.printStackTrace();
  	  auth.setApiRateLimit("getCatalogItem", responseHeaders, e);
	}

	@Override
	public void onSuccess(Item result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
	  	auth.setApiRateLimit("getCatalogItem", responseHeaders, "");
        this.iProductCaptureCatalogItemService.handlerCatalogItem(this.auth,skuRefresh,result);
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
