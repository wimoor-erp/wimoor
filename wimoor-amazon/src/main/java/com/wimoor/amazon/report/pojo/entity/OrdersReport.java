package com.wimoor.amazon.report.pojo.entity;
import java.math.BigDecimal;
import java.util.Date;
 

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

	private String amazonOrderId;

    private String merchantOrderId;

    private Date purchaseDate;

    private Date lastUpdatedDate;

    private String orderStatus;

    private String fulfillmentChannel;

    private String salesChannel;

    private String orderChannel;

    private String url;

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

    private String amazonauthid;

    private String marketplaceId;
     
	
}