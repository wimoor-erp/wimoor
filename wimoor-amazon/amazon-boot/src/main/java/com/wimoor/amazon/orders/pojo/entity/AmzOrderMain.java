package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.amazon.spapi.model.orders.Order;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.util.UUIDUtil;
import com.wimoor.common.GeneralUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_amz_order_main")
@ApiModel(value="Orders对象", description="订单")
public class AmzOrderMain  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1088031503164634523L;

	@TableId(value="amazon_order_id")
    private String amazonOrderId;
	
	@TableField(value="id")
    private String id;
	
	@TableField(value="seller_order_id")
    private String sellerOrderId;

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

	@TableField(value="ship_service_level")
    private String shipServiceLevel;

	@TableField(value="buyer_shipping_address_id")
    private String buyerShippingAddressId;

	@TableField(value="currency")
    private String currency;

	@TableField(value="order_total")
    private BigDecimal orderTotal;

	@TableField(value="numberOfItemsShipped")
    private Integer numberofitemsshipped;

	@TableField(value="numberOfItemsUnshipped")
    private Integer numberofitemsunshipped;

	@TableField(value="paymentMethod")
    private String paymentmethod;

	@TableField(value="payment_execution_detail_item")
    private BigDecimal paymentExecutionDetailItem;

	@TableField(value="buyer_email")
    private String buyerEmail;

	@TableField(value="buyer_name")
    private String buyerName;

	@TableField(value="shipment_serviceLevel_category")
    private String shipmentServicelevelCategory;

	@TableField(value="CbaDisplayableShippingLabel")
    private String cbadisplayableshippinglabel;

	@TableField(value="orderType")
    private String ordertype;

	@TableField(value="earliestShipDate")
    private Date earliestshipdate;

	@TableField(value="latestShipDate")
    private Date latestshipdate;

	@TableField(value="earliestDeliveryDate")
    private Date earliestdeliverydate;

	@TableField(value="latestDeliveryDate")
    private Date latestdeliverydate;

	@TableField(value="isBusinessOrder")
    private boolean isBusinessOrder;
    
	@TableField(value="remark")
    private String remark;
	
	@TableField(value="hasItem")
    private boolean hasItem;
    
	@TableField(value = "fulfillment_supply_sourceid")
	@ApiModelProperty(value = "履行渠道的供应来源id")
    private String fulfillmentSupplySourceid;
	
	@TableField(value = "promise_response_duedate")
	@ApiModelProperty(value = "承诺响应日期")
    private Date promiseResponseDuedate;
	
	@TableField(value = "replaced_orderid")
	@ApiModelProperty(value = "替换的订单ID")
    private String replacedOrderid;
	
	@TableField(value="amazonAuthId")
    private String amazonauthid;

	@TableField(value="marketplaceId")
    private String marketplaceid;
	
	@TableField(exist=false)
    public AmzOrderBuyerShipAddress buyerAdress;

	@TableField(exist=false)
	public List<AmzOrderItem> OrderItemList;
	
	public String getId() {
		if (null == id) {
			id = UUIDUtil.getUUIDshort();
		}
		return id;
	}

	public AmzOrderMain(Order order, String market) {

    	if (order==null) {
			return;
		}
    	this.setMarketplaceid(order.getMarketplaceId());
		this.setAmazonOrderId(order.getAmazonOrderId());
		this.setSellerOrderId(order.getSellerOrderId());
		this.setOrderStatus(order.getOrderStatus().getValue());
		this.setFulfillmentChannel(order.getFulfillmentChannel().getValue());
		this.setSalesChannel(order.getSalesChannel());
		this.setOrderChannel(order.getOrderChannel());
		this.setShipServiceLevel(order.getShipServiceLevel());
		if(order.getOrderTotal()!=null&&order.getOrderTotal().getAmount()!=null){
			this.setOrderTotal(new BigDecimal(order.getOrderTotal().getAmount()));
			this.setCurrency(order.getOrderTotal().getCurrencyCode());
		}
		this.setNumberofitemsshipped(order.getNumberOfItemsShipped());
		this.setNumberofitemsunshipped(order.getNumberOfItemsUnshipped());
		this.setPaymentmethod(order.getPaymentMethod().getValue());
		if(order.getPaymentExecutionDetail()!=null && order.getPaymentExecutionDetail().size()>0&&
				order.getPaymentExecutionDetail().get(0).getPayment()!=null&&order.getPaymentExecutionDetail().get(0).getPayment().getAmount()!=null){
			this.setPaymentExecutionDetailItem(new BigDecimal(order.getPaymentExecutionDetail().get(0).getPayment().getAmount()));
		}
		this.setShipmentServicelevelCategory(order.getShipmentServiceLevelCategory());
		this.setCbadisplayableshippinglabel(order.getCbaDisplayableShippingLabel());
		this.setOrdertype(order.getOrderType().getValue());
		this.setHasItem(false);
		if(order.getShippingAddress()!=null){
			AmzOrderBuyerShipAddress buyerAdress = new AmzOrderBuyerShipAddress();
			buyerAdress.setAmazonOrderid(order.getAmazonOrderId().toString());
			buyerAdress.setMarketplaceid(order.getMarketplaceId());
			buyerAdress.setOpttime(new Date());
			buyerAdress.setAddressLine1(order.getShippingAddress().getAddressLine1());
			buyerAdress.setAddressLine2(order.getShippingAddress().getAddressLine2());
			buyerAdress.setAddressLine3(order.getShippingAddress().getAddressLine3());
			buyerAdress.setCity(order.getShippingAddress().getCity());
			buyerAdress.setCounty(order.getShippingAddress().getCounty());
			buyerAdress.setDistrict(order.getShippingAddress().getDistrict());
			buyerAdress.setStateOrRegion(order.getShippingAddress().getStateOrRegion());
			buyerAdress.setPostalCode(order.getShippingAddress().getPostalCode());
			buyerAdress.setCountryCode(order.getShippingAddress().getCountryCode());
			buyerAdress.setPhone(order.getShippingAddress().getPhone());
			if(order.getShippingAddress().getAddressType()!=null) {
				buyerAdress.setAddressType(order.getShippingAddress().getAddressType().getValue());
			}
			this.setBuyerAdress(buyerAdress);
		}
		
		if(order.getLastUpdateDate()!=null){
			this.setLastUpdatedDate(GeneralUtil.getDatePlus(order.getLastUpdateDate().toString(),market));
		}
		if(order.getPurchaseDate()!=null){
			this.setPurchaseDate(GeneralUtil.getDatePlus(order.getPurchaseDate().toString(),market));
		}
		if (order.getEarliestShipDate()!=null) {
			this.setEarliestshipdate(GeneralUtil.getDatePlus(order.getEarliestShipDate().toString(),market));
		}
		if(order.getLatestShipDate()!=null){
			this.setLatestshipdate(GeneralUtil.getDatePlus(order.getLatestShipDate().toString(),market));
		}
		if(order.getEarliestDeliveryDate()!=null){
			this.setEarliestdeliverydate(GeneralUtil.getDatePlus(order.getEarliestDeliveryDate().toString(),market));
		}
		if(order.getLatestDeliveryDate()!=null){
			this.setLatestdeliverydate(GeneralUtil.getDatePlus(order.getLatestDeliveryDate().toString(),market));
		}
	   this.hasItem=true;
	}

	public AmzOrderMain() {
		
	}

}