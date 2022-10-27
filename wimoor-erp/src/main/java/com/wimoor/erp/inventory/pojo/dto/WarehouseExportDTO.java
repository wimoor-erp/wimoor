package com.wimoor.erp.inventory.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="WarehouseExportDTO对象", description="查询库存")
public class WarehouseExportDTO {
	String fType;
	String groupid ;
	String warehouseid ;
	String sku ;
	String conssku;
	String itemsku;
	String category ;
	String ftypes;
}
