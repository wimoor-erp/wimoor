package com.wimoor.amazon.orders.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmazonOrdersShipDTO对象", description="获取订单发货跟踪列表")
public class AmazonOrdersShipDTO extends BasePageQuery{

	@ApiModelProperty(value = "开始日期")
	String startDate;
	
	@ApiModelProperty(value = "结束日期")
	String endDate;
	
	@ApiModelProperty(value = "站点ID")
	String marketplaceid;
	
	@ApiModelProperty(value = "产品颜色")
	String color;
	
	@ApiModelProperty(value = "店铺ID")
	String groupid;
	
	@ApiModelProperty(value = "查询类型[sku,asin,number]")
	String searchtype;
	
	@ApiModelProperty(value = "查询内容")
	String search;
	
	
	
	
	
	
	
	
}
