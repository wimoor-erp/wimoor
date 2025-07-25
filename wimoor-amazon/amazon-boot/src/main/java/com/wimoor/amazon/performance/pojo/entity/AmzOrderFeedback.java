package com.wimoor.amazon.performance.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_amz_order_feedback")
@ApiModel(value="AmzOrderFeedback", description="订单反馈")
public class AmzOrderFeedback {
   @TableId(value= "amazon_order_id")
   String amazonOrderId;

   @TableField(value= "feeddate")
   Date feeddate;

   @TableField(value= "rating")
   Integer rating;

   @TableField(value= "comments")
   String comments;

   @TableField(value= "response")
   String response;

   @TableField(value= "email")
   String email;

   @TableField(value= "amazonauthid")
   String amazonauthid;

   @TableField(value= "refreshtime")
   Date refreshtime;
}
