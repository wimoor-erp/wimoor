package com.wimoor.amazon.product.service;


import java.util.List;

import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.catalogitems.Item;
import com.amazon.spapi.model.catalogitems.ItemSearchResults;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.dto.ProductCatalogItemsDTO;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;

public interface IProductCatalogItemService extends IRunAmazonService{

	void captureCatalogProductSync(AmazonAuthority amazonAuthority, AmzProductRefresh amzProductRefresh, List<String> marketList);
	public Item captureCatalogProduct(AmazonAuthority auth, AmzProductRefresh skuRefresh, List<String> market);
	void handlerResult(AmazonAuthority auth, AmzProductRefresh skuRefresh, com.amazon.spapi.model.catalogitems.Item result);
	void handlerFailure(AmazonAuthority auth, AmzProductRefresh skuRefresh, ApiException e);
	void stopTask();
	void runTask();
	public Item captureCatalogProductDim(AmazonAuthority auth,String asin, List<String> market);
	public Item captureCatalogProduct(AmazonAuthority auth,String asin, List<String> market, List<String> includedData);
	public ItemSearchResults searchCatalogProducts(AmazonAuthority auth,ProductCatalogItemsDTO dto) ;
	public List<String> captureCatalogProductChildren(AmazonAuthority auth, String asin, List<String> market) ;
	public List<Item> captureCatalogProduct( AmazonAuthority auth, List<String> asins, List<String> market );

 
}
