package com.wimoor.amazon.product.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductListDTO对象", description="获取商品列表")
public class ProductListingItemDTO extends BasePageQuery{

	@ApiModelProperty(value = "sku")
	String sku;
	
	@ApiModelProperty(value = "asin")
	String asin;

	@ApiModelProperty(value = "price")
	String price;
	
	@ApiModelProperty(value = "lowestprice")
	String lowestprice;
	
	@ApiModelProperty(value = "filfullable")
	String filfullable;
	
	String maxOrderQuantity;
	String merchantShippingGroup;
	String leaddays;
	
	String image;
	
	@ApiModelProperty(value = "站点")
	List<String> marketplaceids;
 
	@ApiModelProperty(value = "authid[自动填充]")
	String amazonauthid;
 
	@ApiModelProperty(value = "groupid[自动填充]")
	String groupid;
 
	@ApiModelProperty(value = "删除专用")
	String pid;
	
	String fromDate;
	
	String toDate;
	
	String userid;
	String sellerid;
	List<String> asins;
}
