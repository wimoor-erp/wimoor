package com.wimoor.amazon.product.pojo.dto;

import com.amazon.spapi.model.listings.ListingsItemPatchRequest;
import com.amazon.spapi.model.listings.ListingsItemPutRequest;
import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductListDTO对象", description="获取商品列表")
public class ProductListingPushDTO extends BasePageQuery{
	@ApiModelProperty(value = "站点")
	List<String> marketplaceids;
 
	@ApiModelProperty(value = "authid[自动填充]")
	String amazonauthid;
 
	@ApiModelProperty(value = "groupid")
	String groupid;

	String userid;
	String sellerid;

	String issueLocale;
	List<String> asins;
    String sku;
	String productType;
	private Object attributes;
	ListingsItemPatchRequest patchBody;
}
