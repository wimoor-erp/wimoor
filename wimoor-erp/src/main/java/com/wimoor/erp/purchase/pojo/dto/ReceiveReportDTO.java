package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ReceiveReportDTO对象", description="获取采购入库明细列表")
public class ReceiveReportDTO extends BasePageQuery{
  
	@ApiModelProperty(value = "仓库ID", example = "123456")
	String warehouseid ;
	
	@ApiModelProperty(value = "SKU，订单编码查询", example = "TKK123456")
	String search ;
	
	@ApiModelProperty(value = "查询类型[sku,number]", example = "sku")
	String searchtype ;
	
	@ApiModelProperty(value = "开始日期", example = "2022-01-01")
	String fromDate ;

	@ApiModelProperty(value = "结束日期", example = "2022-01-01")
	String toDate ;
	 
}
