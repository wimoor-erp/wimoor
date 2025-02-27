package com.wimoor.amazon.performance.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_amz_promotions_asin")
@ApiModel(value="AmzPromotionsAsin", description="优惠产品")
public class AmzPromotionsAsin {
   @MppMultiId
   @TableField(value= "promotionid")
   String promotionid;

   @MppMultiId
   @TableField(value= "asin")
   String asin;

   @TableField(value= "currency")
   String currency;

   @TableField(value= "product_glance_views")
   Integer productGlanceViews;

   @TableField(value= "product_units_sold")
   Integer productUnitsSold;

   @TableField(value= "product_revenue")
   BigDecimal productRevenue;

   @TableField(value= "product_name")
   String productName;


}
