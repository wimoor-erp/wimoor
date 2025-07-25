package com.wimoor.amazon.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("t_amz_fulfillment_order_items")
@ApiModel(value="FulfillmentOutboundOrderItems", description="FullfillmentItem")
public class FulfillmentOutboundOrderItems {
        @TableId(value="sellerFulfillmentOrderItemId")
        String sellerFulfillmentOrderItemId;

        @TableField(value="sellerFulfillmentOrderId")
        String sellerFulfillmentOrderId;

        @TableField(value="sellerSku")
        String sellerSku;

        @TableField(value="quantity")
        Integer quantity;

        @TableField(value="giftMessage")
	    String giftMessage;

        @TableField(value="displayableComment")
        String displayableComment;

        @TableField(value="fulfillmentNetworkSku")
        String fulfillmentNetworkSku;

        @TableField(value="orderItemDisposition")
        String orderItemDisposition;

        @TableField(value="cancelledQuantity")
        Integer cancelledQuantity;

        @TableField(value="unfulfillableQuantity")
        Integer unfulfillableQuantity;

        @TableField(value="estimatedShipDate")
        Date estimatedShipDate;

        @TableField(value="estimatedArrivalDate")
        Date estimatedArrivalDate;

        @TableField(value="perUnitPrice")
        BigDecimal perUnitPrice;

        @TableField(value="perUnitTax")
        BigDecimal perUnitTax;

        @TableField(value="perUnitDeclaredValue")
        BigDecimal perUnitDeclaredValue;
}
