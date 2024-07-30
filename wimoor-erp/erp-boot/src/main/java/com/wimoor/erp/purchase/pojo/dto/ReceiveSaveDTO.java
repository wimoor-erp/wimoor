package com.wimoor.erp.purchase.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PaymentSaveDTO对象", description="采购单付款")
public class ReceiveSaveDTO {
	
	String warehouseid;
	
	String remark;
	
	String status;
	
	String entryid;
	
	String recid;
	
	String amount;
	
	String ftype;
}
