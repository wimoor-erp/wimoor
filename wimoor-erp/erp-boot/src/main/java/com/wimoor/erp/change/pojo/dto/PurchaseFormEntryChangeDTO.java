package com.wimoor.erp.change.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PurchaseFormEntryChangeDTO对象", description="换货单dto")
public class PurchaseFormEntryChangeDTO extends BasePageQuery{

	String entryid;
	
	String  supplierid;
	
	String id;
	
	String warehouseid;
	
	String remark;
	
	String skumapstr;
	
}
