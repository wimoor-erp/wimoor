package com.wimoor.amazon.auth.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.sellers.GetMarketplaceParticipationsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonSellerMarketService;

public class ApiCallbackGetMarketplaceParticipations implements ApiCallback<GetMarketplaceParticipationsResponse> {
	IAmazonSellerMarketService iAmazonSellerMarketService;
	AmazonAuthority amazonAuthority;
	public ApiCallbackGetMarketplaceParticipations(IAmazonSellerMarketService iAmazonSellerMarketService, AmazonAuthority amazonAuthority) {
		// TODO Auto-generated constructor stub
		this.iAmazonSellerMarketService=iAmazonSellerMarketService;
		this.amazonAuthority=amazonAuthority;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(responseHeaders, e);
	}

	@Override
	public void onSuccess(GetMarketplaceParticipationsResponse result, int statusCode,
			Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(responseHeaders, "");
		iAmazonSellerMarketService.handler(amazonAuthority, result);
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
