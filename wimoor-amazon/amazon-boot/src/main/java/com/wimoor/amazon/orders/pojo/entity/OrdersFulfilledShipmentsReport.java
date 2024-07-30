package com.wimoor.amazon.orders.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@TableName("t_amz_rpt_orders_fulfilled_shipments_report")  
@ApiModel(value="OrdersFulfilledShipmentsReport对象", description="发货报表")
public class OrdersFulfilledShipmentsReport  {
    @ApiModelProperty(value = "亚马逊订单ID")
	@TableField(value= "amazon_order_id")
	private String amazonOrderId;

    @ApiModelProperty(value = "发货ID")
	@TableField(value= "shipment_item_id")
	private String shipmentItemId;

    @ApiModelProperty(value = "订单ItemID")
	@TableField(value= "amazon_order_item_id")
	private String amazonOrderItemId;
	    
    @ApiModelProperty(value = "卖家订单ID")
	@TableField(value= "merchant_order_id")
    private String merchantOrderId;

    @ApiModelProperty(value = "发货单ID")
	@TableField(value= "shipment_id")
    private String shipmentId;


    @ApiModelProperty(value = "卖家发货ItemID")
	@TableField(value= "merchant_order_item_id")
    private String merchantOrderItemId;


    @ApiModelProperty(value = "客户购买日期")
	@TableField(value= "purchase_date")
    private Date purchaseDate;

    @ApiModelProperty(value = "客户付款日期")
	@TableField(value= "payments_date")
    private Date paymentsDate;


    @ApiModelProperty(value = "订单发货日期")
	@TableField(value= "shipment_date")
    private Date shipmentDate;

    @ApiModelProperty(value = "报表日期")
	@TableField(value= "reporting_date")
    private Date reportingDate;

    @ApiModelProperty(value = "客户邮箱")
	@TableField(value= "buyer_email")
    private String buyerEmail;

    @ApiModelProperty(value = "客户买家名称")
	@TableField(value= "buyer_name")
    private String buyerName;

    @ApiModelProperty(value = "客户买家电话")
	@TableField(value= "buyer_phone_number")
    private String buyerPhoneNumber;

    @ApiModelProperty(value = "商品ＳＫＵ")
	@TableField(value= "sku")
    private String sku;


    @ApiModelProperty(value = "商品名称")
	@TableField(value= "product_name")
    private String productName;

    @ApiModelProperty(value = "发货数量")
	@TableField(value= "quantity_shipped")
    private Integer quantityShipped;

    @ApiModelProperty(value = "币种")
	@TableField(value= "currency")
    private String currency;

    @ApiModelProperty(value = "产品价格")
	@TableField(value= "item_price")
    private BigDecimal itemPrice;

    @ApiModelProperty(value = "产品税")
	@TableField(value= "item_tax")
    private BigDecimal itemTax;


    @ApiModelProperty(value = "运费")
	@TableField(value= "shipping_price")
    private BigDecimal shippingPrice;

    @ApiModelProperty(value = "运费税")
	@TableField(value= "shipping_tax")
    private BigDecimal shippingTax;

    @ApiModelProperty(value = "礼品包装价格")
	@TableField(value= "gift_wrap_price")
    private BigDecimal giftWrapPrice;

    @ApiModelProperty(value = "礼品包装税")
	@TableField(value= "gift_wrap_tax")
    private BigDecimal giftWrapTax;

    @ApiModelProperty(value = "发货服务等级")
	@TableField(value= "ship_service_level")
    private String shipServiceLevel;

    @ApiModelProperty(value = "地址名称")
	@TableField(value= "recipient_name")
    private String recipientName;

    @ApiModelProperty(value = "地址1")
	@TableField(value= "ship_address_1")
    private String shipAddress1;

    @ApiModelProperty(value = "地址2")
	@TableField(value= "ship_address_2")
    private String shipAddress2;

    @ApiModelProperty(value = "地址3")
	@TableField(value= "ship_address_3")
    private String shipAddress3;

    @ApiModelProperty(value = "城市")
	@TableField(value= "ship_city")
    private String shipCity;

    @ApiModelProperty(value = "州")
	@TableField(value= "ship_state")
    private String shipState;

    @ApiModelProperty(value = "邮政编码")
	@TableField(value= "ship_postal_code")
    private String shipPostalCode;

    @ApiModelProperty(value = "国家")
	@TableField(value= "ship_country")
    private String shipCountry;

    @ApiModelProperty(value = "电话")
	@TableField(value= "ship_phone_number")
    private String shipPhoneNumber;

    @ApiModelProperty(value = "订单地址1")
	@TableField(value= "bill_address_1")
    private String billAddress1;

    @ApiModelProperty(value = "订单地址2")
	@TableField(value= "bill_address_2")
    private String billAddress2;

    @ApiModelProperty(value = "订单地址3")
	@TableField(value= "bill_address_3")
    private String billAddress3;

    @ApiModelProperty(value = "订单城市")
  	@TableField(value= "bill_city")
    private String billCity;

    @ApiModelProperty(value = "订单州")
  	@TableField(value= "bill_state")
    private String billState;

    @ApiModelProperty(value = "订单邮政编码")
  	@TableField(value= "bill_postal_code")
    private String billPostalCode;


    @ApiModelProperty(value = "订单国家")
  	@TableField(value= "bill_country")
    private String billCountry;

    @ApiModelProperty(value = "产品折扣")
  	@TableField(value= "item_promotion_discount")
    private BigDecimal itemPromotionDiscount;

    @ApiModelProperty(value = "运费折扣")
  	@TableField(value= "ship_promotion_discount")
    private BigDecimal shipPromotionDiscount;

    @ApiModelProperty(value = "配送商")
  	@TableField(value= "carrier")
    private String carrier;

    @ApiModelProperty(value = "跟踪编码")
  	@TableField(value= "tracking_number")
    private String trackingNumber;

    @ApiModelProperty(value = "预计到达日期")
  	@TableField(value= "estimated_arrival_date")
    private Date estimatedArrivalDate;

    @ApiModelProperty(value = "配送中心")
  	@TableField(value= "fulfillment_center_id")
    private String fulfillmentCenterId;

    @ApiModelProperty(value = "配送渠道")
  	@TableField(value= "fulfillment_channel")
    private String fulfillmentChannel;

    
    @ApiModelProperty(value = "销售渠道")
  	@TableField(value= "sales_channel")
    private String salesChannel;
    
    @ApiModelProperty(value = "店铺授权ID")
  	@TableField(value= "amazonauthid")
    private String  amazonauthid;

}