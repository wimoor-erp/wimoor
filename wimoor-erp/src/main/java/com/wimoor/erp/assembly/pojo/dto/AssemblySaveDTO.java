package com.wimoor.erp.assembly.pojo.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AssemblySaveDTO对象", description="组装单保存")
public class AssemblySaveDTO {

	String assList;
	
	String planwarehouseid;
	
	String warehouseid;
	
	String remark;
	
	String number;
	
	String ftype;
	
	String orderitemlist;
	
	String id;
	
	String materialid;
	
	String amount;
	
	String subAssList;
	
}
