package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ChangeWarehouseFormDTO对象", description="互调单DTO")
public class ChangeWarehouseFormDTO extends BasePageQuery{
	String search;
	
	String fromDate;
	
	String toDate;
	
	String shopid;
}
