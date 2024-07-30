package com.wimoor.amazon.orders.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AmazonOrdersShipVo对象", description="订单退货列表对象")
public class AmazonOrdersShipVo {

	@ApiModelProperty(value = "购买时间")
	Date purchase_date;
	
	@ApiModelProperty(value = "付款时间")
	Date payments_date;
	
	@ApiModelProperty(value = "发货时间")
	Date shipment_date;
	
	@ApiModelProperty(value = "预计到货时间")
	Date reporting_date;
	
	@ApiModelProperty(value = "用户名")
	String buyer_name;
	
	@ApiModelProperty(value = "用户邮箱")
	String buyer_email;
	
	@ApiModelProperty(value = "用户电话")
	String buyer_phone_number;
	
	@ApiModelProperty(value = "商品图片")
	String image;
	
	@ApiModelProperty(value = "购买数量")
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
	
	@ApiModelProperty(value = "单价")
	BigDecimal item_price;
	
	@ApiModelProperty(value = "单价税费")
	BigDecimal item_tax;
	
	@ApiModelProperty(value = "运费")
	BigDecimal shipping_price;
	
	@ApiModelProperty(value = "运费税费")
	BigDecimal ishipping_tax;
	
	@ApiModelProperty(value = "礼品费")
	BigDecimal gift_wrap_price;
	
	@ApiModelProperty(value = "礼品税费")
	BigDecimal gift_wrap_tax;
	
	@ApiModelProperty(value = "收货人名称")
	String recipient_name;
	
	@ApiModelProperty(value = "收货人地址1")
	String ship_address_1;
	
	@ApiModelProperty(value = "收货人地址2")
	String ship_address_2;
	
	@ApiModelProperty(value = "收货人地址3")
	String ship_address_3;
	
	@ApiModelProperty(value = "收货人城市")
	String ship_city;
	
	@ApiModelProperty(value = "收货人所在州")
	String ship_state;
	
	@ApiModelProperty(value = "收货人邮编")
	String ship_postal_code;
	
	@ApiModelProperty(value = "收货人所在国家编码")
	String ship_country;
	
	@ApiModelProperty(value = "收货人手机号码")
	String ship_phone_number;
	
	@ApiModelProperty(value = "亚马逊配送中心ID")
	String fulfillment_center_id;
	
	@ApiModelProperty(value = "配送中心名称")
	String centername;
	
	@ApiModelProperty(value = "配送中心所在城市")
	String centercity;
	
	@ApiModelProperty(value = "配送中心所在州")
	String centerstate;
	
	@ApiModelProperty(value = "追踪编码")
	String tracking_number;
	
	@ApiModelProperty(value = "承运方")
	String carrier;
	
	@ApiModelProperty(value = "订单号")
	String amazon_order_id;
	
	@ApiModelProperty(value = "订单明细数据")
	Map<String,Object> orderdetail;
	
	
	
}
