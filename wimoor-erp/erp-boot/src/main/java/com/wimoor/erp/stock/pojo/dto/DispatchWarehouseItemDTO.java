package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DispatchWarehouseItemDTO对象", description="调库单保存/提交DTO")
public class DispatchWarehouseItemDTO extends BasePageQuery{

	String id;
	
	String action;
	
	String fromwarehouseid;
	
	String towarehouseid;
	
	String towarehousename;
	
	String arrivaltime;
	
	Integer ftype;
	
	String remark;
	
	String skumapstr;
	
	String planitemidstr;
	
	String dispatchList;
}
