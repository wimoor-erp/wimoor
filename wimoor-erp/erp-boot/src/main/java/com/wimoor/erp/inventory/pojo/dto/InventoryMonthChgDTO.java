package com.wimoor.erp.inventory.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InventoryMonthChgDTO对象", description="查询库存周转")
public class InventoryMonthChgDTO extends BasePageQuery{
	String warehouseid;
	String sku;
	String sumtype;
	String monthDate;
	String materialid;
}
