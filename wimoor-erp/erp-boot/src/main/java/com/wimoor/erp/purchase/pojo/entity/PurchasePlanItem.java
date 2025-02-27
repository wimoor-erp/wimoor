package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_v3_purchase_plan_item")
public class PurchasePlanItem extends ErpBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 694493552545913439L;

	@TableField(value= "planid")
    private String planid;

    @TableField(value= "materialid")
    private String materialid;
    
    @TableField(value= "warehouseid")
    private String warehouseid;
    
    @TableField(value= "groupid")
    private String groupid;
    
    @TableField(value= "batchnumber")
    private String batchnumber;

    @TableField(value= "amount")
    private Integer amount;

    @TableField(value= "shopid")
    private String shopid;
}