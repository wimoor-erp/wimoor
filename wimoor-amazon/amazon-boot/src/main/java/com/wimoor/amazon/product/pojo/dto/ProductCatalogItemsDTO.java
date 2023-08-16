package com.wimoor.amazon.product.pojo.dto;


import java.util.List;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="ProductCatalogItemsDTO对象", description="产品详情")
public class ProductCatalogItemsDTO {
	String groupid;
	String marketplaceid;
	List<String> marketplaceIds;
	List<String> identifiers;
	String identifiersType;
	List<String> includedData;
	String locale;
	String sellerId;
	List<String> keywords;
	List<String> brandNames;
	List<String> classificationIds;
	Integer pageSize;
	String pageToken;
	String keywordsLocale;
}
