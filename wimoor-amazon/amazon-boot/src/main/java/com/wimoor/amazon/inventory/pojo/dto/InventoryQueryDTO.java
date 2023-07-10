package com.wimoor.amazon.inventory.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InventoryQuery对象", description="查询库存")
public class InventoryQueryDTO extends BasePageQuery{
 String materialid;
	String fType ;
	String groupid;
	String marketplaceid ;
	String sku;
	String conssku;
	String itemsku;
	String category ;
	String search;
	String ftypes;
}
