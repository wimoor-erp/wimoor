package com.wimoor.amazon.inbound.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_shipcycle")
public class FBAShipCycle extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8529376573404561294L;

	@TableField(value= "sku")
	private String sku;
	
	@TableField(value= "marketplaceid")
	private String marketplaceid;
	
	@TableField(value= "groupid")
	private BigInteger groupid;
    
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