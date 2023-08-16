package com.wimoor.erp.purchase.pojo.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PaymentSaveDTO对象", description="采购单付款")
public class PaymentSaveDTO {
	
	String feelist;
	
	String paymethod;
	
	String payacc;
	
	String logisiter;
	
	String remark;
	
	String status;
	
	Date deliverydate;
	
	String deliverydatestr;
	
	String entryid;
	
	String payid;
	
	String costamount;
	
	String shipamount;
	
	String paytype;
}
