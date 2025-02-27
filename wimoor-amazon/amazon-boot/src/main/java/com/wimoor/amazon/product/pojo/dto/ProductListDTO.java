package com.wimoor.amazon.product.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductListDTO对象", description="获取商品列表")
public class ProductListDTO extends BasePageQuery{

	@ApiModelProperty(value = "仓库ID")
	String warehouseid;
	
	@ApiModelProperty(value = "查询列表类型[shipment和非shipment]")
	String ftype;
	
	@ApiModelProperty(value = "站点")
	String marketplaceid;
	
	@ApiModelProperty(value = "公司ID[自动填充]")
	String shopid;
	
	@ApiModelProperty(value = "店铺ID")
	String groupid;
	
	@ApiModelProperty(value = "authid[自动填充]")
	String amazonauthid;
	
	@ApiModelProperty(value = "sellerid")
	String sellerid;
	
	@ApiModelProperty(value = "查询内容")
	String search;
	
	@ApiModelProperty(value = "查询内容")
	String searchType;
	
	
}
