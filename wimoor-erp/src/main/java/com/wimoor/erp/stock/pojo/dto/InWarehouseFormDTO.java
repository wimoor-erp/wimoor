package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InWarehouseFormDTO对象", description="入库单DTO")
public class InWarehouseFormDTO extends BasePageQuery{

	String search;
	
	String warehouseid;
	
	String fromDate;
	
	String toDate;
	
	String shopid;
}
