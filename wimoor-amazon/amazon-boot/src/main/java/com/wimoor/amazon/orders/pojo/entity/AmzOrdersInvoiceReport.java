package com.wimoor.amazon.orders.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-08-20
 */
@Data
@TableName("t_amz_orders_invoice_report")
@ApiModel(value="AmzOrdersInvoiceReport对象", description="")
public class AmzOrdersInvoiceReport implements Serializable {

    private static final long serialVersionUID=1L;
    	
    	
    	
    @TableField("order_id")
    private String orderId;
    
    @TableField("order_item_id")
    private String orderItemId;

    @TableField("purchase_date")
    private Date purchaseDate;

    @TableField("payments_date")
    private Date paymentsDate;

    @TableField("buyer_email")
    private String buyerEmail;

    @TableField("buyer_name")
    private String buyerName;

    @TableField("buyer_phone_number")
    private String buyerPhoneNumber;

    @TableField("sku")
    private String sku;

    @TableField("quantity_purchased")
    private Integer quantityPurchased;

    @TableField("currency")
    private String currency;

    @TableField("item_price")
    private BigDecimal itemPrice;

    @TableField("item_tax")
    private BigDecimal itemTax;

    @TableField("shipping_price")
    private BigDecimal shippingPrice;

    @TableField("shipping_tax")
    private BigDecimal shippingTax;

    @TableField("ship_service_level")
    private String shipServiceLevel;

    @TableField("recipient_name")
    private String recipientName;

    @TableField("ship_address_1")
    private String shipAddress1;

    @TableField("ship_address_2")
    private String shipAddress2;

    @TableField("ship_address_3")
    private String shipAddress3;

    @TableField("ship_city")
    private String shipCity;

    @TableField("ship_state")
    private String shipState;

    @TableField("ship_postal_code")
    private String shipPostalCode;

    @TableField("ship_country")
    private String shipCountry;

    @TableField("ship_phone_number")
    private String shipPhoneNumber;

    @TableField("delivery_start_date")
    private Date deliveryStartDate;

    @TableField("delivery_end_date")
    private Date deliveryEndDate;

    @TableField("delivery_time_zone")
    private String deliveryTimeZone;

    @TableField("delivery_instructions")
    private String deliveryInstructions;

    @TableField("is_business_order")
    private Boolean isBusinessOrder;

    @TableField("price_designation")
    private String priceDesignation;
    
    @TableField("sales_channel")
    private String salesChannel;

    @TableField("is_iba")
    private Boolean isIba;

    @TableField("amazonAuthId")
    private BigInteger amazonAuthId;

    @TableField("refreshtime")
    private LocalDateTime refreshtime;


}
