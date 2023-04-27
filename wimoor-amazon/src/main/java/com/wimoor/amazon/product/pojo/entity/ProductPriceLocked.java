package com.wimoor.amazon.product.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_price_locked")
@ApiModel(value="ProductPriceLocked对象", description="产品价格锁定")
public class ProductPriceLocked {
	
	@TableId(value="pid")
    private String pid;

	@TableField(value = "price")
    private BigDecimal price;

	@TableField(value = "starttime")
    private Date starttime;

	@TableField(value = "endtime")
    private Date endtime;
	
	@TableField(value = "disable")
    private Boolean disable;

	@TableField(value = "operator")
    private String operator;

	@TableField(value = "opttime")
    private Date opttime;
}