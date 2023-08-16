package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_orders_remark")
@ApiModel(value="OrdersRemark对象", description="订单备注表")
public class AmzOrdersRemark  implements Serializable{
	
	private static final long serialVersionUID = -5415211575998678711L;
	
	@TableId(value="amazon_order_id")
    private String amazonOrderId;
	
	@TableField(value="feed_queueid")
    private String feedQueueid;
	
	@TableField(value="review_send_time")
    private Date reviewSendTime;
	
	@TableField(value="review_send_operator")
    private String reviewSendOperator;
	
	@TableField(value="remark")
    private String remark;
	
	@TableField(value="operator")
    private String operator;
	
	@TableField(value="opttime")
    private Date opttime;
	
}
