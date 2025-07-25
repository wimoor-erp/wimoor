package com.wimoor.amazon.performance.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_amz_promotions_performance_report")
@ApiModel(value="AmzPromotionsPerformanceReport", description="优惠")
public class AmzPromotionsPerformanceReport {
   @TableId(value= "promotionid")
   String promotionid;

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

   @TableField(value= "status")
   String status;

   @TableField(value= "type")
   String type;

   @TableField(value= "glance_views")
   Integer glanceViews;

   @TableField(value= "units_sold")
   Integer unitsSold;

   @TableField(value= "revenue")
   BigDecimal revenue;

   @TableField(value= "start_date_time")
   Date startDateTime;

   @TableField(value= "end_date_time")
   Date endDateTime;

   @TableField(value= "created_date_time")
   Date createdDateTime;

   @TableField(value= "last_updated_date_time")
   Date lastUpdatedDateTime;

   @TableField(value= "refreshtime")
   Date refreshtime;

}
