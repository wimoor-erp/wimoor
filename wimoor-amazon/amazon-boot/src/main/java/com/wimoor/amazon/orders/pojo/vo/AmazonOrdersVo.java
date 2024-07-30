package com.wimoor.amazon.orders.pojo.vo;



import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AmazonOrdersVo对象", description="订单列表对象")
public class AmazonOrdersVo {
	@ApiModelProperty(value = "订单购买时间")
	Date buydate;
	
	@ApiModelProperty(value = "订单Item状态")
	String itemstatus;
	
	@ApiModelProperty(value = "订单状态")
	String orderstatus;
	
	@ApiModelProperty(value = "订单ID")
	String orderid;
	
	@ApiModelProperty(value = "店铺ID")
	String groupid;
	
	@ApiModelProperty(value = "销售渠道")
	String channel;
	
	@ApiModelProperty(value = "订单金额")
	BigDecimal orderprice;
	
	@ApiModelProperty(value = "订单单价[订单金额/订单商品数]")
	BigDecimal itemprice;
	
	@ApiModelProperty(value = "订单商品数量")
	Integer quantity;
	
	@ApiModelProperty(value = "商品图片")
	String image;
	
	@ApiModelProperty(value = "商品SKU")
	String sku;
	
	@ApiModelProperty(value = "商品ASIN")
	String asin;
	
	@ApiModelProperty(value = "店铺名称")
	String groupname;
	
	@ApiModelProperty(value = "对应本地产品颜色")
	String color;
	
	@ApiModelProperty(value = "产品名称")
	String name;
	
	@ApiModelProperty(value = "sku_orderid[ID]")
	String id;
	
	@ApiModelProperty(value = "销售站点[Amazon.com,Amazon.ca......]")
	String market;
	
	@ApiModelProperty(value = "币种[USD,EUR,CAD.....]")
	String currency;
	
	@ApiModelProperty(value = "发货服务")
	String shipservice;
	
	@ApiModelProperty(value = "发货城市")
	String city;
	
	@ApiModelProperty(value = "运费")
	BigDecimal shipfee;
	
	@ApiModelProperty(value = "运费折扣")
	BigDecimal shipdiscount;
	
	@ApiModelProperty(value = "商品折扣")
	BigDecimal itemdiscount;
	
	@ApiModelProperty(value = "AuthID")
	String authid;
	
	@ApiModelProperty(value = "订单备注")
	String remark;
	
	@ApiModelProperty(value = "是否为企业买家['false','true']")
	String isbusiness;
	
	@ApiModelProperty(value = "marketplaceId")
	String marketplaceId;
	
	@ApiModelProperty(value = "marketname")
	String marketname;
	
	@ApiModelProperty(value = "region")
	String region;
	
	@ApiModelProperty(value = "feedstatus")
	String feedstatus;
	
	@ApiModelProperty(value = "feedid")
	String feedid;
	
	@ApiModelProperty(value = "reviewstatus")
	String reviewstatus;
	
	@ApiModelProperty(value = "reviewSendTime")
	Date reviewSendTime;
	
	public String getKey() {
		return orderid+"-"+sku;
	}
	
	
	
	
	
	
	
	
}
