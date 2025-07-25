package com.wimoor.amazon.product.pojo.dto;

import java.util.List;

import com.amazon.spapi.model.catalogitems.Dimensions;
import com.amazon.spapi.model.listings.Money;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
@Data
public class ItemAttributesDTO {
    private String title;
    private String title_language;
    private Money list_price;
    private Money purchasable_price;
    private Money discounted_price;
    private String discounted_price_start_at;
    private String discounted_price_end_at;
    private String condition_type;
    private String fulfillment_availability;
    private String fulfillment_channel_code;
    private String max_order_quantity;
    private String number_of_items;
    private String merchant_shipping_group;
    private Dimensions itemDimensions;
    private Dimensions packageDimensions;
    private String lead_time_to_ship_max_days;
    public JsonObject getJsonFulfillment(String marketid) {
    	  JsonObject fulfillment_availability_json=new JsonObject();
		  fulfillment_availability_json.addProperty("fulfillment_channel_code",fulfillment_channel_code);
		  fulfillment_availability_json.addProperty("quantity", fulfillment_availability);
		  if(StrUtil.isNotBlank(lead_time_to_ship_max_days)) {
			  fulfillment_availability_json.addProperty("lead_time_to_ship_max_days", lead_time_to_ship_max_days);
		  }
		  fulfillment_availability_json.addProperty("marketplace_id", marketid);
		  return fulfillment_availability_json; 
    }
    public JsonObject getJsonPurchasableOffer(String marketid) {
	  	  JsonObject price=new JsonObject();
		  JsonArray our_price=new JsonArray();
		  JsonObject schedule=new JsonObject();
		  JsonArray scheduleitem=new JsonArray();
		  if(purchasable_price!=null && purchasable_price.getAmount()!=null) {
			  JsonObject value_with_tax=new JsonObject();
			  value_with_tax.addProperty("value_with_tax", purchasable_price.getAmount());
			  scheduleitem.add(value_with_tax);
			  schedule.add("schedule", scheduleitem);
			  our_price.add(schedule);
			  price.add("our_price", our_price );
			  price.addProperty("currency",purchasable_price.getCurrencyCode() );
		  }
		 
		  if(discounted_price!=null && discounted_price.getAmount()!=null) {
			  JsonArray discounted_price_arrray=new JsonArray();
			  JsonObject schedule2=new JsonObject();
			  JsonArray scheduleitem2=new JsonArray();
			  JsonObject value=new JsonObject();
			  value.addProperty("value_with_tax", discounted_price.getAmount());
			  value.addProperty("start_at", discounted_price_start_at);
			  value.addProperty("end_at", discounted_price_end_at);
			  scheduleitem2.add(value);
			  schedule2.add("schedule", scheduleitem2);
			  discounted_price_arrray.add(schedule2);
			  price.add("discounted_price", discounted_price_arrray );
			  price.addProperty("currency",discounted_price.getCurrencyCode() );
		  }
		  
		  price.addProperty("marketplace_id", marketid);
		  return price;
  }
    
    public JsonObject getJsonMerchantShippingGroup(String marketid) {
		  JsonObject merchant_shipping_group_json = new JsonObject();
		  merchant_shipping_group_json.addProperty("value",merchant_shipping_group);
		  merchant_shipping_group_json.addProperty("marketplace_id", marketid);
		  return merchant_shipping_group_json;
    }
    
    public JsonObject getJson(List<String> marketplaceids,String asin) {
    	JsonObject json = new  JsonObject();
		  JsonArray market_condition_types=new JsonArray();
		  JsonArray market_merchant_suggested_asin=new JsonArray();
		  JsonArray market_fulfillment_availability=new JsonArray();
		  JsonArray market_item_name=new JsonArray();
		  JsonArray market_number_of_items=new JsonArray();
		  JsonArray market_list_price=new JsonArray();
		  JsonArray market_purchasable_offer=new JsonArray();
		  JsonArray market_merchant_shipping_group_json=new JsonArray();
		  JsonArray market_max_order_quantity=new JsonArray();
		  for(String marketid:marketplaceids) {
			  if(StrUtil.isNotBlank(condition_type)) {
				  JsonObject condition_type_json = new JsonObject();
				  condition_type_json.addProperty("value", condition_type);
				  condition_type_json.addProperty("marketplace_id", marketid);
				  market_condition_types.add(condition_type_json); 
			  }
			  if(StrUtil.isNotBlank(fulfillment_availability)) {
				  JsonObject fulfillment_availability_json=getJsonFulfillment(marketid);
				  market_fulfillment_availability.add(fulfillment_availability_json); 
			  }
			  if(list_price!=null) {
				  JsonObject price=new JsonObject();
				  price.addProperty("CurrencyCode",list_price.getCurrencyCode());
				  price.addProperty("value", list_price.getAmount());
				  price.addProperty("marketplace_id", marketid);
				  market_list_price.add(price); 
			  }
			  if(purchasable_price!=null) {
				  JsonObject price=getJsonPurchasableOffer(marketid);
				  market_purchasable_offer.add(price); 
			  }
			  if(StrUtil.isNotBlank(title)) {
				  JsonObject item_name = new JsonObject();
				  item_name.addProperty("value",this.title);
				  item_name.addProperty("language_tag", title_language);
				  item_name.addProperty("marketplace_id", marketid);
				  market_item_name.add(item_name);
			  }
			  if(StrUtil.isNotBlank(number_of_items)) {
				  JsonObject number_of_items_json = new JsonObject();
				  number_of_items_json.addProperty("value",number_of_items);
				  number_of_items_json.addProperty("marketplace_id", marketid);
				  market_number_of_items.add(number_of_items_json);
			  }
			  if(StrUtil.isNotBlank(max_order_quantity)) {
				  JsonObject max_order_quantity_json = new JsonObject();
				  max_order_quantity_json.addProperty("value",max_order_quantity);
				  max_order_quantity_json.addProperty("marketplace_id", marketid);
				  market_max_order_quantity.add(max_order_quantity_json);
				  
			  }
			  if(StrUtil.isNotBlank(merchant_shipping_group)) {
				  JsonObject merchant_shipping_group_json =getJsonMerchantShippingGroup(marketid);
				  market_merchant_shipping_group_json.add(merchant_shipping_group_json);
			  }
			  
			  JsonObject merchant_suggested_asin = new JsonObject();
			  merchant_suggested_asin.addProperty("value", asin);
			  merchant_suggested_asin.addProperty("marketplace_id", marketid);
			  market_merchant_suggested_asin.add(merchant_suggested_asin); 
		  }
		  if(market_condition_types.size()>0) {
			  json.add("condition_type", market_condition_types);
		  }
		  if(market_fulfillment_availability.size()>0) {
			  json.add("fulfillment_availability", market_fulfillment_availability);
		  }
		  if(market_list_price.size()>0) {
			  json.add("list_price", market_list_price);
		  }
		  
		  if(market_item_name.size()>0) {
			  json.add("item_name", market_item_name);
		  }
		  if(market_purchasable_offer.size()>0) {
			  json.add("purchasable_offer", market_purchasable_offer);
		  }
		  if(market_merchant_shipping_group_json.size()>0) {
			  json.add("merchant_shipping_group", market_merchant_shipping_group_json);
		  }
		  if(market_number_of_items.size()>0) {
			  json.add("number_of_items", market_number_of_items);
		  }
		  if(market_max_order_quantity.size()>0) {
			  json.add("max_order_quantity", market_max_order_quantity);
		  }
		  json.add("merchant_suggested_asin", market_merchant_suggested_asin);
          return json;
    }
}
