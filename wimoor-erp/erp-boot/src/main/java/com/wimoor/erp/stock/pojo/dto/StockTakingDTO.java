package com.wimoor.erp.stock.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="StockTakingDTO对象", description="盘点单ItemDTO")
public class StockTakingDTO extends BasePageQuery{

	String search;
	
	String warehouseid;
	
	String shopid;
	
	String hasInv;
	
	String id;
	String materialid;
	String stocktakingid;
	String selected;
	String addressid;
}
