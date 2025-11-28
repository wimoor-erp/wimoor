package com.wimoor.amazon.performance.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amz_coupon_asin")
@ApiModel(value="AmzCouponAsin", description="折扣券ASIN")
public class AmzCouponAsin {
   @MppMultiId
   @TableField(value= "couponid")
   String couponid;

   @MppMultiId
   @TableField(value= "asin")
   String asin;

}
