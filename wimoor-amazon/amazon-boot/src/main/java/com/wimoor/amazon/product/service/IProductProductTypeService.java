package com.wimoor.amazon.product.service;

import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;

public interface IProductProductTypeService extends IRunAmazonService{
	public void captureProductType(AmazonAuthority amazonAuthority,String  marketplaceid) ;

	ProductTypeDefinition captureTypeDefinition(AmazonAuthority amazonAuthority, String marketplaceid,String typename);
}
