package com.wimoor.erp.purchase.pojo.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_v3_purchase_plan")
public class PurchasePlan  extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1405555393100029595L;

	@TableField(value= "number")
    private String number;

    @TableField(value= "creator")
    private String creator;

    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "createtime")
    private Date createtime;
 
    @TableField(value= "disable")
    private Boolean disable;
    
    @TableField(exist = false)
	List<PurchasePlanWareHouse> warehouseList;

}