package com.wimoor.amazon.orders.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmazonOrdersRemoveDTO对象", description="获取订单移除列表")
public class AmazonOrdersRemoveDTO extends BasePageQuery{
	@ApiModelProperty(value = "开始日期")
	String startDate;
	
	@ApiModelProperty(value = "结束日期")
	String endDate;
	
	@ApiModelProperty(value = "授权ID")
	String amazonauthid;
	
	@ApiModelProperty(value = "店铺ID")
	String groupid;
	
	@ApiModelProperty(value = "sellerID")
	String sellerid;
	
	@ApiModelProperty(value = "查询类型[sku,asin,number]")
	String searchtype;

	@ApiModelProperty(value = "查询日期类型[purchase,updates]")
	String datetype;

	@ApiModelProperty(value = "查询sku")
	String sku;
	
	@ApiModelProperty(value = "查询内容")
	String search;
	
	@ApiModelProperty(value = "系统填写")
	String owner;


	
}
