package com.wimoor.erp.purchase.pojo.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PurchaseSaveDTO对象", description="采购单保存")
public class PurchaseSaveDTO {

	String planwarehouseid;
	
	String warehouseid;
	
	String ftype;
	
	String orderitemlist;
	
	String remark;
	
	String assList;
	
	String purchaser;
	
	String formid;
	
	String groupid;
	
}
