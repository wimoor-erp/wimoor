package com.wimoor.erp.purchase.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_planitem")
public class PurchasePlanItem extends ErpBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 694493552545913439L;

	@TableField(value= "subplanid")
    private String subplanid;

    @TableField(value= "materialid")
    private String materialid;
    
    @TableField(value= "warehouseid")
    private String warehouseid;

    @TableField(value= "status")
    private Byte status;

    @TableField(value= "amount")
    private Integer amount;

    @TableField(value= "itemprice")
    private BigDecimal itemprice;

    @TableField(value= "orderprice")
    private BigDecimal orderprice;

    @TableField(value= "supplier")
    private String supplier;

    @TableField(value= "shopid")
    private String shopid;
 
    @TableField(value= "isparent")
    private Boolean isparent;

    @TableField(value= "parent")
    private String parent;
 
    @TableField(value= "sales")
    private Integer sales;
    
    @TableField(value= "opttime")
    private Date opttime;
   

  
}