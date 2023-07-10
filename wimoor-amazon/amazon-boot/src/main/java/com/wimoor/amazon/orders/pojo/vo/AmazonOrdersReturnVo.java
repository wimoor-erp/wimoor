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
@ApiModel(value="AmazonOrdersReturnVo对象", description="订单退货列表对象")
public class AmazonOrdersReturnVo {

	@ApiModelProperty(value = "订单退货时间")
	Date returnDate;
	
	@ApiModelProperty(value = "订单ID")
	String orderId;
	
	@ApiModelProperty(value = "marketplaceid")
	String marketplaceid;
	
	@ApiModelProperty(value = "退货原因")
	String reason;
	
	@ApiModelProperty(value = "退货详细")
	String detailedDisposition;
	
	@ApiModelProperty(value = "物流中心ID")
	String fulfillmentCenterId;
	
	@ApiModelProperty(value = "商品图片")
	String image;
	
	@ApiModelProperty(value = "退货数量")
	Integer quantity;
	
	@ApiModelProperty(value = "SKU")
	String sku;
	
	@ApiModelProperty(value = "ASIN")
	String asin;
	
	@ApiModelProperty(value = "店铺名称")
	String groupname;
	
	@ApiModelProperty(value = "产品本地SKU颜色")
	String color;
	
	@ApiModelProperty(value = "商品名称")
	String name;
	
	@ApiModelProperty(value = "站点名称")
	String marketname;
	
	@ApiModelProperty(value = "AuthID")
	String authid;
	
	@ApiModelProperty(value = "客户注释")
	String customerComments;
	
	@ApiModelProperty(value = "下单时间")
	Date purchaseDate;
	
	@ApiModelProperty(value = "订单总金额")
	BigDecimal itemPrice;
	
	
	
}
