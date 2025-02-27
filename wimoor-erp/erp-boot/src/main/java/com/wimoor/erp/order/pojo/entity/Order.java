package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Material对象", description="产品物流对象")
@TableName("t_erp_order")
public class Order  extends ErpBaseEntity {
    @TableField("platform_id")
    String platformId;

    @TableField("order_id")
    String orderId;

    @TableField("sku")
    String sku;

    @TableField("warehouseid")
    String warehouseid;

    @TableField("isout")
    boolean isout;

    @TableField("quantity")
    Integer quantity;

    @TableField("price")
    BigDecimal price;

    @TableField("purchase_date")
    Date purchaseDate;

    @TableField("ship_fee")
    BigDecimal shipFee;

    @TableField("referral_fee")
    BigDecimal referralFee;

    @TableField("referral_rate")
    BigDecimal referralRate;

    @TableField("shopid")
    String shopid;
}
