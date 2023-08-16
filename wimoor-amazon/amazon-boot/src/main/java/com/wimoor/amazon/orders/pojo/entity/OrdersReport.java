package com.wimoor.amazon.orders.pojo.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_orders_report")  
@ApiModel(value="OrdersReport对象", description="订单")
public class OrdersReport extends BaseEntity {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -2814977072838876644L;
    @TableField(value="amazon_order_id")
	private String amazonOrderId;

    @TableField(value="merchant_order_id")
    private String merchantOrderId;

    @TableField(value="purchase_date")
    private Date purchaseDate;

    @TableField(value="last_updated_date")
    private Date lastUpdatedDate;

    @TableField(value="order_status")
    private String orderStatus;

    @TableField(value="fulfillment_channel")
    private String fulfillmentChannel;

    @TableField(value="sales_channel")
    private String salesChannel;

    @TableField(value="order_channel")
    private String orderChannel;

    private String url;

    @TableField(value="ship_service_level")
    private String shipServiceLevel;

    private String sku;

    private String asin;

    private String itemStatus;

    private Integer quantity;

    private String currency;

    private BigDecimal itemPrice;

    private BigDecimal itemTax;

    private BigDecimal shippingPrice;

    private BigDecimal shippingTax;

    private BigDecimal giftWrapPrice;

    private BigDecimal giftWrapTax;

    private BigDecimal itemPromotionDiscount;

    private BigDecimal shipPromotionDiscount;

    private String shipCity;

    private String shipState;

    private String shipPostalCode;

    private String shipCountry;

    private String promotionIds;

    private String isBusinessOrder;

    private String purchaseOrderNumber;

    private String priceDesignation;

    @TableField(value="amazonAuthId")
    private String amazonauthid;

    @TableField(value="marketplaceId")
    private String marketplaceId;
     
    @TableField(value="refreshtime")
    private Date refreshtime;
	
}