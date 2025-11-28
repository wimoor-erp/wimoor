package com.wimoor.amazon.orders.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_amz_fulfillment_order_shipment")
@ApiModel(value="FulfillmentOutboundOrderShipment", description="FulfillmentOutboundOrderShipment")
public class FulfillmentOutboundOrderShipment {

        @TableId(value="amazonShipmentId")
        String amazonShipmentId;

        @TableField(value="sellerFulfillmentOrderId")
        String sellerFulfillmentOrderId;

        @TableField(value="fulfillmentCenterId")
        String fulfillmentCenterId;

        @TableField(value="fulfillmentShipmentStatus")
        String fulfillmentShipmentStatus;

        @TableField(value="shippingDate")
        Date shippingDate;

        @TableField(value="estimatedArrivalDate")
	    Date estimatedArrivalDate;

        @TableField(value="shippingNotes")
	    String shippingNotes;
}
