package com.wimoor.amazon.product.service;


import java.util.List;

import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.listings.Item;
import com.amazon.spapi.model.listings.ListingsItemSubmissionResponse;
import com.squareup.okhttp.Call;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;

public interface IProductListingsItemService extends IRunAmazonService{

	Item captureListMatchingProduct(AmazonAuthority amazonAuthority, String sku, List<String> marketplaces,String issueLocale, List<String> includedData );

	Call captureListMatchingProductSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketList);

	public void handlerResult(Item result, AmazonAuthority amazonAuthority,AmzProductRefresh mrefresh) ;

	void handlerFailure(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, ApiException e);
	
	void stopTask();

	void runTask();

	ProductInfo pushAsin(ProductListingItemDTO dto);

	Object deleteAsin(ProductListingItemDTO dto);

	ProductInfo saveAsin(ProductListingItemDTO dto);
	
	public ListingsItemSubmissionResponse changePriceSku(ProductListingItemDTO dto);
	
}
