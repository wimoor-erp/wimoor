package com.wimoor.erp.warehouse.pojo.entity;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName("t_erp_whse_unsalable_rpt")
public class WhseUnsalableReport   {
	@TableField(value= "shopid")
    private String shopid;

	@TableField(value= "wid")
    private String wid;

	@TableField(value= "mtid")
    private String mtid;

	@TableField(value= "sku")
    private String sku;
	
	@TableField(value= "groupid")
    private String groupid;
	
	@Size(max=36,message="名称不能超过36个字符")
	@TableField(value= "name")
    private String name;

	@TableField(value= "qtysum")
    private BigDecimal qtysum;
	
	@TableField(value= "qtyablesum")
    private BigDecimal qtyablesum;
	
	@TableField(value= "qtysum30")
    private BigDecimal qtysum30;
	
	@TableField(value= "qtysum60")
    private BigDecimal qtysum60;
	
	@TableField(value= "qtysum90")
    private BigDecimal qtysum90;

	@TableField(value= "qtysum180")
    private BigDecimal qtysum180;

	@TableField(value= "qtysum365")
    private BigDecimal qtysum365;

	@TableField(value= "salesum30")
    private BigDecimal salesum30;
	
	@TableField(value= "salesum60")
    private BigDecimal salesum60;
	
	@TableField(value= "salesum90")
    private BigDecimal salesum90;

	@TableField(value= "salesum180")
    private BigDecimal salesum180;

	@TableField(value= "salesum365")
    private BigDecimal salesum365;
	
	@TableField(value="nostock30")
	private BigDecimal nostock30;
 
    
}