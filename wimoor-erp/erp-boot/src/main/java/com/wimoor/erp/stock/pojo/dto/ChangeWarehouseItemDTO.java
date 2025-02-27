package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ChangeWarehouseItemDTO对象", description="互调单保存/提交DTO")
public class ChangeWarehouseItemDTO extends BasePageQuery{

	String id;
	
	String warehouseid;
	
	String remark;
	
	String skumapstr;
}
