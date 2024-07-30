package com.wimoor.amazon.finances.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="FinDataMonthDTO对象", description="查询SKU自定义财务费用")
public class FinDataMonthDTO extends BasePageQuery{
	String itemid ;
	String marketplaceid ;
	String groupid ;
	String fromDate ;
	String endDate;
	String sku ;
	String ftype;
	String isdeep;
	String progresskey;
	String shopid;
}
