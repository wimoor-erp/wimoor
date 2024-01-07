package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PaymentSaveDTO对象", description="采购单付款")
public class PaymentReportDTO extends BasePageQuery{
	
	  
		@ApiModelProperty(value = "仓库ID", example = "123456")
		String warehouseid ;
		
		@ApiModelProperty(value = "SKU，订单编码查询", example = "TKK123456")
		String search ;
		
		@ApiModelProperty(value = "查询类型[sku,number]", example = "sku")
		String searchtype ;
		
		@ApiModelProperty(value = "查询类型[paydate,recdate]", example = "sku")
		String datetype ;
		
		@ApiModelProperty(value = "开始日期", example = "2022-01-01")
		String fromDate ;

		@ApiModelProperty(value = "结束日期", example = "2022-01-01")
		String toDate ;
		
		String paymethod;
		
		String projectid;
	 
		String supplierid;
		
		String settlementid;
}
