package com.wimoor.erp.inventory.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_inventory_month_summary")
public class InventoryMonthSummary extends BaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 50899126190573904L;

    @TableField(value= "shopid")
    private BigInteger shopid;
    
    @TableField(value= "materialid")
	private BigInteger materialid;

    @TableField(value= "warehouseid")
    private BigInteger warehouseid;

    @TableField(value= "month")
    private Date month;
    
    @TableField(value= "startqty")
    private Integer startqty;
    
    @TableField(value= "endqty")
    private Integer endqty;

    @TableField(value= "shipment")
    private Integer shipment;

    @TableField(value= "purchase")
    private Integer purchase;

    @TableField(value= "otherout")
    private Integer otherout;

    @TableField(value= "otherin")
    private Integer otherin;

    @TableField(value= "dispatch")
    private Integer dispatch;
    
    @TableField(value= "assembly")
    private Integer assembly;
    
    @TableField(value= "diff")
    private Integer diff;

    @TableField(value="stock")
    private Integer stock;
    
    @TableField(value="period")
    private BigDecimal period;
    
    @TableField(value="turndays")
    private BigDecimal turndays;
    
    @TableField(value= "refreshtime")
    private Date refreshtime;
    
}