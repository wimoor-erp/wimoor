package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InWarehouseItemDTO对象", description="入库单保存的DTO")
public class InWarehouseItemDTO extends BasePageQuery{

	String id;
	
	String warehouseid;
	
	String remark;
	
	String skumapstr;
}
