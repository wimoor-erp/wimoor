package com.wimoor.amazon.product.service;

import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.amazon.spapi.model.definitions.ProductTypeList;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.product.pojo.dto.DefinitionsProductTypesDTO;

public interface IProductProductTypeService extends IRunAmazonService{
	public void captureProductType(AmazonAuthority amazonAuthority,String  marketplaceid) ;
	public ProductTypeList searchDefinitionsProductTypes(AmazonAuthority amazonAuthority, DefinitionsProductTypesDTO dto);
	ProductTypeDefinition captureTypeDefinition(AmazonAuthority amazonAuthority, DefinitionsProductTypesDTO dto);
}
