package com.wimoor.erp.order.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductPresaleListDTO对象", description="获取计划的商品列表")
public class PresaleListDTO extends BasePageQuery{
	String warehouseid;
	String shopid;
	String sku;
	String msku;
	String asin;
	String owner;
	Boolean needplan;
	String month;
	Date fromDate;
	Date toDate;
}
