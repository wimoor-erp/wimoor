package com.wimoor.amazon.orders.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@TableName("t_amz_fulfillment_order_payment")
@ApiModel(value="FulfillmentOutboundOrderPayment", description="FullfillmentPayment")
public class FulfillmentOutboundOrderPayment {
    @TableField(value="sellerFulfillmentOrderId")
	String sellerFulfillmentOrderId;

    @TableId(value="paymentTransactionId")
    String paymentTransactionId;

    @TableField(value="paymentMode")
    String paymentMode;

    @TableField(value="paymentDate")
    Date paymentDate;
}
