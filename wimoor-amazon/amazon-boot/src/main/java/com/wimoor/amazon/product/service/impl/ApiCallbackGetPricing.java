package com.wimoor.amazon.product.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.productpricing.GetPricingResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IProductProductPriceService;

public class ApiCallbackGetPricing implements ApiCallback<GetPricingResponse> {
	IProductProductPriceService iProductCaptureProductPriceService;
	AmazonAuthority amazonAuthority;
	AmzProductRefresh amzProductRefresh;
	
	public ApiCallbackGetPricing(IProductProductPriceService iProductCaptureProductPriceService,
			AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh) {
		// TODO Auto-generated constructor stub
		this.iProductCaptureProductPriceService=iProductCaptureProductPriceService;
		this.amazonAuthority=amazonAuthority;
		this.amzProductRefresh=amzProductRefresh;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit( responseHeaders, e);
		iProductCaptureProductPriceService.handlerFailure(amazonAuthority,amzProductRefresh,e);
	}

	@Override
	public void onSuccess(GetPricingResponse result, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(responseHeaders, "");
		iProductCaptureProductPriceService.handlerResult(result,amazonAuthority,amzProductRefresh.getMarketplaceid());
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
