package com.wimoor.erp.inventory.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InventoryValueReportDTO对象", description="查询库存货值")
public class InventoryValueReportDTO extends BasePageQuery{
	String sku ;
	String byday;
	String warehouseid;
}
