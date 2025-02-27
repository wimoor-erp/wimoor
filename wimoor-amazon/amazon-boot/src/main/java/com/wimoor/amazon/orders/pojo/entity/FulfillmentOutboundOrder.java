package com.wimoor.amazon.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_amz_fulfillment_order")
@ApiModel(value="FulfillmentOutboundOrders", description="其他订单备")
public class FulfillmentOutboundOrder {
    @TableId(value="sellerFulfillmentOrderId")
    private String sellerFulfillmentOrderId;

    @TableField(value="marketplaceId")
    private String marketplaceId;

    @TableField(value="displayableOrderId")
    private String displayableOrderId;

    @TableField(value="displayableOrderDate")
    private Date displayableOrderDate;

    @TableField(value="displayableOrderComment")
    private String displayableOrderComment;

    @TableField(value="shippingSpeedCategory")
    private Integer shippingSpeedCategory;

    @TableField(value="deliveryStart")
    private Date deliveryStart;

    @TableField(value="deliveryEnd")
    private Date deliveryEnd;

    @TableField(value="destinationAddress")
    private String destinationAddress;

    @TableField(value="fulfillmentAction")
    private String fulfillmentAction;

    @TableField(value="fulfillmentPolicy")
    private String fulfillmentPolicy;

    @TableField(value="receivedDate")
    private Date receivedDate;

    @TableField(value="fulfillmentOrderStatus")
    private String fulfillmentOrderStatus;

    @TableField(value="statusUpdatedDate")
    private Date statusUpdatedDate;

    @TableField(value="notificationEmails")
    private String notificationEmails;

    @TableField(value="featureConstraints")
    private String featureConstraints;

    @TableField(value="amazonauthid")
    private String amazonauthid;

    @TableField(value="refreshtime")
    private Date refreshtime;
}
