package com.wimoor.amazon.product.service;


import java.util.List;

import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.productpricing.GetOffersResponse;
import com.amazon.spapi.model.productpricing.GetPricingResponse;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;

public interface IProductProductPriceService extends IRunAmazonService{

	public GetPricingResponse captureProductPrice(AmazonAuthority amazonAuthority, String sku, String  marketplaceid) ;

	Call captureProductPriceSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketList);

	void handlerFailure(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, ApiException e);

	void runTask();

	void stopTask();

	void handlerResult(GetPricingResponse result, AmazonAuthority amazonAuthority, String marketplaceid);

	public GetOffersResponse getItemOffers(AmazonAuthority amazonAuthority, String asin, String  marketplaceid); 
	 
   void handleResultItemOffers(AmazonAuthority amazonAuthority, String asin, String  marketplaceid, GetOffersResponse response);
	
}
