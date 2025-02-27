package com.wimoor.amazon.finances.service.impl;

import java.util.List;
import java.util.Map;

import com.amazon.spapi.client.ApiCallback;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.finances.ListFinancialEventGroupsResponse;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.finances.service.IAmzFinAccountService;

public class ApiCallbackListFinancialEventGroupsEvents implements ApiCallback<ListFinancialEventGroupsResponse> {
	IAmzFinAccountService amzFinAccountService;
	AmazonAuthority amazonAuthority;
	
	public ApiCallbackListFinancialEventGroupsEvents(IAmzFinAccountService amzFinAccountService, AmazonAuthority amazonAuthority) {
		// TODO Auto-generated constructor stub
		this.amzFinAccountService=amzFinAccountService;
		this.amazonAuthority=amazonAuthority;
	}

	@Override
	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		amazonAuthority.setApiRateLimit(responseHeaders, e);
	}

	@Override
	public void onSuccess(ListFinancialEventGroupsResponse result, int statusCode,
			Map<String, List<String>> responseHeaders) {
		// TODO Auto-generated method stub
		String nexttoken="";
		if(result!=null&&result.getPayload()!=null&&result.getPayload().getNextToken()!=null) {
			nexttoken=result.getPayload().getNextToken();
		}
		amazonAuthority.setApiRateLimit(responseHeaders, nexttoken);
	    amzFinAccountService.handler(amazonAuthority,result);
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
