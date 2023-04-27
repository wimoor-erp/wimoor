package com.wimoor.erp.purchase.pojo.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PurchaseFormDownloadDTO对象", description="采购单下载")
public class PurchaseFormDownloadDTO {

	String number;
	
	String warehouseid;
	
	String supplierid;
	
	String buyerName;
	
	String buyerDate;
	
	String totalprice;
	
	String remark;
	
	String creator;
	
	String shopid;
	
	String formid;
	
	
}
