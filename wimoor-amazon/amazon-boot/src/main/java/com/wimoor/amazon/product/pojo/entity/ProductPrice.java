package com.wimoor.amazon.product.pojo.entity;


import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_price")
@ApiModel(value="ProductPrice对象", description="产品价格信息")
public class ProductPrice  extends BaseEntity{
 

    /**
	 * 
	 */
	private static final long serialVersionUID = 4083893893436761564L;

	private String marketplaceid;

    private String asin;

    private Date byday;

    private String ptype;

    private BigDecimal landedAmount;

    private String landedCurrency;

    private BigDecimal listingAmount;

    private String listingCurrency;

    private BigDecimal shippingAmount;

    private String shippingCurrency;

    private Boolean isnewest;

    private String fulfillmentchannel;

    private String itemcondition;

    private String itemsubcondition;

    private String sellerid;

    private String sellersku;
  
}