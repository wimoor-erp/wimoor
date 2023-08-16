package com.wimoor.amazon.product.pojo.entity;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_follow_offerchange")
@ApiModel(value="FollowOffer对象", description="FollowOffer")
public class FollowOfferChange extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4478098187694509545L;

	@TableField(value = "asin")
    private String asin;

	@TableField(value = "marketplaceid")
    private String marketplaceid;

	@TableField(value = "sellerid")
    private String sellerid;

	@TableField(value = "listing_price_amount")
    private BigDecimal listingPriceAmount;

	@TableField(value = "shiping_amount")
    private BigDecimal shipingAmount;

	@TableField(value = "currency")
    private String currency;

	@TableField(value = "is_fulfilled_by_amazon")
    private Boolean isFulfilledByAmazon;

	@TableField(value = "is_buy_box_winner")
    private Boolean isBuyBoxWinner;

	@TableField(value = "is_featured_merchant")
    private Boolean isFeaturedMerchant;

	@TableField(value = "is_prime")
    private Boolean isPrime;

	@TableField(value = "is_national_prime")
    private Boolean isNationalPrime;

	@TableField(value = "ships_domestically")
    private Boolean shipsDomestically;

	@TableField(value = "findtime")
    private Date findtime;

	@TableField(value = "losttime")
    private Date losttime;

	@TableField(value = "opttime")
    private Date opttime;
	
    @TableField(value = "minimum_hours")
    Integer minimumHours ;
    
    @TableField(value = "maximum_hours")
	Integer maximumHours ;
    
    @TableField(value = "availability_type")
	String availabilityType;
    
    @TableField(value = "available_date")
	String availableDate;
    
    @TableField(value = "sub_condition")
	String subCondition;
    
    @TableField(value = "ships_from_state")
	String shipsFromState;
    
    @TableField(value = "ships_from_country")
	String shipsFromCountry;
    
}