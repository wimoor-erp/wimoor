package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_planmodelitem")
public class PurchasePlanModelItem extends ErpBaseEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1693664034380950344L;

	@TableField(value= "materialid")
    private String materialid;

	@TableField(value= "modelid")
    private String modelid;
	
	
	@TableField(value= "planamount")
    private Integer planamount;

	@TableField(value= "itemprice")
    private BigDecimal itemprice;

	@TableField(value= "orderprice")
    private BigDecimal orderprice;
	
	@TableField(value= "invamount")
	private Integer invamount;
 
	
}