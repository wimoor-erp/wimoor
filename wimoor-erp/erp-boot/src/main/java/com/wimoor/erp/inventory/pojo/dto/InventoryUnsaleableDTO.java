package com.wimoor.erp.inventory.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InventoryQuery对象", description="查询库存")
public class InventoryUnsaleableDTO extends BasePageQuery{
    String days;
    String search;
	String warehouseid ;
	 
}
