package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="StockTakingFormDTO对象", description="盘点单DTO")
public class StockTakingFormDTO extends BasePageQuery{

	String search;
	
	String fromDate;
	
	String toDate;
	
	String warehouseid;
	
	String groupid;
	
	String shopid;
	
	String isworking;
	

}
