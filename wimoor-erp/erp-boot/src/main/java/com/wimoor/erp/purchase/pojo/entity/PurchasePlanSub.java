package com.wimoor.erp.purchase.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_plansub")
public class PurchasePlanSub extends ErpBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1184389229341197127L;

	@TableField(value= "planid")
    private String planid;

    @TableField(value= "status")
    private Integer status;

    @TableField(value= "ftype")
    private String ftype;
     
}