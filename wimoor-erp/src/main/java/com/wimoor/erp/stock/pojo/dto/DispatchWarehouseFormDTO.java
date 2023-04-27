package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DispatchWarehouseFormDTO对象", description="调库单DTO")
public class DispatchWarehouseFormDTO extends BasePageQuery{

	String search;
	
	String auditstatus;
	
	String fromDate;
	
	String toDate;
	
	String shopid;
	
	String fromwid;
	
	String towid;
}
