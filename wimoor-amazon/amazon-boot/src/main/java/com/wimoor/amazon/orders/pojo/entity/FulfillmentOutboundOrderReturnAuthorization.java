package com.wimoor.amazon.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amz_fulfillment_order_return_authorization")
@ApiModel(value="FulfillmentOutboundOrderReturnAuthorization", description="FullfillmentReturnAuthorization")
public class FulfillmentOutboundOrderReturnAuthorization {
    @TableId(value="returnAuthorizationId")
    String returnAuthorizationId;

    @TableField(value="sellerFulfillmentOrderId")
    String sellerFulfillmentOrderId;

    @TableField(value="fulfillmentCenterId")
    String fulfillmentCenterId;

    @TableField(value="returnToAddress")
    String returnToAddress;

    @TableField(value="amazonRmaId")
    String amazonRmaId;

    @TableField(value="rmaPageURL")
    String rmaPageURL;
}
