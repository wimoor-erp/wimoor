package com.wimoor.erp.purchase.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseFinanceListDTO对象", description="获取采购请款列表")
public class PurchaseFinanceListDTO extends BasePageQuery{

	
	String supplierid;
	
	String search;
	
	String searchtype;
	
	String fromDate;
	
	String toDate;
	
	String paymethod;
	
	String status;
	
	String remark;
}
