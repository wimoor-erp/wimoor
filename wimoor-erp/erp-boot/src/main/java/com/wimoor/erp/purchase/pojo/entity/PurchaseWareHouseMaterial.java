package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_v3_purchase_plan_warehouse_material")
public class PurchaseWareHouseMaterial {
	
	@TableField(value= "planid")
	private String planid;
	
	@TableField(value= "materialid")
    private String materialid;
	
	@TableField(value= "warehouseid")
    private String warehouseid;
 

}