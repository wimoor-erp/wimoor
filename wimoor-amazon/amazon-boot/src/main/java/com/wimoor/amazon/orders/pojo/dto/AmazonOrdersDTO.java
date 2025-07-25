package com.wimoor.amazon.orders.pojo.dto;



import java.util.List;

import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmazonOrdersDTO对象", description="获取订单列表")
public class AmazonOrdersDTO extends BasePageQuery{

	@ApiModelProperty(value = "开始日期")
	String startDate;
	
	@ApiModelProperty(value = "结束日期")
	String endDate;
	
	@ApiModelProperty(value = "店铺ID[全选时为all]")
	String groupid;
	
	@ApiModelProperty(value = "AuthID[系统填充]")
	String amazonAuthId;
	
	@ApiModelProperty(value = "站点ID[系统填充]")
	String marketplaceid;
	
	@ApiModelProperty(value = "店铺List[系统填充]")
	List<AmazonGroup> groupList;
	
	@ApiModelProperty(value = "orderid[系统填充]")
	String orderid;
	
	@ApiModelProperty(value = "asin[系统填充]")
	String asin;
	
	@ApiModelProperty(value = "sku[系统填充]")
	String sku;
	
	@ApiModelProperty(value = "owner[系统填充]")
	String owner;
	
	@ApiModelProperty(value = "发货渠道,值:[Amazon,Merchant]")
	String channel;
	
	@ApiModelProperty(value = "订单状态[Unshipped,Shipped,Cancelled,Pending]")
	String status;
	
	@ApiModelProperty(value = "产品颜色")
	String color;
	
	@ApiModelProperty(value = "查询类型[sku,asin,number]")
	String searchtype;
	
	@ApiModelProperty(value = "站点[Amazon.ca,Amazon.com.....]")
	String pointname;
	
	@ApiModelProperty(value = "查询内容")
	String search;
	
	@ApiModelProperty(value = "客户类型['true','false']")
	String isbusiness;
	
	@ApiModelProperty(value = "公司ID[系统填充]")
	String shopid;
	
	@ApiModelProperty(value = "[系统填充]")
	String isarchive="false";
	
}
