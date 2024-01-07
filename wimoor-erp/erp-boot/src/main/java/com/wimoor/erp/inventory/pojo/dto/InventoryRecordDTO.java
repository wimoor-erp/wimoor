package com.wimoor.erp.inventory.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InventoryRecordDTO对象", description="查询库存明细")
public class InventoryRecordDTO extends BasePageQuery{
		String warehouseid ;
		String operator;
		String search;
		String searchtype;
		List<String> formtype;
		String fromDate;
		String toDate;
}
