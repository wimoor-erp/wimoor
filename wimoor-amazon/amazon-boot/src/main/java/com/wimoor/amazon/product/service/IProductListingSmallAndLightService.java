package com.wimoor.amazon.product.service;

import java.util.List;

import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;

public interface IProductListingSmallAndLightService {

	void refreshSmallAndLight(AmazonAuthority auth, String sku, List<String> marketplaces);

}
