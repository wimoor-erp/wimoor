package com.wimoor.erp.assembly.pojo.dto;

import java.util.Date;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AssemblyFormListDTO对象", description="组装单列表查询")
public class AssemblyFormListDTO extends BasePageQuery{
	String search ;
	String ftype ;
	String searchtype;
	String auditstatus;
	String operate;
	String warehouseid;
	String shopid;
	String supplier;
	String owner;
	Date fromDate;
	Date toDate;
}
