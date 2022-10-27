package com.wimoor.amazon.orders.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.amazon.spapi.model.orders.OrderItem;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.wimoor.amazon.util.UUIDUtil;
import com.wimoor.common.GeneralUtil;

import lombok.Data;

@Data
@TableName("t_amz_order_item")
public class AmzOrderItem   {

	@MppMultiId
	@TableField(value="amazon_order_id")
    private String amazonOrderId;
	
	@MppMultiId
	@TableField(value="orderItemId")
    private String orderitemid;
	
	@TableField(value="id")
    private String id;

	@TableField(value="last_updated_date")
    private Date lastUpdatedDate;

	@TableField(value="purchase_date")
    private Date purchaseDate;

	@TableField(value="asin")
    private String asin;

	@TableField(value="sku")
    private String sku;

	@TableField(value="title")
    private String title;

	@TableField(value="QuantityOrdered")
    private Integer quantityordered;

	@TableField(value="QuantityShipped")
    private Integer quantityshipped;

	@TableField(value="currency")
    private String currency;

	@TableField(value="item_price")
    private BigDecimal itemPrice;

	@TableField(value="item_tax")
    private BigDecimal itemTax;

	@TableField(value="shipping_price")
    private BigDecimal shippingPrice;

	@TableField(value="shipping_tax")
    private BigDecimal shippingTax;

	@TableField(value="gift_wrap_price")
    private BigDecimal giftWrapPrice;

	@TableField(value="gift_wrap_tax")
    private BigDecimal giftWrapTax;

	@TableField(value="item_promotion_discount")
    private BigDecimal itemPromotionDiscount;

	@TableField(value="ship_promotion_discount")
    private BigDecimal shipPromotionDiscount;

	@TableField(value="promotion_ids")
    private String promotionIds;

	@TableField(value="CODFee")
    private BigDecimal codfee;

	@TableField(value="CODFeeDiscount")
    private BigDecimal codfeediscount;

	@TableField(value="GiftMessageText")
    private String giftmessagetext;

	@TableField(value="GiftWrapLevel")
    private String giftwraplevel;

	@TableField(value="ConditionId")
    private String conditionid;

	@TableField(value="ConditionSubtypeId")
    private String conditionsubtypeid;

	@TableField(value="ConditionNote")
    private String conditionnote;

	@TableField(value="ScheduledDeliveryStartDate")
    private Date scheduleddeliverystartdate;

	@TableField(value="ScheduledDeliveryEndDate")
    private Date scheduleddeliveryenddate;

	@TableField(value="amazonAuthId")
    private String amazonauthid;

	@TableField(value="marketplaceId")
    private String marketplaceid;
    
	public AmzOrderItem() {
		
	}
	public String getId() {
		if (null == id) {
			id = UUIDUtil.getUUIDshort();
		}
		return id;
	}
	
	public AmzOrderItem(OrderItem orderitem, String market) {

    	if (orderitem==null) {
			return;
		}
    	this.setOrderitemid(orderitem.getOrderItemId());
    	this.setAsin(orderitem.getASIN());
    	this.setSku(orderitem.getSellerSKU());
    	this.setTitle(orderitem.getTitle());
    	this.setQuantityordered(orderitem.getQuantityOrdered());
    	this.setQuantityshipped(orderitem.getQuantityShipped());
    	if(orderitem.getItemPrice()!=null && orderitem.getItemPrice().getAmount()!=null){
    		this.setCurrency(orderitem.getItemPrice().getCurrencyCode());
    		this.setItemPrice(new BigDecimal(orderitem.getItemPrice().getAmount()));
    	}
    	if(orderitem.getItemTax()!=null&&orderitem.getItemTax().getAmount()!=null){
    		this.setItemTax(new BigDecimal(orderitem.getItemTax().getAmount()));
    	}
    	if(orderitem.getShippingPrice()!=null&&orderitem.getShippingPrice().getAmount()!=null){
    		this.setShippingPrice(new BigDecimal(orderitem.getShippingPrice().getAmount()));
    	}
    	if(orderitem.getShippingTax()!=null&&orderitem.getShippingTax().getAmount()!=null){
    		this.setShippingTax(new BigDecimal(orderitem.getShippingTax().getAmount()));
    	}
    	if(orderitem.getPromotionDiscount()!=null&&orderitem.getPromotionDiscount().getAmount()!=null){
    		this.setItemPromotionDiscount(new BigDecimal(orderitem.getPromotionDiscount().getAmount()));
    	}
    	if(orderitem.getShippingDiscount()!=null&&orderitem.getShippingDiscount().getAmount()!=null){
    		this.setShipPromotionDiscount(new BigDecimal(orderitem.getShippingDiscount().getAmount()));
    	}
    	if(orderitem.getPromotionDiscount()!=null&&orderitem.getPromotionDiscount().getAmount()!=null){
    		this.setItemPromotionDiscount(new BigDecimal(orderitem.getPromotionDiscount().getAmount()));
    	}
    	if(orderitem.getPromotionIds()!=null&&orderitem.getPromotionIds().size()>0){
    		StringBuffer sb = new StringBuffer();
    		for (int i = 0; i < orderitem.getPromotionIds().size(); i++) {
    			sb.append(orderitem.getPromotionIds().get(i)).append(",");
			}
    		this.setPromotionIds(sb.substring(0, sb.length()-1));
    	}
    	if(orderitem.getCoDFee()!=null && orderitem.getCoDFee().getAmount()!=null){
    		this.setCodfee(new BigDecimal(orderitem.getCoDFee().getAmount()));
    	}
    	if(orderitem.getCoDFeeDiscount()!=null && orderitem.getCoDFeeDiscount().getAmount()!=null){
    		this.setCodfeediscount(new BigDecimal(orderitem.getCoDFeeDiscount().getAmount()));
    	}
    	this.setConditionid(orderitem.getConditionId());
    	this.setConditionsubtypeid(orderitem.getConditionSubtypeId());
    	this.setConditionnote(orderitem.getConditionNote());
    	
		if(orderitem.getScheduledDeliveryStartDate()!=null){
			this.setScheduleddeliverystartdate(GeneralUtil.getDatePlus(orderitem.getScheduledDeliveryStartDate().toString(),market));
		}
		if(orderitem.getScheduledDeliveryEndDate()!=null){
			this.setScheduleddeliveryenddate(GeneralUtil.getDatePlus(orderitem.getScheduledDeliveryEndDate().toString(),market));
		}
	}
    
    
}