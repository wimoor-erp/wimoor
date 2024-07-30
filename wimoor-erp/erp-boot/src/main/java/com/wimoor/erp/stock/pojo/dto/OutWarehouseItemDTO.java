package com.wimoor.erp.stock.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OutWarehouseItemDTO对象", description="出库单保存的DTO")
public class OutWarehouseItemDTO extends BasePageQuery{

	
	String id;
	
	String warehouseid;
	
	String purchaser;
	
	String toaddress;
	
	String express;
	
	String expressno;
	
	String remark;
	
	String skumapstr;
	
	String customer;
	
	Integer ftype;
}
