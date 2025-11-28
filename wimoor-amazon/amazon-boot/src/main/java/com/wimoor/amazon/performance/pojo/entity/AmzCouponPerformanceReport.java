package com.wimoor.amazon.performance.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_amz_coupon_performance_report")
@ApiModel(value="AmzCouponPerformanceReport", description="折扣券")
public class AmzCouponPerformanceReport {
   @TableId(value= "couponid")
   String couponid;

   @TableField(value= "merchantid")
   String merchantid;

   @TableField(value= "amazonauthid")
   String amazonauthid;

   @TableField(value= "marketplaceid")
   String marketplaceid;

   @TableField(value= "currency")
   String currency;

   @TableField(value= "name")
   String name;

   @TableField(value= "websit_message")
   String websitMessage;

   @TableField(value= "start_date_time")
   Date startDateTime;

   @TableField(value= "end_date_time")
   Date endDateTime;

   @TableField(value= "discount_type")
   String discountType;

   @TableField(value= "discount_amount")
   BigDecimal discountAmount;

   @TableField(value= "total_discount")
   BigDecimal totalDiscount;

   @TableField(value= "clips")
   Integer clips;

   @TableField(value= "redemptions")
   Integer redemptions;

   @TableField(value= "budget")
   BigDecimal budget;

   @TableField(value= "budget_spend")
   BigDecimal budgetSpend;

   @TableField(value= "budget_remaining")
   BigDecimal budgetRemaining;

   @TableField(value= "budget_percentage_used")
   BigDecimal budgetPercentageUsed;

   @TableField(value= "sales")
   BigDecimal sales;

   @TableField(value= "refreshtime")
   Date refreshtime;

}
