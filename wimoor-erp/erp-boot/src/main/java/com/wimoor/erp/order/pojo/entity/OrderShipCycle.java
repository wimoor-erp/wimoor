package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_order_shipcycle")
public class OrderShipCycle extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8529376573404561294L;

	@TableField(value= "sku")
	private String sku;
	
	@TableField(value= "warehouseid")
	private BigInteger warehouseid;

	@TableField(value= "transtype")
	private BigInteger transtype;
    
	@TableField(value= "stockingCycle")
    private Integer stockingcycle;

	@TableField(value= "min_cycle")
    private Integer minCycle; 
	
	@TableField(value= "first_leg_charges")
    private BigDecimal firstLegCharges;
	
	@TableField(value= "operator")
    private BigInteger operator;
	
	@TableField(value= "opttime")
    private Date opttime;
	
    
}