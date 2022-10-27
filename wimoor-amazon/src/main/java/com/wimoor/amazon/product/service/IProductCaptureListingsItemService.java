package com.wimoor.amazon.product.service;


import java.util.List;

import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.listings.Item;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;

public interface IProductCaptureListingsItemService extends IRunAmazonService{

	Item captureListMatchingProduct(AmazonAuthority amazonAuthority, String sku, List<String> marketList);

	Call captureListMatchingProductSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketList);

	void handlerResult(Item result, AmazonAuthority amazonAuthority) ;

	void handlerFailure(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, ApiException e);

	void stopTask();

	void runTask();

 
	
}
