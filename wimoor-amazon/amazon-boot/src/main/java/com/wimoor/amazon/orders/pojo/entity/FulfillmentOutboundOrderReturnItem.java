package com.wimoor.amazon.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_amz_fulfillment_order_return_item")
@ApiModel(value="FulfillmentOutboundOrderReturnItem", description="FullfillmentReturnItem")
public class FulfillmentOutboundOrderReturnItem {
    @TableId(value="sellerReturnItemId")
    String sellerReturnItemId;

    @TableField(value="sellerFulfillmentOrderItemId")
    String sellerFulfillmentOrderItemId;

    @TableField(value="amazonShipmentId")
    String amazonShipmentId;

    @TableField(value="sellerReturnReasonCode")
    String sellerReturnReasonCode;

    @TableField(value="returnComment")
    String returnComment;

    @TableField(value="amazonReturnReasonCode")
    String amazonReturnReasonCode;

    @TableField(value="status")
    String status;

    @TableField(value="statusChangedDate")
    Date statusChangedDate;

    @TableField(value="returnAuthorizationId")
    String returnAuthorizationId;

    @TableField(value="returnReceivedCondition")
    String returnReceivedCondition;

    @TableField(value="fulfillmentCenterId")
    String fulfillmentCenterId;
}
