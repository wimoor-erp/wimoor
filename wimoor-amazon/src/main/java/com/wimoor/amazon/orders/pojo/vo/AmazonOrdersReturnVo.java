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
	Date returndate;
	
	@ApiModelProperty(value = "订单ID")
	String orderid;
	
	@ApiModelProperty(value = "退货原因")
	String reason;
	
	@ApiModelProperty(value = "物流中心ID")
	String centerid;
	
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
	
	@ApiModelProperty(value = "sku_orderid")
	String id;
	
	@ApiModelProperty(value = "站点名称")
	String market;
	
	@ApiModelProperty(value = "AuthID")
	String authid;
	
	@ApiModelProperty(value = "客户注释")
	String custcomment;
	
	@ApiModelProperty(value = "下单时间")
	Date purchase_date;
	
	@ApiModelProperty(value = "订单总金额")
	BigDecimal item_price;
	
	
}
